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
    
    event.preventDefault();
    return false;
}
