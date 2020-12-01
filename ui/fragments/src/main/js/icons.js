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
    faShareAlt,
    faTags,
    faCommentAlt,
    faListOl,
    faEnvelope,
    faUser
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
    faTags,
    faCommentAlt,
    faListOl,
    faEnvelope,
    faUser,
    
    // regular
    faEdit,
    
    // brand
    faGoogle);

dom.watch();
