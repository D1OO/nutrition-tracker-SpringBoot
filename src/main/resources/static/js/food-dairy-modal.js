function openAddFoodModalWindow(recordtab) {
    event.preventDefault();
    var data = $(recordtab).serialize();
    $.get('/food-diary/adding-entries-modal-window', data, function (data) {
        document.getElementById('modal-window').innerHTML = data;
        $("#modal-window").css("display", "block");
    });
}

function closeAddFoodModalWindow() {
    $('#modal-window').css("display", "none");
}

function setModalContainerTo(name) {
    $('#modalbody > *').css("display", "none");
    document.getElementById(name).style.display = "block";
}

function addedNewEntry(foodDTO, foodName) {
    // event.preventDefault();
    var data = '&foodDTOJSON=' + foodDTO + '&foodName=' + foodName + '&newEntriesDTOJSON=' + $('#new-entries-list').val();
    $.post('/food-diary/modal-window/added-entry', data, function (response) {
        $('#new-entries-container').html(response);
    });
}

function removedEntry(index) {
    $(this).closest('tr').remove();
    data = 'index=' + index + '&newEntriesDTOJSON=' + $('#new-entries-list').val();
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
