import { library, dom } from '@fortawesome/fontawesome-svg-core'
import { 
    faStickyNote, 
    faShare, 
    faChartLine, 
    faFlag, 
    faInfoCircle, 
    faDizzy, 
    faPizzaSlice,
    faEdit
} from '@fortawesome/free-solid-svg-icons'


import { 
    faGoogle 
} from '@fortawesome/free-brands-svg-icons'

export const initIcons = () => {
	library.add(
		// solid
	    faStickyNote, 
	    faShare, 
	    faChartLine, 
	    faFlag, 
	    faInfoCircle, 
	    faDizzy, 
	    faPizzaSlice,
	    
	    // regular
	    faEdit,
	    
	    // brand
	    faGoogle);
	
	dom.watch();
};