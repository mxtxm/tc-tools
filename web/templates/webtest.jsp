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
        <p><input id="local" placeholder="local base-url" value="http://localhost:8081"/></p>
        <p><input id="production" placeholder="local base-url" value="http://cel.ictrc.ac.ir"/></p>
        <p><input type="checkbox" id="islocal" checked="checked"/> <label for="islocal">local</label></p>
        <p><input id="username" placeholder="username" value="mttest"/></p>
        <p><input id="password" placeholder="password" value="@#32mttestRT98"/></p>
        <p><button type="button" id="get-token">signin</button></p>
        <p><input id="auth" placeholder="auth-token"/></p>
        <p><input id="lang" placeholder="lang" value="en"/></p>
        <p><input id="web-service-path" placeholder="web-service path"/></p>
        <p>
            <select id="method">
                <option value="POST" selected>POST</option>
                <option value="POSTJSON">POST JSON</option>
                <option value="POSTFILE">POST FILE</option>
                <option value="GET">GET</option>
            </select>
        </p>
        <p><textarea id="data" placeholder="JSON"></textarea></p>
        <p><button type="button" id="exec">execute</button></p>
    </section>
    <section id="right-pane">
        <div><pre id="status"></pre></div>
        <div><pre id="success-container"></pre></div>
        <div><pre id="error-container"></pre></div>
    </section>
</body>

<script src="/js/jquery.min.js"></script>
<script src="/js/webservice.js"></script>
<script src="/js/beautify-json.js"></script>

<script>
//<![CDATA[
$(window).on("load", function () {

    $("#get-token").on("click", function () {
        let baseUrl = $($("#islocal").is(':checked') ? "#local" : "#production").val();
        post(
            baseUrl + "/ui/user/signin",
            {
                "username": $("#username").val(),
                "password": $("#password").val()
            },
            "",
            "",
            function (data) {
                $("#auth").val(data.value.token);
            }
        );
    });


    $("#exec").on("click", function () {
        let baseUrl = $($("#islocal").is(':checked') ? "#local" : "#production").val();

        $("#success-container, #error-container, #status").html("");
        $("#success-container, #error-container").hide();

        let method = $("#method").val(),
            url = baseUrl + $.trim($("#web-service-path").val()),
            data = $.trim($("#data").val()),
            auth = $.trim($("#auth").val()),
            lang = $.trim($("#lang").val());

        if (data.endsWith("}") && data.startsWith("{")) {
            data = JSON.parse(data);
        } else {
            let d = data.split('\n');
            data = {};
            for (let i = 0, l = d.length ; i < l ; ++i) {
                let kv = d[i].split(d[i].indexOf('=') ? '=' : ':');
                data[$.trim(kv[0])] = $.trim(kv[1]);
            }
        }

        let success = function (rdata, status) {
            $("#status").html(status);
            $("#success-container").html(JSON.stringify(rdata)).show().beautifyJSON({ type: "plain" });

            post(
                baseUrl + "/test/web/insert",
                {
                    url: url,
                    method: method,
                    data: JSON.stringify(data)
                },
                auth
            );
        };

        let fail = function (rdata, status) {
            $("#status").html(status);
            $("#error-container").html(JSON.stringify(rdata)).show().beautifyJSON({ type: "plain" });
        };

        if (method === "GET") {
            get(url, data, auth, lang, success, fail);
            return;
        }

        if (method === "POST") {
            post(url, data, auth, lang, success, fail);
            return;
        }

        if (method === "POSTFILE") {
            postFile(url, data, auth, lang, success, fail);
            return;
        }

        postJson(url, data, auth, lang, success, fail);
    });

});
//]]>
</script>
</html>
