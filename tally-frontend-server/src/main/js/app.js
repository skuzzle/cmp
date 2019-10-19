import { DateLabel } from './date-label';
import { InputNow } from './input-now';

customElements.define("date-label", DateLabel, { extends: 'time' });
customElements.define("input-now", InputNow, { extends: 'input' });
