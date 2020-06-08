function openAddFoodModalWindow(recordtab) {
    event.preventDefault();
    var data = $(recordtab).serialize();
    $.get('/food-diary/adding-entries-modal-window', data, function (data) {
        document.getElementById('modal-window').innerHTML = data;
        $("#modal-window").css("display", "block");
    });
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

function saveNewEntries() {
    event.preventDefault();
    var data = $('#new-entries-form').serialize();
    $.post('/food-diary/modal-window/save-new-entries', data, replacePageWith);
}

function saveCreatedFood() {
    event.preventDefault();
    var data = $('#createfoodform').serialize();
    $.post('/food-diary/modal-window/save-new-food', data, replacePageWith);
}

function closeAddFoodModalWindow() {
    $('#modal-window').css("display", "none");
}