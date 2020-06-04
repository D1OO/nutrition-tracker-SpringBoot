AOS.init({
    easing: 'ease',
    duration: 1000,
    once: true
});

$(document).ready(function () {
    setContentContainerTo('/food-diary');
    $('.recordTab').css("background", "#e2dbff");
    window.onclick = function (event) {
        if (event.target === $('#modal-window')) {
            $('modal-window').style.display = "none";
        }
    };
});

function setContentContainerTo(controllerEndpoint) {
    $.get(controllerEndpoint, function (data) {
        document.getElementById('content-container').innerHTML = data;
    });
}

function setModalContainerTo(name) {
    $('#modalbody > *').css("display", "none");
    document.getElementById(name).style.display = "block";
}

function openAddFoodModalWindow(recordtab) {
    event.preventDefault();
    var data = $(recordtab).serialize();
    $.get('/adding-entries-modal-window', data, function (data) {
        document.getElementById('modal-window').innerHTML = data;
        $("#modal-window").css("display", "block");
    });
}

function addedNewEntry(foodDTO, foodName) {
    // event.preventDefault();
    var data = '&foodDTOJSON=' + foodDTO + '&foodName=' + foodName + '&newEntriesDTOJSON=' + $('#new-entries-list').val();
    $.post('/added-entry', data, function (response) {
        $('#new-entries-container').html(response);
    });
}

function removedEntry(index) {
    $(this).closest('tr').remove();
    data = 'index=' + index + '&newEntriesDTOJSON=' + $('#new-entries-list').val();
    $.post('/removed-entry', data, function (response) {
        $('#new-entries-container').html(response);
    });
}

function saveCreatedFood() {
    event.preventDefault();
    var data = $('#createfoodform').serialize();
    $.post('/save-new-food', data, replacePageWith);
}

function saveNewEntries() {
    event.preventDefault();
    var data = $('#new-entries-form').serialize();
    $.post('/save-new-entries', data, replacePageWith);
}

function replacePageWith(html) {
    var newDoc = document.open("text/html", "replace");
    newDoc.write(html);
    newDoc.close();
}

function tabClick(tab) {
    $('.recordTab').css("background", "#e2dbff");
    $(event.target).css("background", "linear-gradient(338deg, rgba(213, 95, 147, 0.62) 10%, rgba(233, 232, 148, 0.73) 100%)");
    $('.record-tabs').css("display", "none");
    $(tab).css("display", "block");
}

function closeAddFoodModalWindow() {
    $('#modal-window').css("display", "none");
}