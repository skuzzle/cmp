export function getAjax(event) {
    const url = event.currentTarget.getAttribute('href');
    fetch(url)
    .then(response => response.text())
    .then(body => {
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
        eval(body)
    });
    
    event.preventDefault();
    return false;
}
