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

import { attachConfirmation } from './components/confirmable.js';
attachConfirmation();

import { postAjax, getAjax } from './components/postajax.js';

global.postAjax = postAjax;
global.getAjax = getAjax;