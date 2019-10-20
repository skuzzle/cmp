import { DateLabel } from './date-label';
import { InputNow } from './input-now';
import bulmaTagsinput from '../../../node_modules/bulma-tagsinput/src/js/index.js';

customElements.define("date-label", DateLabel, { extends: 'time' });
customElements.define("input-now", InputNow, { extends: 'input' });
bulmaTagsinput.attach();
