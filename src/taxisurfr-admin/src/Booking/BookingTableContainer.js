"use strict";

var BookingTable = require('./BookingTable');
var TableStore = require('./../util/TableStore');
import React, {Component, PropTypes} from 'react'
import {connect} from 'react-redux'
import  {fetchBookingDataIfNeeded,cancelBooking} from './actions';


class BookingTableContainer extends React.Component {
    constructor() {
        super();
    };

    componentDidMount() {
        const {dispatch} = this.props;
        dispatch(fetchBookingDataIfNeeded())
    }


    render() {
        return (
            <div>
                <BookingTable
                    admin={this.props.admin}
                    bookingList={this.props.bookingList}
                    onCancel={this.props.onCancelBookingClick}/>
            </div>
        );
    }
}

BookingTableContainer.propTypes = {
    bookingList: PropTypes.object.isRequired,
    isFetching: PropTypes.bool.isRequired,
    dispatch: PropTypes.func.isRequired
}

function mapStateToProps(state) {
    const {bookingList} = state.bookingData;
    const {admin} = state.bookingData;
    const {
        isFetching
    } = bookingList || {
        isFetching: true,
        bookingList: {}
    }

    return {
        isFetching: false,
        bookingList: bookingList,
        admin:admin
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        onCancelBookingClick: (id) => {
            console.log('cancelling row'+id);
            dispatch(cancelBooking(id))
        },
        dispatch : dispatch
    }
}
export default connect(mapStateToProps,mapDispatchToProps)(BookingTableContainer)

