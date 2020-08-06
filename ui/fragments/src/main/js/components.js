import 'slim-js/directives/if.js'
import 'slim-js/directives/repeat.js'

import '@webcomponents/webcomponentsjs/webcomponents-loader.js'

import { InputNow } from './components/input-now';
import { EditIncrement } from './components/edit-increment.js';

import Turbolinks from 'turbolinks';
Turbolinks.start();

import { Application } from "stimulus"
import { definitionsFromContext } from "stimulus/webpack-helpers"

const application = Application.start()
const context = require.context("./controllers", true, /\.js$/)
application.load(definitionsFromContext(context))


import { confirmAction } from './components/confirmable.js';
import { postAjax, getAjax } from './components/ajax.js';

global.confirmAction = confirmAction;
global.postAjax = postAjax;
global.getAjax = getAjax;

import BulmaTagsInput from '@creativebulma/bulma-tagsinput';
BulmaTagsInput.attach();
