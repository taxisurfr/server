import fetch from 'isomorphic-fetch'
import util from 'util';
import {getMethod,getOptions,getUrl} from '../util/network';


export const LOGINLOGOUT = 'LOGINLOGOUT'


export function loginLogoutWithToken(loggedIn) {
    return {
        type: LOGINLOGOUT,
        loggedIn: loggedIn
    }
}

