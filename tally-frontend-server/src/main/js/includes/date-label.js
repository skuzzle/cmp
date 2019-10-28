import { utcToLocal } from './utc.js'

export class DateLabel extends HTMLTimeElement  {
	constructor() {
		super();
		const serverDateUTC = this.textContent;
		const formatted = utcToLocal(serverDateUTC);
		this.textContent = formatted;
	}
}
customElements.define("date-label", DateLabel, { extends: 'time' });