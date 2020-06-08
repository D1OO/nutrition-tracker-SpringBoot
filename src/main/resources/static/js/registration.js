function saveNewUser() {
    $('.loading').slideDown(100);
    clearErrorMessages();
    $.ajax({
        type: "POST",
        url: '/signup',
        data: $('#registration').serialize(),
        statusCode: {
            500: function (response) {
                $(".alert-danger").show(200);
                $('.loading').slideUp(100);
            },
            400: function (response) {
                $.each(response.responseJSON, function (errorKey, errorMessage) {
                    $('p[id="' + errorKey + '"]').text(errorMessage);
                });
                $('.loading').slideUp(100);
            }
        },
        success: function (response) {
            window.location.replace(response.url);
            $('.loading').slideUp(100);
        }
    });
}

function clearErrorMessages() {
    $('.errorServerValidation').text("");
    $(".alert-danger").hide(200);
}
