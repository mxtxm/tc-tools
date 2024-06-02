/**
 * version: 4.9.18
 */
let preTests = [],
    tests = [],
    requestCount = 1;


function working(isWorking) {
    $("#working").remove();
    if (isWorking) {
        $("#right-pane").prepend("<p id='working'>please wait...</p>");
    }
}


$(window).on("load", function () {

    let pw = $("#pick-webservice");
    pw.select2();
    pw.on("change", function () {
        let webServiceUrl = $("#pick-webservice").val();
        $("#web-service-path").val(webServiceUrl);
        get(
            "/admin/documentation/webservice/get/json",
            {
                url: webServiceUrl
            },
            $.trim($("#auth").val()),
            "en",
            function (rdata, status) {
                $("#method").val(rdata.httpMethod).trigger("change");

                const f = $("#files");
                f.html("");
                if (rdata.files) {
                    for (var i = 0, l = rdata.files.length ; i < l ; ++i) {
                        f.append(
                            '<div>' +
                            '<input class="file-select" id="' + rdata.files[i] + '" type="file" placeholder="upload file"/>' +
                            '</div>'
                        );
                    }
                }

                $("#doc-link").html("<a target='_blank' href='" + rdata.documentUrl + "'>documentation</a>");
                $("#data").val(rdata.inputSampleJson);
            },
            function (rdata, status) {

            }
        );
    });



    const m = $("#method");
    m.on("change", function () {
        if (m.val() === "POST UPLOAD FILE") {
            $("#file-container").show();
        } else {
            $("#file-container").hide();
        }
    });



    $('#file-add-row').on("click", function () {
        const fk = $("#file-key");
        const key = fk.val();
        if (!key) {
            return
        }
        $("#files").append(
            '<div>' +
            '<input class="file-select" id="' + key + '" type="file" placeholder="upload file"/>' +
            '</div>'
        );
        fk.val("");
    });



    $("#open-unit-test").on("click", function () {
        $("#assert").toggle();
    });



    $("#get-token").on("click", function () {
        working(true);
        post(
            $("#base-url").val() + "/api/user/signin",
            {
                "username": $("#username").val(),
                "password": $("#password").val()
            },
            "",
            "",
            function (data) {
                $("#auth").val(data.accessToken);
                working(false);
            }
        );
    });



    $("#add-pre-test").on("click", function () {
        let path = $("#web-service-path").val();
        $("#added-tests").append("<p class='pre-test-item'>" + path + "</p>").show();
        preTests.push({
            method: $("#method").val(),
            url:  $.trim(path),
            data: $.trim($("#data").val()),
            lang: $.trim($("#lang").val())
        });
    });



    $("#exec").on("click", function () {
        $("#right-pane").html("");

        tests = [];
        for (let j = 0, m = preTests.length ; j < m ; ++j) {
            tests.push(preTests[j])
        }

        tests.push({
            method: $("#method").val(),
            url:  $.trim($("#web-service-path").val()),
            data: $.trim($("#data").val()),
            lang: $.trim($("#lang").val())
        });

        executeTest(tests.shift());
    });
});




function executeTest(tData) {
    working(true);

    let data = stringToObject(tData.data),
        startTime =  performance.now(),
        method = tData.method,
        baseUrl = $("#base-url").val(),
        auth = $.trim($("#auth").val()),
        url = baseUrl + tData.url,
        // > > > ADD RESULT
        addResult = function (status, successData, failData) {
            let statusId = "status" + requestCount,
                requestDataContainerId = "request-data-container" + requestCount,
                successContainerId = "success-data" + requestCount,
                failContainerId = "fail-data" + requestCount,
                requestDataId = "request-data" + requestCount,
                html = '<div><pre id="' + statusId + '" class="response-status">'
                    + status + "  " + method + ": " + url + '</pre>'
                    + '<pre>' + ((performance.now() - startTime) / 1000) + 's</pre>'
                    + '<div id="' + requestDataContainerId + '" class="request-data">'
                    + '<pre id="' + requestDataId + '">' + JSON.stringify(data) + '</pre>'
                    + '</div></div>';

            if (successData) {
                html += '<div><pre id="' + successContainerId + '" class="success-container">' + successData + '</pre></div>';
            }
            if (failData) {
                html += '<div><pre id="' + failContainerId + '" class="error-container">' + failData + '</pre></div>';
            }
            $('#right-pane').prepend('<div class="result-item">' + html + '</div>');

            $("#" + statusId).on('click', function () {
                $("#" + requestDataContainerId).toggle();
            });

            $("#" + requestDataId + ",#" + successContainerId + ",#" + failContainerId).beautifyJSON({ type: "plain" })

            ++requestCount;
        },
        // > > > SUCCESS
        success = function (rdata, status) {
            working(false);
            addResult(status, JSON.stringify(rdata), false);

            if (tests.length > 0) {
                executeTest(tests.shift());
            }

            if ($("#store-test").is(":checked")) {
                post(
                    baseUrl + "/test/web/insert",
                    {
                        title: $("#title").val(),
                        url: tData.url,
                        method: tData.method,
                        data: JSON.stringify(data),
                        isUnitTest: false,
                    },
                    auth
                );
            }
        },
        // > > > FAIL
        fail = function (rData, status) {
            working(false);

            $("#status").html(status);
            addResult(status, false, JSON.stringify(rData));
        };

    if (method === "GET") {
        get(url, data, auth, tData.lang, success, fail);
        return;
    }
    if (method === "POST") {
        post(url, data, auth, tData.lang, success, fail);
        return;
    }
    if (method === "POST UPLOAD FILE") {
        let files = {};
        $('.file-select').each(function () {
            let e = $(this);
            files[e.attr("id")] = $(e)[0].files[0];
        });
        postFile(url, data, files, auth, tData.lang, success, fail);
        return;
    }
    if (method === "POST JSON") {
        postJson(url, data, auth, tData.lang, success, fail);
        return;
    }
    if (method === "DOWNLOAD (GET DIRECT)") {
        let p = "";
        $.each(data, function (k, v) {
            p = "&" + k + "=" + v;
        });
        window.open(
            url + "?x=" + auth + "&lang=" + tData.lang + p,
            "_blank"
        );
        return;
    }
    alert(method + " not supported");
}

function stringToObject(data) {
    if ((data.endsWith("}") && data.startsWith("{")) || data.endsWith("[") && data.startsWith("]")) {
        data = JSON.parse(data);
    } else {
        let d = data.split(data.includes('&') ? '&' : '\n');
        data = {};
        for (let i = 0, l = d.length ; i < l ; ++i) {
            let kv = d[i],
                iSep = kv.indexOf('=');
            if (iSep < 0) {
                iSep = kv.indexOf(':');
            }
            if (iSep < 0) {
                continue
            }
            let k = kv.substring(0, iSep);
            let v = kv.substring(iSep + 1);

            data[trim(k)] = v ? trim(v) : "";
        }
    }
    return data;
}

function trim(string) {
    string = $.trim($.trim(string).replace(/,+$/g, ''));
    return $.trim($.trim(string).replace(/^"+|"+$/g, ''));
}
