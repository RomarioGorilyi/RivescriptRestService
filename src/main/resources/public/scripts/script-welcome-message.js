/**
 * Created by RomanH on 07.04.2017.
 */
function getCookie(name) {
    var matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

function createWelcomeMessage() {
    var welcomeMessageContainer = document.querySelector('.messages.msg_receive');
    var cookie = getCookie("lang");
    switch (cookie) {
        case 'eng':
            welcomeMessageContainer.innerHTML
                += "<p>Hello. I'm your personal helper. I'm ready to answer your questions.</p>";
            break;
        case 'ukr':
            welcomeMessageContainer.innerHTML
                += "<p>Привіт. Я Ваш персональний помічник. Я готовий відповідати на Ваші питання.</p>";
            break;
        case 'rus':
            welcomeMessageContainer.innerHTML
                += "<p>Привет. Я Ваш персональный помошник. Я готов отвечать на Ваши вопросы.</p>";
            break;
        default:
            welcomeMessageContainer.innerHTML
                += "<p>Hello. I'm your personal helper. I'm ready to answer your questions.</p>";
            break;
    }
}
