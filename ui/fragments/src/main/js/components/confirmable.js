export function confirmAction(event, action) {
    if (!confirm('Are you sure? This action can not be undone')) {
        event.preventDefault();
        return false;
    } else {
        return action(event);
    }
}