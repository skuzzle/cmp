if (!Date.prototype.toInputString) {
  (function() {

    function pad(number) {
      if (number < 10) {
        return '0' + number;
      }
      return number;
    }

    Date.prototype.toInputString = function() {
      return this.getUTCFullYear() +
        '-' + pad(this.getUTCMonth() + 1) +
        '-' + pad(this.getUTCDate());
    };

  }());
}

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.is-date-utc').forEach(dateField => {
        const dateUtc = new Date(dateField.textContent + "Z");
        dateField.textContent = dateUtc.toLocaleDateString();
    });

    const nowString = new Date().toInputString();
    document.querySelectorAll('input[type=date]').forEach(dateInput => {
        dateInput.value = nowString;
    });
});