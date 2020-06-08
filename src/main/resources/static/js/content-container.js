$(document).ready(function () {
    const section = document.getElementById('sectionToFetchWithAJAX').value;
    setContentContainerTo(section ? section : 'food-diary');
    window.onpopstate = function (e) {
        if (e.state != null)
            setContentContainerTo(e.state.endpoint);
        else
            window.location.href = document.location;
    };
});

function loadFromServerIntoContentContainer(...endpoint) {
    const state = {"endpoint": endpoint.join('')};
    window.history.pushState(state, "Dreamfit", endpoint);
    setContentContainerTo(endpoint.join(''));
}
function setContentContainerTo(controllerEndpoint) {
    $.ajax({
        type: "GET",
        url: controllerEndpoint,
        data: {AJAXrequest: controllerEndpoint},
        success: function(data) {
            document.getElementById('content-container').innerHTML = data;
        },
        error: function(data){
            replacePageWith(data.responseText);
        }
    });
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

