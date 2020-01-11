import { utcToLocal } from './utc.js'
import { Slim } from 'slim-js';

Slim.tag(
'date-label',
'<time bind>{{date}}</time>',
class DateLabel extends Slim {
	onBeforeCreated() {
		const serverDateUTC = this.textContent;
		const formatted = utcToLocal(serverDateUTC);
		this.date = formatted;
	}
});
