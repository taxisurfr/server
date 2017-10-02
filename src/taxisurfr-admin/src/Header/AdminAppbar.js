import React, {Component, PropTypes} from 'react'
import Appbar from 'muicss/lib/react/appbar';
import GoogleLogin from '../Login/Google';
import GoogleLogout from '../Login/Google';
import Links from '../Links/Links';

class AdminAppbar extends Component {



    logout = (response) => {
        console.log(response);
    }

    render() {
        const clrText = {color: 'black'};
        const clr = {backgroundColor: '#66ff99'};

        const s1 = {verticalAlign: 'middle'};
        const s2 = {textAlign: 'right', color: 'black'};
        var style = {
            color: 'white',
            fontSize: 24
        }
        const {loggedIn,login,logout} = this.props;
        return (
            <Appbar style={clr}>
                <table width="100%">
                    <tbody>
                    <tr style={s1}>
                        <td><Links /></td>
                        <td className="mui--appbar-height mui--pull-right">
                            {!loggedIn && <GoogleLogin
                                clientId="468150866643-hvi544b3jp0bjptd444gsg365nd576j6.apps.googleusercontent.com"
                                buttonText="Login"
                                onSuccess={login}
                                onFailure={login}
                            />}
                            {/*{loggedIn && <GoogleLogout
                                clientId="468150866643-hvi544b3jp0bjptd444gsg365nd576j6.apps.googleusercontent.com"
                                buttonText="Logout"
                                onSuccess={logout}
                                onFailure={logout}
                            />}*/}
                        </td>

                    </tr>
                    </tbody>
                </table>
            </Appbar>
        )
    }
}

export default AdminAppbar
