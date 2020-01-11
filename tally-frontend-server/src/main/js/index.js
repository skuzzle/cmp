// Webcomponents
import 'slim-js/directives/if.js'
import 'slim-js/directives/repeat.js'

import '@webcomponents/webcomponentsjs'

import { EditableTitle } from './components/editable-title';
import { DateLabel } from './components/date-label';
import { InputNow } from './components/input-now';
import { EditIncrement } from './components/edit-increment.js';

// icons
import { initIcons } from './icons.js';
initIcons();

import bulmaTagsinput from 'bulma-tagsinput';
bulmaTagsinput.attach();

// charts
import Chart from 'chart.js'

// our own css

import '../css/main.scss';

// confirm
import './confirmable.js'