
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.is-date-utc').forEach(dateField => {
        const dateUtc = new Date(dateField.textContent + "Z");
        dateField.textContent = dateUtc.toLocaleString();
    });
});