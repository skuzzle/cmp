import { utcString } from './utc.js'
import { Slim } from 'slim-js';

Slim.tag(
'input-now',
`<input s:id="dateInput" class="input" type="date"></input>`,
class InputNow extends Slim {
	onRender() {
		const name = this.getAttribute('name');
		const required = this.getAttribute('required') || false;
		const autofocus = this.getAttribute('autofocus') || false;
		const today = utcString(new Date());
		
		this.dateInput.setAttribute('required', required);
		this.dateInput.setAttribute('autofocus', autofocus);
		this.dateInput.setAttribute('name', name);
		this.dateInput.setAttribute('value', today);
	}
});

