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
                <option value="http://172.16.1.31">http://172.16.1.31</option>
                <option value="http://proda.ictrc.ac.ir">http://proda.ictrc.ac.ir</option>
            </select>
        </p>
        <p id="auth-form">
            <input id="username" placeholder="username" value="mttest"/>
            <input id="password" placeholder="password" value="@#32mttestRT98"/>
            <button type="button" id="get-token">signin</button>
        </p>
        <p>
            <input id="auth" placeholder="auth-token"/><input id="lang" placeholder="lang" value="en"/>
        </p>
        <p>
            <button type="button" id="exec">execute</button>
        </p>
    </section>
    <section id="right-pane">

    </section>
</body>

<script src="/js/jquery.min.js"></script>
<script src="/js/webservice.js"></script>
<script src="/js/beautify-json.js"></script>

<script>
//<![CDATA[
let testData = [];

$(window).on("load", function () {

    $("#get-token").on("click", function () {
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
            }
        );
    });




    $("#exec").on("click", function () {
        $('#right-pane').html("");
        let baseUrl = $("#base-url").val();

        postJson(
            baseUrl + "/test/web/search",
            {
                "sort": ["order:asc"],
                "condition": {
                    "operator": "AND",
                    "items": [
                        {"col": "isUnitTest", "type": "EQUAL", "value": true}
                    ]
                }
            },
            $.trim($("#auth").val()),
            "en",
            function (data) {
                testData = data;
                executeTest(testData.shift());
            }
        );
    });
});

function executeTest(item) {
    let baseUrl = $("#base-url").val(),
        auth = $.trim($("#auth").val()),
        lang = $.trim($("#lang").val()),
        data = JSON.parse(item.data),
        url = baseUrl + item.url,
        method = item.method,
        d = {};

    let callBack = function (rdata, status) {
        verify(item.assertData, rdata)

        let html =
            '<div class="test-box clearfix">'
            + (item.assertStatusCode === status ? '<span class="test-passed">passed</span>' : '<span class="test-failed">failed</span>')
            + '<span class="test-title">' + item.title + ' <label class="test-url">' + item.url + '</label></span>'
            + '</div>';
        $('#right-pane').append(html);

        if (testData.length > 0) {
            executeTest(testData.shift());
        }
    };

    if (method === "GET") {
        get(url, data, auth, lang, callBack, callBack);
        return;
    }

    if (method === "POST") {
        post(url, data, auth, lang, callBack, callBack);
        return;
    }

    postJson(url, data, auth, lang, callBack, callBack);
}


function verify(base, data) {

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


//]]>
</script>
</html>
