export const attachPostAjax = () => {
    const elems = document.getElementsByClassName('ajax-post');

    for (let i = 0; i < elems.length; i++) {
        elems[i].addEventListener('submit', postAjax, false);
    }
}

export function getAjax(event) {
    const url = event.currentTarget.getAttribute('href');
    fetch(url, {
        method: 'get'
    })
    .then(response => response.text())
    .then(body => {
        console.log('Ceived response');
        console.log(body);
        eval(body)
    });
    console.log('We submit link asynchronously (AJAX)');
    event.preventDefault();
    return false;
}

export function postAjax(event) {
    const form = event.currentTarget;
    fetch(form.action, {
        method: 'post',
        body: new FormData(form)
    })
    .then(response => response.text())
    .then(body => {
        console.log('Ceived response');
        console.log(body);
        eval(body)
    });
    
    
    /*const xhr = new XMLHttpRequest();
    xhr.open(form.method, form.getAttribute("action"));
    xhr.send(new FormData(form));*/

    console.log('We submit form asynchronously (AJAX)');
    event.preventDefault();
    return false;
}
