import { Controller } from "stimulus"

export default class extends Controller {
    static get targets() {
        return ['new_name', 'title', 'title_section', 'edit_section'];
    }

    connect() {

    }

    submitTitle() {
        const newTitle = this.new_nameTarget.value;
        if (newTitle === this.currentTitle) {
            this.finishEditing();
            return;
        }
        const actionUrl = this.data.get('url') + encodeURIComponent(newTitle);
        fetch(actionUrl)
            .then(response => {
                this.titleTarget.textContent = newTitle;
                this.new_nameTarget.value = newTitle;
                this.finishEditing();
            });
    }

    startEditing() {
        this.title_sectionTarget.classList.add('is-hidden');
        this.edit_sectionTarget.classList.remove('is-hidden');
        this.new_nameTarget.select();
    }

    finishEditing() {
        this.title_sectionTarget.classList.remove('is-hidden');
        this.edit_sectionTarget.classList.add('is-hidden');
    }

}