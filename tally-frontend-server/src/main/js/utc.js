export function registerPrototype() {
	if (!Date.prototype.toInputString) {
	    Date.prototype.toInputString = function() {
	    	return `${this.getUTCFullYear()}-${pad(this.getUTCMonth() + 1)}-${pad(this.getUTCDate())}`
	    };
	}
	
	function pad(number) {
		if (number < 10) {
			return '0' + number;
		}
		return number;
	}
}

