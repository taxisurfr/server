import fetch from 'isomorphic-fetch'
import util from 'util';
var TableStore = require('./../util/TableStore');
import {getMethod,getOptions,getUrl} from '../util/network';

export const REQUEST_BOOKINGDATA = 'REQUEST_BOOKINGDATA'
export const RECEIVE_BOOKINGDATA = 'RECEIVE_BOOKINGDATA'

function requestBookingData() {
    return {
        type: REQUEST_BOOKINGDATA
    }
}

function receiveBookingData(json) {
    return {
        type: RECEIVE_BOOKINGDATA,
        bookingList: new TableStore(json.bookingList),
        admin: json.admin
    }
}


/*
function getUrl(type) {
    var url = 'https://app.taxisurfr.com/';
    // url = 'http://localhost:3001/';
    url = 'http://localhost:8080/taxisurfr-1.0/';
    url = url + 'rest/admin/'+type;
    return url;
}
function getOptions(method) {
    var tokenId = localStorage.getItem("tokenId");
    var getBookingData = {
        method: method,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + tokenId
        },
        // mode: 'no-cors',
        cache: 'default'
    };
    return getBookingData;
}
*/



function fetchBookingData() {
    return dispatch => {
        dispatch(requestBookingData())
        fetch(getUrl('booking'), getOptions('POST'))
            .then((response) => response.json())
            .then((responseJson) => dispatch(receiveBookingData(responseJson)))
            .catch((error) => {
                console.error(error);
            });

    }
}

function shouldFetchBookingData(state) {
    const bookingData = state.bookingData
    return !bookingData.isFetching;
}

export function fetchBookingDataIfNeeded() {
    return (dispatch, getState) => {
        if (shouldFetchBookingData(getState())) {
            return dispatch(fetchBookingData())
        }
    }
}

export function cancelBooking(bookingId) {
    console.log("canceling: "+bookingId);
    return (dispatch, getState) => {
        return dispatch(cancelBookingOnServer(bookingId))
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
            .then((responseJson) => dispatch(receiveBookingData(responseJson)))
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
