/**
 * version: 4.9.18
 */
$(window).on("load", function () {

    $(".multi-select").multiSelect();

    // dto search: load previous search
    jsonToSearch($("#jsonsearch").val());
    // dto search: +
    $("#add-search-item").on("click", function () {
        addSearchItem($("#search-type").val(), $("#search-col").val(), $("#search-value").val(), true);
    });
    // dto search: json toggle button
    $("#get-search-json").on("click", function () {
        $("#jsonsearch").toggle();
    });

    // delete delete:
    $("#delete-select-all").on("click", function () {
        $(".delete-check").prop("checked", $(this).is(":checked"));
    });
    // dto form:
    $("#dto-form").on("submit", function () {
        let x = {};
        let nullProperties = [];
        $('#dto-form').find('input, select, textarea').each(function () {
            let e = $(this),
                v = e.val(),
                n = e.attr("name"),
                isEmpty = !v || !$.trim(v);

            if (e.attr("id") === "asjson") {
                return;
            }

            if (isEmpty && n !== "password") {
                nullProperties.push(n);
            }

            if (e.hasClass("json") && !e.prop("multiple")) {
                if (isEmpty) {
                    return;
                }
                try {
                    v = JSON.parse(v);
                } catch (e) {
                    alert(e.message)
                    return;
                }
            }
            x[n] = v;
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
    // textarea expand
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

//EQUAL
//NOT_EQUAL
//
//LIKE
//NOT_LIKE
//FULL_SEARCH
//
//IN
//NOT_IN
//CONTAINS_ALL (for list)
//CONTAINS_ALL-object (operator=CONTAINS_ALL, for key-val)
//
//LESS_THAN
//GREATER_THAN
//LESS_THAN_EQUAL
//GREATER_THAN_EQUAL
//BETWEEN
//NOT_BETWEEN
//
//IS_NULL
//IS_NOT_NULL
//IS_EMPTY
//IS_NOT_EMPTY
//
//NEAR
//FAR (pseudo operator, converts into WITHIN condition)
//WITHIN
//
//IN_LIST
//IN_DTO
//QUERY
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

            // not supported
            if (type === "QUERY" || type === "IS_DTO" || type === "IN_LIST") {
                return;
            }
            // no value
            if (type === "IS_NULL" || type === "IS_NOT_NULL" || type === "IS_EMPTY" || type === "IS_NOT_EMPTY") {
                q.condition.items.push({"type": type, "col": col});
                return;
            }
            // has value
            if (type ===  "EQUAL" || type === "NOT_EQUAL" || type === "LIKE" || type === "NOT_LIKE"
                || type === "LESS_THAN" || type === "GREATER_THAN" || type === "LESS_THAN_EQUAL"
                || type === "GREATER_THAN_EQUAL") {

                if (value === "true") {
                    value = true;
                } else if (value === "false") {
                    value = false;
                } else if (jQuery.isNumeric(value)) {
                    value = value.indexOf('.') ? parseFloat(value) : parseInt(value, 10);
                }
                q.condition.items.push({"type": type, "col": col, "value": value});
                return;
            }
            if (type === "FULL_SEARCH") {
                q.condition.items.push({"type": type, "value": value});
                return;
            }

            // has values
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
                    v = v.indexOf('.') ? parseFloat(v) : parseInt(v, 10);
                }
                item.values.push(v);
            }
            q.condition.items.push(item);
        });
    }

    $("#jsonsearch").val(JSON.stringify(q));
}

function jsonToSearch(jsonStr) {
    if (isempty(jsonStr)) {
        return;
    }
    const json = JSON.parse($.trim(jsonStr));
    if (isempty(json)) {
        return;
    }

    if (!isempty(json.page)) {
        $("#page-no").val(json.page);
    }
    if (!isempty(json.lenth)) {
        $("#page-length").val(json.lenth);
    }
    if (!isempty(json.sort) && json.sort.length > 0) {
        const s = json.sort[0].split(":");
        $("#sort").val($.trim(s[0]));
        $("#sortpos").val($.trim(s[1]));
    }

    $("#search-items").html("");
    if (!isempty(json.condition)) {
        $("#search-op").val(json.operator ? json.operator : "AND");

        if (!isempty(json.condition.items)) {
            for (let i = 0, l = json.condition.items.length ; i < l ; ++i) {
                let c = json.condition.items[i]
                v = !isempty(c.value) ? c.value : (!isempty(c.values) ? c.values : "");

                if (typeof v === "object") {
                    v = Array.isArray(v) ? c.values.join(',') : v.toString();
                }

                addSearchItem(
                    c.type,
                    c.col,
                    v,
                    false
                );
            }
        }
    }
}

function addSearchItem(t, c, v, updateJson) {
    if (isempty(t)) {
        return;
    }
    if (t === "IS_NULL" || t === "IS_NOT_NULL" || t === "IS_EMPTY" || t === "IS_NOT_EMPTY") {
        v = "";
        if (isempty(c)) {
            return;
        }
    } else if (t === "FULL_SEARCH") {
        c = "";
        if (isempty(v)) {
            return;
        }
    } else {
        if (isempty(c) || isempty(v)) {
            return;
        }
    }

    $("#search-items").append(
        "<li class='search-item' data-type='" + t + "' data-col='" + c + "' data-value='" + v + "'>"
        + "<button class='search-item-remove' type='button'>X</button> "
        + "<span class='si-col'>" + c + "</span><span class='si-type'>" + t
        + "</span><span class='si-value'>" + v + "</span>"
        + "</li>"
    );

    if (updateJson) {
        searchToJson();
    }

    $(".search-item-remove").on("click", function () {
        $(this).parent(".search-item").remove();
        searchToJson();
    });
}

function dataHintB(u) {
    const e = $(u),
        o = e.offset(),
        s = $(document).scrollTop();
    $("#data-hint").css({top: o.top, left: o.left, display: "block"});
    $("#hint-container").html(e.parent().find(".field-hint").html());
}

function getLogWebInfo(userLogId) {

    function getKv(k, v) {
        return "<div class=\"kv-flex-container\" style=\"direction:ltr;justify-content:left;align-items:left\">\n"
            + "<label class=\"kv-key\" style=\"overflow-wrap:break-word;text-align:right\">" + k + "</label>"
            + "<label class=\"kv-value\" style=\"text-align:left\">" + v + "</label></div>";
    }

    get("/admin/data/log/web/get", {id: userLogId}, "", "", function (obj) {
        let headers = "";
        $.each(obj.headers, function (k, v) {
            headers += getKv(k, v);
        });
        let html =
            getKv("request type", obj.requestType)
            + getKv("IP", obj.ip)
            + getKv("request time", obj.time)
            + getKv("headers", headers);

        if (obj.paramsX) {
            let p = "";
            $.each(obj.paramsX, function (k, v) {
                p += getKv(k, v);
            });
            html += getKv("params", p);
        }

        if (obj.uploadedFiles) {
            html += getKv("uploaded files", obj.uploadedFiles.join("<br/>"));
        }
        if (obj.extraData) {
            let uData = "";
            $.each(obj.extraData, function (k, v) {
                uData += getKv(k, v);
            });
            html += getKv("user data", uData);
        }
        $(".kv-flex-container").last().replaceWith(html);
    }, function () {

    });
}


(function ($) {
    $.fn.multiSelect = function (options) {
        options = options || {};

        function add(x, e, id) {
            var i = $(x).index();
            if (i == 0) {
                return;
            }

            var atts = {
                hidden: true,
                text: x.text(),
                click: function () {
                    var t = $(this);
                    t.hide("fast");
                    t.remove();
                    x.attr('disabled', false);
                    e.children('option')[i].selected = false;
                    if (options.onRemove) {
                        options.onRemove(x.val(), x.text());
                    }
                }
            };

            if (x.val()) {
                atts.val = x.val();
            }

            var n = $('<li/>', atts).appendTo('#'+id+' ul');
            n.show();
            x.attr('disabled', true);
            e.children('option')[i].selected = true;

            if (options.onAdd) {
                options.onAdd(n, x.val(), x.text());
            }
        }

        return this.each(function () {
            var e = $(this);
            if (!e.data('multiSelect')) {
                e.hide();
                e.data('multiSelect', 1);
                var id = e.attr('id')+'-wrapper';
                $('<div class="multi-select" id="'+id+'"><select>'+e.html()+
                    '</select><ul></ul></div>').insertAfter(e);
                // adding item
                var s = $('#'+id).find('select');
                s.change(function () {
                    add(s.find(':selected'), e, id);
                    $(s).val($(s).find('option:first').val());
                });
                // initially selected items
                var f = function () {
                    e.children('option:selected').each(function () {
                        add(s.find('option[value="'+$(this).val()+'"]'), e, id);
                    });
                }
                f();
                e.change(f);
            }
        });
    };
} (jQuery));
