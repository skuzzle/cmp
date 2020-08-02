import { Controller } from "stimulus"
import BulmaTagsInput from '@creativebulma/bulma-tagsinput';

export default class extends Controller {
    static get targets() {
        return ['tags'];
    }
    
    connect() {
        new BulmaTagsInput(this.tagsTarget);
    }
}