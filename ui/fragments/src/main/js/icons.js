import { library, dom } from '@fortawesome/fontawesome-svg-core'
import { 
    faStickyNote, 
    faShare, 
    faChartLine, 
    faFlag, 
    faInfoCircle, 
    faDizzy, 
    faPizzaSlice,
    faEdit,
    faQuestionCircle,
    faShareAlt
} from '@fortawesome/free-solid-svg-icons'


import { 
    faGoogle 
} from '@fortawesome/free-brands-svg-icons'

library.add(
	// solid
    faStickyNote, 
    faShare, 
    faChartLine, 
    faFlag, 
    faInfoCircle, 
    faDizzy, 
    faPizzaSlice,
    faQuestionCircle,
    faShareAlt,
    
    // regular
    faEdit,
    
    // brand
    faGoogle);

dom.watch();
