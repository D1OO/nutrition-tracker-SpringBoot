function updateUserProfile() {
    clearErrorMessages();
    $.ajax({
        type: "POST",
        url: '/profile',
        data: $('#update-profile').serialize(),
        statusCode: {
            500: function () {
                $("#userSavingErrorBox").show(200);
            },
            400: function (response) {
                $.each(response.responseJSON, function (errorKey, errorMessage) {
                    $("#" + errorKey).text(errorMessage);
                });
            },
            302: function (response) {
                window.location.replace(response.responseText);
            }
        }
    });
}

function clearErrorMessages() {
    $('.errorServerValidation').text("");
    $("#userSavingErrorBox").hide(200);
}
