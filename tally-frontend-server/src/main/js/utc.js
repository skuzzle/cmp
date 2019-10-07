export function utcToLocal(dateString) {
    const dateUtc = new Date(dateString + "Z");
    return dateUtc.toLocaleDateString([], {
    	day: '2-digit',
    	month: '2-digit',
    	year: 'numeric'
    		
    });
}

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

