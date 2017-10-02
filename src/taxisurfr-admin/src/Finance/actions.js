import fetch from 'isomorphic-fetch'
import util from 'util';
import {getMethod,getOptions,getUrl} from '../util/network';

var TableStore = require('./../util/TableStore');

export const REQUEST_FINANCEDATA = 'REQUEST_FINANCEDATA'
export const RECEIVE_FINANCEDATA = 'RECEIVE_FINANCEDATA'
export const TRANSFER_ACTIVE = 'TRANSFER_ACTIVE'
export const TRANSFER_NAME_CHANGE = 'TRANSFER_NAME_CHANGE'
export const TRANSFER_AMOUNT_CHANGE = 'TRANSFER_AMOUNT_CHANGE'

function requestFinanceData() {
    return {
        type: REQUEST_FINANCEDATA
    }
}

function receiveFinanceData(json) {
    return {
        type: RECEIVE_FINANCEDATA,
        transferList: new TableStore(json.transferList),
        paymentList: new TableStore(json.paymentList),
        summaryList: new TableStore(json.summaryList),
        admin: json.admin
    }
}


export function transferActive(active) {
    return {
        type: TRANSFER_ACTIVE,
        transferActive: active
    }
}
export function setTransferName(name) {
    return {
        type: TRANSFER_NAME_CHANGE,
        transferName: name
    }
}
export function setTransferAmount(transferAmount) {
    return {
        type: TRANSFER_AMOUNT_CHANGE,
        transferAmount: transferAmount
    }
}


function fetchFinanceData() {
    return dispatch => {
        dispatch(requestFinanceData())
        fetch(getUrl('finance'), getOptions('POST'))
            .then((response) => response.json())
            .then((responseJson) => dispatch(receiveFinanceData(responseJson)))
            .catch((error) => {
                console.error(error);
            });
    }
}

function shouldFetchFinanceData(state) {
    const financeData = state.financeData
    return !financeData.isFetching;
}

export function fetchFinanceDataIfNeeded() {
    return (dispatch, getState) => {
        if (shouldFetchFinanceData(getState())) {
            return dispatch(fetchFinanceData())
        }
    }
}





export function cancelBooking(bookingId) {
    console.log("canceling: "+bookingId);
    return (dispatch, getState) => {
        return dispatch(cancelBookingOnServer(bookingId))
    }
}

export function saveTransferOnServer(cents,description) {
    util.inspect(cents, { showHidden: true, depth: null });
    util.inspect(description, { showHidden: true, depth: null });
    var body = JSON.stringify({
        cents: cents,
        description: description

    });
    return dispatch => {
        fetch(getUrl('createTransfer'), getMethod('POST',body))
            .then((response) => response.json())
            .then((responseJson) => dispatch(receiveFinanceData(responseJson)))
            .catch((error) => {
                console.error(error);
            });
    }
}

function cancelBookingOnServer(id) {
    util.inspect(id, { showHidden: true, depth: null });
    var bi = id;
    var body = JSON.stringify({
        bookingId: id
    });

    return dispatch => {
        fetch(getUrl('cancelBooking'), getMethod('POST',body))
            .then((response) => response.json())
            .then((responseJson) => dispatch(receiveFinanceData(responseJson)))
            .catch((error) => {
                console.error(error);
            });
    }

}



export const toggleTodo = (id) => {
    return {
        type: 'TOGGLE_TODO',
        id
    }
}
