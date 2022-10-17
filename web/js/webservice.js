function showMsg(msg) {
    let html =
        '<p id="message">' + msg + '</p>' +
        '<p id="message-button-container"><button id="close-msg">Close</button></p>';

    $("#msg").html(html).show();

    $("#close-msg").on("click", function () {
        $("#msg").hide();
    });
}

function showConfirm(msg, fn) {
    let html =
        '<p id="message">' + msg + '</p>' +
        '<p id="message-button-container"><button id="close-msg">No</button> <button id="yes-msg">Yes</button></p>';

    $("#msg").html(html).show();

    $("#close-msg").on("click", function () {
        $("#msg").hide();
    });

    $("#yes-msg").on("click", function () {
        $("#msg").hide();
        fn();
    });
}

function loading(status) {
    if (status) {
        $("#loading").show();    
    } else {
        $("#loading").hide();
    }
}

function get(url, data, auth, lang, success, fail) {
    loading(true);
    $.ajax({
        url: url,
        type: "GET",
        data: data,
        dataType: "json",
        timeout: 10800000,
        beforeSend: function(request) {
            if (auth) {
                request.setRequestHeader("X-Auth-Token", auth);
            }
            if (lang) {
                request.setRequestHeader("X-Lang", lang);
            }
        },
        success: function (data, status, xhr) {
            loading(false);
            if (xhr.status === 204) {
                data = "";
            }
            if (typeof success === "function") {
                success(data, xhr.status);
            }
        },
        error: function (xhr) {
            loading(false);
            if (typeof fail === "function") {
                fail(xhr.responseJSON, xhr.status);
            }
        }
    });
}


function getString(url, data, auth, lang, success, fail) {
    loading(true);
    $.ajax({
        url: url,
        type: "GET",
        data: data,
        timeout: 10800000,
        beforeSend: function(request) {
            if (auth) {
                request.setRequestHeader("X-Auth-Token", auth);
            }
            if (lang) {
                request.setRequestHeader("X-Lang", lang);
            }
        },
        success: function (data, status, xhr) {
            loading(false);
            if (xhr.status === 204) {
                data = "";
            }
            if (typeof success === "function") {
                success(data, xhr.status);
            }
        },
        error: function (xhr) {
            loading(false);
            if (typeof fail === "function") {
                fail(xhr.responseJSON, xhr.status);
            }
        }
    });
}


function post(url, data, auth, lang, success, fail) {
    loading(true);
    $.ajax({
        url: url,
        type: "POST",
        data: data,
        dataType: "json",
        timeout: 10800000,
        beforeSend: function(request) {
            if (auth) {
                request.setRequestHeader("X-Auth-Token", auth);
            }
            if (lang) {
                request.setRequestHeader("X-Lang", lang);
            }
        },
        success: function (data, status, xhr) {
            loading(false);
            if (xhr.status === 204) {
                data = "";
            }
            if (typeof success === "function") {
                success(data, xhr.status);
            }
        },
        error: function (xhr) {
            loading(false);
            if (typeof fail === "function") {
                fail(xhr.responseJSON, xhr.status);
            }
        }
    });
}


function postJson(url, data, auth, lang, success, fail) {
    loading(true);
    $.ajax({
        url: url,
        type: "POST",
        data: JSON.stringify(data),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        timeout: 10800000,
        beforeSend: function(request) {
            if (auth) {
                request.setRequestHeader("X-Auth-Token", auth);
            }
            if (lang) {
                request.setRequestHeader("X-Lang", lang);
            }
        },
        success: function (data, status, xhr) {
            loading(false);
            if (xhr.status === 204) {
                data = "";
            }
            if (typeof success === "function") {
                success(data, xhr.status);
            }
        },
        error: function (xhr) {
            loading(false);
            if (typeof fail === "function") {
                fail(xhr.responseJSON, xhr.status);
            }
        }
    });
}


function getSynched(url, data, auth, lang, success, fail) {
    loading(true);
    let x = $.ajax({
        url: url,
        type: "GET",
        data: data ? data : {},
        async: false,
        timeout: 10800000,
        beforeSend: function(request) {
            if (auth) {
                request.setRequestHeader("X-Auth-Token", auth);
            }
            if (lang) {
                request.setRequestHeader("X-Lang", lang);
            }
        },
        success: function (data, status, xhr) {
            loading(false);
            if (xhr.status === 204) {
                data = "";
            }
            if (typeof success === "function") {
                success(data, xhr.status);
            }
        },
        error: function (xhr) {
            loading(false);
            if (typeof fail === "function") {
                fail(xhr.responseJSON, xhr.status);
            }
        }
    }).responseText;
    loading(false);
    return x;
}

function postFile(url, data, files, auth, lang, success, fail) {
    loading(true);

    let formData = new FormData();
    if (typeof files === 'object') {
        $.each(files, function (k, v) {
            formData.append(k, v);
        });
    } else  {
        formData.append("file", files);
    }
    $.each(data, function (k, v) {
        formData.append(k, v);
    });

    $.ajax({
        url: url,
        type: "POST",
        data: formData,
        async: true,
        cache: false,
        contentType: false,
        processData: false,
        timeout: 10800000,
        beforeSend: function(request) {
            if (auth) {
                request.setRequestHeader("X-Auth-Token", auth);
            }
            if (lang) {
                request.setRequestHeader("X-Lang", lang);
            }
        },
        success: function (data, status, xhr) {
            loading(false);
            if (xhr.status === 204) {
                data = "";
            }
            if (typeof success === "function") {
                success(data, xhr.status);
            }
        },
        error: function (xhr) {
            loading(false);
            if (typeof fail === "function") {
                fail(xhr.responseJSON, xhr.status);
            }
        }
    });
}

function postSynch(url, data, auth, lang, success, fail) {
    loading(true);
    $("#error-container").html("");
    $.ajax({
        url: url,
        type: "POST",
        data: data,
        dataType: "json",
        async: false,
        timeout: 10800000,
        beforeSend: function(request) {
            if (auth) {
                request.setRequestHeader("X-Auth-Token", auth);
            }
            if (lang) {
                request.setRequestHeader("X-Lang", lang);
            }
        },
        success: function (data, status, xhr) {
            loading(false);
            if (xhr.status === 204) {
                data = "";
            }
            if (typeof success === "function") {
                success(data, xhr.status);
            }
        },
        error: function (xhr) {
            loading(false);
            if (typeof fail === "function") {
                fail(xhr.responseJSON, xhr.status);
            }
        }
    });
}


function inspect() {

    function dump(o, d) {
        let t  = typeof o,
            ot = 'DICTIONARY {\n',
            b  = '}';

        if (t === 'object') {
            if (o) {
                if (Array.isArray(o)) {
                    ot = 'LIST [\n';
                    b  = ']';
                }
            } else {
                t = 'null';
            }
        }

        switch (t) {
            case 'function':
                return 'function ()';
            case 'string':
                return 'string "' + o + '"';
            case 'number':
            case 'boolean':
                return t + ' ' + o;
            case 'object':
                let dd = d+'        ',
                    s  = [];

                $.each(o, function(k, v) {
                    s.push(dd + k + ': ' + dump(v, dd));
                });
                return ot + s.join(',\n') + '\n' + d + b;
        }
        return t;
    }

    let t = '',
        i, l=arguments.length;

    for(i=0; i<l; ++i) {
        t += dump(arguments[i], '')+"\n";
    }

    return t;
}


function objectToHtml(obj, cls) {
    if (Array.isArray(obj)) {
        let html = "<table" + (cls ? " class='" + cls + "'" : "") + ">";
        let l = obj.length;
        if (l === 0) {
            return "empty";
        }
        for (let i=0; i<l; ++i) {
            vl = obj[i];
            if (typeof(vl) == "object") {
                vl = objectToHtml(vl, "inner");
            }
            html += "<tr><td class='view-value'>" + vl + "</td></tr>";
        }
        html += "</table>";
        return html;
    }

    let html = "<table" + (cls ? " class='" + cls + "'" : "") + ">";
    let hasData = false;
    $.each(obj, function(k, v) {
        hasData = true;
        if (typeof(v) == "object") {
            if (typeof v.length === 'number' && !(v.propertyIsEnumerable('length')) && typeof v.splice === 'function') {
                v = objectToHtml(v, "inner");
            } else {
                v = objectToHtml(v, "inner");
            }
        }
        html += "<tr><td class='view-label'>" + k + "</td><td class='view-value'>" + v + "</td></tr>";
    });

    if (!hasData) {
        return "empty";
    }

    return html + "</table>";
}

function searchToHtml(obj, cls) {
    if (typeof obj.length === 'number' && !(obj.propertyIsEnumerable('length')) && typeof obj.splice === 'function') {
        let html = "";
        let l = obj.length;
        if (l === 0) {
            return "empty";
        }
        for (let i=0; i<l; ++i) {
            html += "<div class='list-item'>";
            html += objectToHtml(obj[i], "inner");
            html += "</div>";
        }
        return html;
    }
}
