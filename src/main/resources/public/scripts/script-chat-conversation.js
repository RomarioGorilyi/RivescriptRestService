/**
 * Created by RomanH on 07.04.2017.
 */
function makeRequest() {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/chatbot/', true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    var message = document.getElementById('btn-input').value;
    var body = JSON.stringify({ message: message });
    var inputField = document.getElementById('btn-input');
    inputField.value = '';
    inputField.focus();
    xhr.send(body);

    xhr.onreadystatechange = function() {
        if (xhr.readyState !== 4) {
            return;
        }
        if (xhr.status !== 200) {
            alert(xhr.status + ': ' + xhr.statusText);
        } else {
            var messageContainer = document.querySelector('.panel-body.msg_container_base');

            messageContainer.innerHTML += "<div class=\"row msg_container human_message\">"
                + "<div class=\"col-md-10 col-xs-10\">"
                + "<div class=\"messages msg_receive\">"
                + "<p>" + message + "</p>"
                + "</div></div>"
                + "<div class=\"col-md-2 col-xs-2 avatar\">"
                + "<img src=\"https://cdn1.iconfinder.com/data/icons/unique-round-blue/93/user-512.png\" class=\"img-responsive\">"
                + "</div></div>";

            messageContainer.innerHTML += "<div class=\"row msg_container chatbot_message\">"
                + "<div class=\"col-md-2 col-xs-2 avatar\">"
                + "<img src=\"https://d13yacurqjgara.cloudfront.net/users/279657/screenshots/2701628/chatbot.png\" class=\"img-responsive\">"
                + "</div>"
                + "<div class=\"col-md-10 col-xs-10\">"
                + "<div class=\"messages msg_receive\">"
                + "<p>" + JSON.parse(xhr.responseText)['message'] + "</p>"
                + "</div> </div> </div>";

            messageContainer.scrollTop = messageContainer.scrollHeight;
        }
    }
}

function sendRequestOnMessage(e) {
    if (e.keyCode == 13) {
        makeRequest();
    }
}