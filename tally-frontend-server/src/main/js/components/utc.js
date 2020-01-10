export function utcToLocal(dateString) {
    const dateUtc = new Date(dateString + "Z");
    return dateUtc.toLocaleDateString([], {
    	day: '2-digit',
    	month: '2-digit',
    	year: 'numeric'
    		
    });
}

export function utcToInputValue(dateString) {
    const dateUtc = new Date(dateString + "Z");
    return utcString(dateUtc);
}

export function utcString(date) {
	return `${date.getUTCFullYear()}-${pad(date.getUTCMonth() + 1)}-${pad(date.getUTCDate())}`
}

function pad(number) {
	if (number < 10) {
		return '0' + number;
	}
	return number;
}
