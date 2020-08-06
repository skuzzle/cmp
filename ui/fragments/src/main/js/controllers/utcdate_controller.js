import { Controller } from "stimulus"

export default class extends Controller {

    connect() {
        let formattedDate = this.data.get('formatted-date'); 
        if (!formattedDate) {
            formattedDate = this.utcToLocal(this.element.textContent);
            this.data.set('formatted-date', formattedDate);
        }
        this.element.textContent = formattedDate;
    }

    utcToLocal(dateString) {
        const dateUtc = new Date(dateString + "Z");
        return dateUtc.toLocaleDateString([], {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'

        });
    }

}