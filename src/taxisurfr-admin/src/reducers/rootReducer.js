import { combineReducers } from 'redux'
var TableStore = require('./../util/TableStore');
import {
    REQUEST_FINANCEDATA, RECEIVE_FINANCEDATA, TRANSFER_ACTIVE,TRANSFER_NAME_CHANGE,TRANSFER_AMOUNT_CHANGE
} from '../Finance/actions';
import {
    LOGINLOGOUT
} from '../Header/actions';

import {
    REQUEST_BOOKINGDATA, RECEIVE_BOOKINGDATA
} from '../Booking/actions'

function financeData(state = {
    loggedIn:false,
    transferName: '',
    isFetching: false,
    paymentList: new TableStore(""),
    transferList: new TableStore(""),
    summaryList: new TableStore("")
}, action) {
    switch (action.type) {
        case REQUEST_FINANCEDATA:
            return Object.assign({}, state, {
                isFetching: true
            })
        case LOGINLOGOUT:
            return Object.assign({}, state, {
                loggedIn: action.loggedIn,
            })
        case RECEIVE_FINANCEDATA:
            return Object.assign({}, state, {
                isFetching: false,
                paymentList: action.paymentList,
                transferList: action.transferList,
                summaryList: action.summaryList,
                admin: action.admin
            })
        case TRANSFER_ACTIVE:
            return Object.assign({}, state, {
                transferActive: action.transferActive
            })
        case TRANSFER_NAME_CHANGE:
            return Object.assign({}, state, {
                transferName: action.transferName
            })
        case TRANSFER_AMOUNT_CHANGE:
            return Object.assign({}, state, {
                transferAmount: action.transferAmount
            })
        default:
            return state
    }
}

function bookingData(state = {
    isFetching: false,
    bookingList: new TableStore("")
}, action) {
    switch (action.type) {
        case REQUEST_BOOKINGDATA:
            return Object.assign({}, state, {
                isFetching: true
            })
        case RECEIVE_BOOKINGDATA:
            return Object.assign({}, state, {
                isFetching: false,
                bookingList: action.bookingList,
                admin:action.admin
            })
        default:
            return state
    }
}


const rootReducer = combineReducers({
    financeData,
    bookingData
})

export default rootReducer
