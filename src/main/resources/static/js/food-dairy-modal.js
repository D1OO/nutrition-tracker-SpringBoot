function openAddFoodModalWindow(recordtab) {
    $.get('/food-diary/modal-window', $(recordtab).serialize(), function (data) {
        $('#modal-window').html(data);
        $("#modal-window").show();
    });
}

function closeAddFoodModalWindow() {
    $('#modal-window').hide();
}

function setModalContainerTo(name) {
    $('#modalbody > *').hide();
    $('#' + name).show();
}

function updateFoodList(container) {
    $.post('/food-diary/modal-window/updated-food', function (response) {
        $('#food-container').html(response);
    });
    setModalContainerTo(container)
}

function addedNewEntry(foodDTO, foodName) {
    const data = '&foodDTOJSON=' + foodDTO + '&foodName=' + foodName + '&newEntriesDTOJSON=' + $('#new-entries-list').val();
    $.post('/food-diary/modal-window/added-entry', data, function (response) {
        $('#new-entries-container').html(response);
    });
}

function removedEntry(index) {
    $(this).closest('tr').remove();
    const data = 'index=' + index + '&newEntriesDTOJSON=' + $('#new-entries-list').val();
    $.post('/food-diary/modal-window/removed-entry', data, function (response) {
        $('#new-entries-container').html(response);
    });
}

function saveNewEntries(record) {
    clearErrorMessages();
    $.ajax({
        type: "POST",
        url: '/food-diary/modal-window/save-new-entries',
        data: $('#new-entries-form').serialize(),
        statusCode: {
            500: function () {
                $("#foodSavingErrorBox").show(200);
            },
            400: function (response) {
                $.each(response.responseJSON, function (errorKey) {
                    $('input[id="' + errorKey + '"]').css("border", "1px solid red");
                });
            }
        },
        success: function () {
            closeAddFoodModalWindow();
            loadFromServerIntoContentContainer('/food-diary/day?d=' + record);
        }
    });
}

function saveCreatedFood() {
    clearErrorMessages();
    $.ajax({
        type: "POST",
        url: '/food-diary/modal-window/save-new-food',
        data: $('#createfoodform').serialize(),
        statusCode: {
            500: function () {
                $("#foodSavingErrorBox").show(200);
            },
            400: function (response) {
                $.each(response.responseJSON, function (errorKey, errorMessage) {
                    $("#" + errorKey).text(errorMessage);
                });
            }
        },
        success: function () {
            $("#foodSavedSuccessBox").show(200);
        }
    });
}

function clearErrorMessages() {
    $('.errorServerValidation').text("");
    $("#foodSavingErrorBox").hide(100);
    $("#articleSavingErrorBox").hide(100);
    $("#foodSavedSuccessBox").hide(100);
}
