import 'slim-js/directives/if.js'
import 'slim-js/directives/repeat.js'

import '@webcomponents/webcomponentsjs/webcomponents-loader.js'

import { EditableTitle } from './components/editable-title';
import { DateLabel } from './components/date-label';
import { InputNow } from './components/input-now';
import { EditIncrement } from './components/edit-increment.js';
import Turbolinks from 'turbolinks';
Turbolinks.start();

import bulmaTagsinput from 'bulma-tagsinput';
bulmaTagsinput.attach();

import { confirmAction } from './components/confirmable.js';

import { postAjax, getAjax } from './components/ajax.js';

global.confirmAction = confirmAction;
global.postAjax = postAjax;
global.getAjax = getAjax;