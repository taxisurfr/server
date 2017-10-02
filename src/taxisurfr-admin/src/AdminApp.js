import React, {Component, PropTypes} from 'react'
import {connect} from 'react-redux'
import AdminAppbar from './Header/AdminAppbar';

import Home from './Home/Home';
import About from './About/About';
import Contact from './Contact/Contact';
import FinanceTableContainer from './Finance/Finance';
import Booking from './Booking/Booking';
import logo from './logo.svg';
import {
    loginLogoutWithToken,
    LOGIN
} from './Header/actions'

/*import {getRouteDescription, getFormatedPrice,getPickup}
 from '../util/formatter';*/

class AdminApp extends Component {
    constructor(props) {
        super(props);
    }

    login = (response) => {
        localStorage.setItem("tokenId", response.tokenId);
        const {dispatch} = this.props;
        dispatch(loginLogoutWithToken(true));
    }
    logout = (response) => {
        localStorage.setItem("tokenId", null);
        const {dispatch} = this.props;
        dispatch(loginLogoutWithToken(false));
    }

    render() {

        const {src} = this.props;
        const {match} = this.props;

        const {pathname} = this.props.location;
        const {loggedIn} = this.props;
        const finance = (pathname==='' || pathname==='finance'|| pathname==='/finance') && loggedIn;
        const booking = (pathname==='booking'|| pathname==='/booking') && loggedIn;

        return (
            <div>
                <AdminAppbar loggedIn={loggedIn} login={this.login} logout={this.logout}/>
                {finance && <FinanceTableContainer/>}
                {booking && <Booking/>}

            </div>
        )
    }
}


AdminApp.propTypes = {
    // onSubmit: PropTypes.func.isRequired,
    dispatch: PropTypes.func.isRequired
};

function mapStateToProps(state) {
    const loggedIn = state.financeData.loggedIn;
    return {
        loggedIn
    }
}


const mapDispatchToProps = (dispatch) => {
    return {
        dispatch: dispatch
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(AdminApp)
