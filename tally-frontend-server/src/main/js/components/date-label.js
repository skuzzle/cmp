import { utcToLocal } from './utc.js'

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
