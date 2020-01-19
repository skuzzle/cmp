export const attachConfirmation = () => {
	const elems = document.getElementsByClassName('confirmable');
	const confirmIt = function (e) {
	    if (!confirm('Are you sure? This action can not be undone')) e.preventDefault();
	};
	for (let i = 0; i < elems.length; i++) {
	    elems[i].addEventListener('click', confirmIt, false);
	}
}