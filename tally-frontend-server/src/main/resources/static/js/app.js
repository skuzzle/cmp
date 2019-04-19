
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.is-date-utc').forEach(dateField => {
        const dateUtc = new Date(dateField.textContent);
        dateField.textContent = dateUtc.toLocaleString();
    });
});