$(window).on("load", function () {

    jsonToSearch($("#jsonsearch").val());

    $("#add-search-item").on("click", function () {
        addSearchItem(
            $("#search-type").val(),
            $("#search-col").val(),
            $("#search-value").val()
        );
    });

    $('#dto-list-form').on('submit', function () {
        if ($("#jsonsearch").is(":hidden")) {
            searchToJson();
        }
    });

    $("#json-to-search").on("click", function () {
        jsonToSearch($("#jsonsearch").val());
    });

    $("#get-search-json").on("click", function () {
        searchToJson();
        $("#jsonsearch").toggle();
    });

    $("#delete-select-all").on("click", function () {
        $(".delete-check").prop("checked", $(this).is(":checked"));
    });

    $("#dto-form").on("submit", function () {
        let x = {};
        let nullProperties = [];
        $('#dto-form').find('input, select, textarea').each(function () {
            let e = $(this),
                v = e.val()
                isEmpty = !v || !$.trim(v);

            if (e.attr("id") === "asjson") {
                return;
            }

            if (isEmpty && e.attr("name") !== "password") {
                nullProperties.push(e.attr("name"));
            }

            if (e.hasClass("json") && !e.prop("multiple")) {
                if (isEmpty) {
                    return;
                }
                try {
                    v = JSON.parse(v);
                } catch (e) {
                    return;
                }
            }
            x[e.attr("name")] = v;
        });
        if (nullProperties.length > 0) {
            x["__nullProperties"] = nullProperties;
        }
        $('#asjson').val(JSON.stringify(x));
    });

    let t = $("#total");
    t.on("click", function () {
        let isChecked = t.attr("data-ischecked");
        if (isChecked === 'y') {
            $(".delete-check").removeAttr('checked')
        } else {
            $(".delete-check").attr('checked', 'checked')
        }
        t.attr("data-ischecked", isChecked === 'y' ? 'n' : 'y');
    });

    $(".vtx").each(function () {
        let t = $(this);
        t.on("dblclick", function (e) {
            if (e.ctrlKey) {
                if (t.hasClass("extended")) {
                    t.height("50px");
                    t.removeClass("extended");
                } else {
                    t.height(t[0].scrollHeight);
                    t.addClass("extended");
                }
            }
        });
    });

});


function searchToJson() {
    let page = $("#page-no").val(),
        q = {
            "pagination": !!page,
            "page": page,
            "length": $("#page-length").val(),
            "sort": [$("#sort").val() + ":" + $("#sortpos").val()],
        },
        items = $("#search-items li");

    if (items) {
        q.condition = {
            "operator": $("#search-op").val(),
            "items": []
        };
        items.each(function () {
            let e = $(this),
                type = e.attr("data-type"),
                col = e.attr("data-col"),
                value = $.trim(e.attr("data-value"));

            if (type === "IS_NULL" || type === "IS_NOT_NULL" || type === "IS_EMPTY" || type === "IS_NOT_EMPTY") {
                q.condition.items.push({"type": type, "col": col});
                return;
            }

            if (type ===  "EQUAL" || type === "NOT_EQUAL" || type === "LIKE" || type === "NOT_LIKE"
                || type === "LESS_THAN" || type === "GREATER_THAN" || type === "LESS_THAN_EQUAL"
                || type === "GREATER_THAN_EQUAL") {

                if (value === "true") {
                    value = true;
                } else if (value === "false") {
                    value = false;
                } else if (jQuery.isNumeric(value)) {
                    value = value.indexOf('.') ? parseFloat(value) : parseFloat(value);
                }
                q.condition.items.push({"type": type, "col": col, "value": value});
                return;
            }

            if (type === "FULL_SEARCH") {
                q.condition.items.push({"type": type, "value": value});
                return;
            }

            if (type === "CONTAINS_ALL-object") {
                const item = {"type": "CONTAINS_ALL", "col": col, "objects": {}},
                    values = value.split(',');
                for (let i = 0, l = values.length ; i < l ; ++i) {
                    const kv = values[i].split(':');
                    let v = $.trim(kv[1]);
                    if (v === "true") {
                        v = true;
                    } else if (v === "false") {
                        v = false;
                    } else if (jQuery.isNumeric(v)) {
                        v = v.indexOf('.') ? parseFloat(v) : parseFloat(v);
                    }
                    item.objects[$.trim(kv[0])] = v;
                }
                q.condition.items.push(item);
                return;
            }

            const item = {"type": type, "col": col, "values": []},
                values = value.split(',');
            for (let i = 0, l = values.length ; i < l ; ++i) {
                let v = $.trim(values[i]);
                if (v === "true") {
                    v = true;
                } else if (v === "false") {
                    v = false;
                } else if (jQuery.isNumeric(v)) {
                    v = v.indexOf('.') ? parseFloat(v) : parseFloat(v);
                }
                item.values.push(v);
            }
            q.condition.items.push(item);

        });
    }

    $("#jsonsearch").val(JSON.stringify(q));
}

function jsonToSearch(jsonStr) {
    if (!$.trim(jsonStr)) {
        return;
    }
    const json = JSON.parse(jsonStr);

    if (json.page) {
        $("#page-no").val(json.page);
    }
    if (json.lenth) {
        $("#page-length").val(json.lenth);
    }
    if (json.sort && json.sort.length > 0) {
        const s = json.sort[0].split(":");
        $("#sort").val($.trim(s[0]));
        $("#sortpos").val($.trim(s[1]));
    }
    $("#search-items").html("");
    if (json.condition) {
        $("#search-op").val(json.operator ? json.operator : "AND");

        if (json.condition.items) {
            for (let i = 0, l = json.condition.items.length ; i < l ; ++i) {
                let c = json.condition.items[i];
                addSearchItem(
                    c.type,
                    c.col,
                    c.value ? c.value : c.values.join(',')
                );
            }
        }
    }
}

function addSearchItem(t, c, v) {
    $("#search-items").append(
        "<li class='search-item' data-type='" + t + "' data-col='" + c + "' data-value='" + v + "'>"
        + "<button class='search-item-remove' type='button'>X</button> "
        + "<span class='si-col'>" + c + "</span><span class='si-type'>" + t
        + "</span><span class='si-value'>" + v + "</span>"
        + "</li>"
    );

    $(".search-item-remove").on("click", function () {
        $(this).parent(".search-item").remove();
    });

}
