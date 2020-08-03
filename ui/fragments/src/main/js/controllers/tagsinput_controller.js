import { Controller } from "stimulus"
import BulmaTagsInput from '@creativebulma/bulma-tagsinput';

export default class extends Controller {
    static get targets() {
        return ['tags'];
    }
    
    connect() {
        const lastTarget = this.tagsTargets[this.tagsTargets.length - 1];
        console.log("Is this thing on?", this.tagsTargets)
        //BulmaTagsInput.attach(this.tagsTarget);
        //new BulmaTagsInput(lastTarget);
    }
}