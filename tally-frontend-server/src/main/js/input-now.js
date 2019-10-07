export class InputNow extends HTMLInputElement {
	constructor() {
		super();
		this.value = new Date().toInputString();
	}
}