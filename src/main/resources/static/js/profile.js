function updateUserProfile() {
    clearErrorMessages();
    $.ajax({
        type: "POST",
        url: '/profile',
        data: $('#update-profile').serialize(),
        statusCode: {
            500: function (response) {
                $("#userSavingErrorBox").show(200);
            },
            400: function (response) {
                $.each(response.responseJSON, function (errorKey, errorMessage) {
                    $("#" + errorKey).text(errorMessage);
                });
            }
        },
        success: function (response) {
            window.location.replace(response.url);
        }
    });
}

function clearErrorMessages() {
    $('.errorServerValidation').text("");
    $("#userSavingErrorBox").hide(200);
}
