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
            x['nullProperties'] = nullProperties;
        }
        $('#asjson').val(JSON.stringify(x));
    });
});
