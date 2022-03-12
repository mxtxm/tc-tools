$(window).on("load", function () {
    $('#dto-form').on('submit', function () {
        let x = {};
        let nullProperties = [];
        $('#dto-form').find('input, select, textarea').each(function () {
            let e = $(this),
                v = e.val()
                isEmpty = !v || !$.trim(v);

            if (e.attr('id') === 'asjson') {
                return;
            }

            if (isEmpty && e.attr('name') !== "password") {
                nullProperties.push(e.attr('name'));
            }

            if (e.hasClass('json') && !e.prop('multiple')) {
                if (isEmpty) {
                    return;
                }
                try {
                    v = JSON.parse(v);
                } catch (e) {
                    return;
                }
            }
            x[e.attr('name')] = v;
        });
        if (nullProperties.length > 0) {
            x['__nullProperties'] = nullProperties;
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

});
