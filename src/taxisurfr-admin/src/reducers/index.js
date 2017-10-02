import { combineReducers } from 'redux'
import auth from './auth'
import rootreducer from './rootReducer'

const taxisurfrApp = combineReducers({
    auth,
    rootreducer

})

export default taxisurfrApp
