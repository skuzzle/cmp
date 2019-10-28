import { utcString } from './utc.js'

export class InputNow extends HTMLInputElement {
	constructor() {
		super();
		this.value = utcString(new Date());
	}
}
customElements.define("input-now", InputNow, { extends: 'input' });
