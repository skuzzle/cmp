import { utcToInputValue } from './utc.js'
import bulmaTagsinput from 'bulma-tagsinput';
import { Slim } from 'slim-js';

Slim.tag(
'edit-increment',
`
<div s:if="!editing">
	<div>
		<em s:if="empty" title="This increment has no tags and no description">No Description</em>
		<span s:if="!empty" bind>{{description}}</span>
			<a s:if="allowEdit" class="is-small edit-increment" click="startEdit">
			<span class="icon">
				<i class="fas fa-edit"></i>
			</span>
		</a>
	</div>
	<div s:if="tagsAvailable" class="tags">
		<span s:repeat="tagList as tag" bind class="tag">{{tag}}</span>
	</div>
</div>
<div s:if="editing">
    <div class="field">
        <div class="control is-expanded">
            <input s:id="dateInput" bind:value="incrementDateUTC" name="incrementDateUTC" required="true" autofocus="true"  class="input" type="date"  title="The increment date">
        </div>
    </div>
    <div class="field">
        <div class="control is-expanded">
            <input s:id="descriptionInput" bind:value="description" class="input" name="description" type="text" placeholder="Description" title="Increment description (optional)" autofocus="true" >
        </div>
    </div>

    <div class="field">
        <div class="control is-expanded">
            <input s:id="tagsInput" bind:value="tags" list="tagnames" class="input" name="tags" type="tags" placeholder="Add some tags" title="Comma separated tags, leading and trailing spaces will be trimmed (optional)" autofocus="true"  >
        </div>
    </div>
    
    <div class="field is-grouped">
        <div class="control">
        	<button class="button is-light" click="submitIncrement">Update</button>
        </div>
        <div class="control">
        	<button class="button is-outlined is-danger"  click="stopEdit">Cancel</button>
        </div>
    </div>
</div>
`,
class EditIncrement extends Slim {
	
	onBeforeCreated() {
		this.editing = false;
		this.incrementDateUTC = utcToInputValue(this.getAttribute("incrementDateUTC"));
		this.description = this.getAttribute("description") || "";
		
		this.tags = this.getAttribute("tags") || "";
		this.tagList = this.tags.split(",");
		this.tagsAvailable = this.tags !== "";
		this.empty = this.tags === "" && this.description === "";
		this.allowEdit = this.getAttribute("allowEdit") == "true";
		this.updateUrl = this.getAttribute("updateUrl");
	}
	
	startEdit() {
		this.once || new bulmaTagsinput(this.tagsInput, {});
		this.once = true;
		this.editing = true;
	}
	
	submitIncrement() {
		const newIncrementDateUTC = encodeURIComponent(this.dateInput.value);
		const newDescription = encodeURIComponent(this.descriptionInput.value);
		const newTags = encodeURIComponent(this.tagsInput.value);
		
		const actionUrl = this.updateUrl + `&incrementDateUTC=${newIncrementDateUTC}&description=${newDescription}&tags=${newTags}`;
		fetch(actionUrl)
			.then(response => {
				window.location.reload();
			})
	}
	
	stopEdit() {
		this.editing = false;
	}
})
