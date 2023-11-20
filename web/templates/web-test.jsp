<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'/>
    <meta name="description" content="Vantar admin dashboard">
    <meta name="author" content="Mehdi Torabi">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TC-Tools</title>
    <link rel="stylesheet" type="text/css" href="/css/webtest.css"/>
</head>
<body class="clearfix">
    <section id="left-pane">
        <p>
            <select id="base-url">
                <option value="http://localhost:8081">http://localhost:8081</option>
                <option value="http://185.147.162.67:8080">http://185.147.162.67:8080/</option>
            </select>
        </p>
        <p id="auth-form">
            <input id="username" placeholder="username" value="mttest"/>
            <input id="password" placeholder="password" value="@#32mttestRT98"/>
            <button type="button" id="get-token">signin</button>
        </p>

        <p><input id="title" placeholder="title" value="Test"/></p>
        <p>
            <input id="auth" placeholder="auth-token"/><input id="lang" placeholder="lang" value="en"/>
        </p>
        <p><input id="web-service-path" placeholder="web-service path"/></p>
        <p>
            <select id="method">
                <option value="POST" selected>POST</option>
                <option value="POSTJSON">POST JSON</option>
                <option value="GET">GET</option>
                <option value="POSTFILE">POST FILE</option>
            </select>
        </p>

        <div id="file-container">
            <div>
                <input id="file-key" placeholder="FILE KEY"/>
                <button type="button" id="file-add-row">add</button>
            </div>
            <div id="files"></div>
        </div>

        <p><textarea id="data" placeholder="JSON"></textarea></p>
        <p>
            <button type="button" id="exec">execute</button>
            <input type="checkbox" id="store-test"/> <label for="store-test">store test</label>
        </p>
        <p>
            <button type="button" id="add-pre-test">add pre-test</button>
        </p>
        <div id="added-tests"></div>

        <p>
            <button type="button" id="open-unit-test">unit-test...</button>
        </p>
        <div id="assert">
            <p><input id="order" placeholder="unit-test order" value="1"/></p>
            <p><input id="assert-status-code" placeholder="ASSERT status code" value="200"/></p>
            <p><textarea id="assert-data" placeholder="ASSERT"></textarea></p>
            <p><button type="button" id="add-unit-test">add unit-test</button></p>
        </div>
    </section>
    <section id="right-pane"></section>
</body>

<script src="/js/jquery.min.js"></script>
<script src="/js/webservice.js"></script>
<script src="/js/beautify-json.js"></script>

<script>
//<![CDATA[
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


    const m = $("#method");
    m.on("change", function () {
        if (m.val() === "POSTFILE") {
            $("#file-container").show();
        } else {
            $("#file-container").hide();
        }
    });



    $('#file-add-row').on("click", function () {
        const key = $("#file-key").val();
        if (!key) {
            return
        }
        $("#files").append(
            '<div>' +
            '<input class="file-select" id="' + key + '" type="file" placeholder="upload file"/>' +
            '</div>'
        );
        $("#file-key").val("");
    });




    $("#open-unit-test").on("click", function () {
        $("#assert").toggle();
    });



    $("#get-token").on("click", function () {
        working(true);
        post(
            $("#base-url").val() + "/ui/user/signin",
            {
                "username": $("#username").val(),
                "password": $("#password").val()
            },
            "",
            "",
            function (data) {
                $("#auth").val(data.accessToken ? data.accessToken : data.dto.token);
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




    $("#add-unit-test").on("click", function () {
        working(true);
        post(
            $("#base-url").val() + "/test/web/insert",
            {
                title: $("#title").val(),
                url: $("#web-service-path").val(),
                method: $("#method").val(),
                data: JSON.stringify(fixData($("#data").val())),
                isUnitTest: true,
                assertStatusCode: $("#assert-status-code").val(),
                assertData: $("#assert-data").val(),
                order: $("#order").val()
            },
            $.trim($("#auth").val()),
            "",
            function (data) {
                working(false);
                $("#status, #error-container").append("");
                $("#success-container").html("TEST STORED");
                alert("TEST STORED");

                let e = $("#order");
                e.val(parseInt(e.val(), 10) + 1);
                $("#assert-status-code").val("200");
                $("#assert-data").val("");
            }
        );
    });
});




function executeTest(tData) {
    working(true);

    let data = fixData(tData.data),
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
    if (method === "POSTFILE") {
        let files = {};
        $('.file-select').each(function () {
            let e = $(this);
            files[e.attr("id")] = $(e)[0].files[0];
        });
        postFile(url, data, files, auth, tData.lang, success, fail);
        return;
    }
    postJson(url, data, auth, tData.lang, success, fail);
}

function fixData(data) {
    if ((data.endsWith("}") && data.startsWith("{")) || data.endsWith("[") && data.startsWith("]")) {
        data = JSON.parse(data);
    } else {
        let d = data.split(data.includes('&') ? '&' : '\n');
        data = {};
        for (let i = 0, l = d.length ; i < l ; ++i) {
            let kv = d[i].split(d[i].indexOf('=') > 0 ? '=' : ':');
            data[trim(kv[0])] = trim(kv[1]);
        }
    }
    return data;
}

function trim(string) {
    string = $.trim($.trim(string).replace(/,+$/g, ''));
    return $.trim($.trim(string).replace(/^"+|"+$/g, ''));
}
//]]>
</script>
</html>
