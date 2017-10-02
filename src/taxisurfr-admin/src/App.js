import React from 'react';
import {Router, Route, Link, hashHistory} from 'react-router';
import AdminApp from './AdminApp';

import GoogleLogin from './Login/Google';
import './App.css';
import {render} from 'react-dom'
import {Provider} from 'react-redux'
import {createStore} from 'redux'
import taxisurfrApp from './reducers'
import configureStore from './util/configureStore'
import {IntlProvider} from 'react-intl';
import 'muicss/dist/css/mui.min.css'


const responseGoogle = (response) => {
    console.log(response);
    localStorage.setItem("tokenId", response.tokenId);
    // console.log(localStorage.getItem("tokenId"));
}

const store = configureStore()

const App = () => {
    return (
        <IntlProvider locale="en">
            <Provider store={store}>
            <div>
                <Router history={ hashHistory }>
                    <Route path="/" component={AdminApp}></Route>
                    <Route path="/finance" component={AdminApp}></Route>
                    <Route path="/booking" component={AdminApp}></Route>
                </Router>
            </div>
        </Provider>
        </IntlProvider>
    )
};

export default App;
