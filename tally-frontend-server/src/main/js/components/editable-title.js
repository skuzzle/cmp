import { Slim } from 'slim-js';

Slim.tag(
'editable-title',
`<h1 s:if="!editing" class="title is-capitalized" bind>
	{{currentTitle}} 
	<button s:if="allowEdit" class="button" click="startEditing">
		Change name
	</button>
</h1>
<div s:if="editing" class="field is-grouped">
	<div class="control">
		<input s:id="titleInput" class="input" type="text" autofocus=true required="true" title="Enter the new title"></input>
	</div>
	<div class="control">
		<button class="button is-light" click="submitTitle" title="Change title"bind>
			Save
		</button>
	</div>
	<div class="control">
		<button class="button is-outlined is-danger" click="finishEditing" title="Keep current title">
			Cancel
		</button>
	</div>
</div>`,
class EditableTitle extends Slim {
	
	onBeforeCreated() {
		this.editing = false;
		this.currentTitle = this.innerText;
		this.action = this.getAttribute("changenameaction");
		this.allowEdit = this.getAttribute("allowedit") == "true";
	}
	
	submitTitle() {
		const newTitle = this.titleInput.value;
		if (newTitle === this.currentTitle) {
			this.finishEditing();
			return;
		}
		const actionUrl = this.action + encodeURIComponent(newTitle);
		fetch(actionUrl)
			.then(response => {
				this.currentTitle = newTitle;
				this.finishEditing();
			});
	}
	
	finishEditing() {
		this.editing = false;
	}
	
	startEditing() {
		this.editing = true;
		this.titleInput.value = this.currentTitle;
		this.titleInput.select();
	}
})
