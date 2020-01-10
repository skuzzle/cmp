import { EditableTitle } from './components/editable-title';
import { DateLabel } from './components/date-label';
import { InputNow } from './components/input-now';
import { EditIncrement } from './components/edit-increment.js';

import bulmaTagsinput from '../../../node_modules/bulma-tagsinput/src/js/index.js';
bulmaTagsinput.attach();

const elems = document.getElementsByClassName('confirmable');
const confirmIt = function (e) {
    if (!confirm('Are you sure? This action can not be undone')) e.preventDefault();
};
for (let i = 0; i < elems.length; i++) {
    elems[i].addEventListener('click', confirmIt, false);
}