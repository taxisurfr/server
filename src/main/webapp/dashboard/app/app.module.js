(function () {
    'use strict';

    angular.module('app', [
        'app.core',
        'app.widgets',
        'app.admin',
        'app.route',
        'app.layout',
        'satellizer'
    ]).config(function ($authProvider,$httpProvider) {
        $httpProvider.defaults.useXDomain = true;


        $authProvider.facebook({
            clientId: 'Facebook App ID'
        });

        // Optional: For client-side use (Implicit Grant), set responseType to 'token' (default: 'code')
        $authProvider.facebook({
            clientId: 'Facebook App ID',
            responseType: 'token'
        });

        $authProvider.google({
            clientId: '468150866643-hvi544b3jp0bjptd444gsg365nd576j6.apps.googleusercontent.com',
            url: 'http://app.taxisurfr.com/rest/auth/google'

        });

        $authProvider.github({
            clientId: 'GitHub Client ID'
        });

        $authProvider.linkedin({
            clientId: 'LinkedIn Client ID'
        });

        $authProvider.instagram({
            clientId: 'Instagram Client ID'
        });

        $authProvider.yahoo({
            clientId: 'Yahoo Client ID / Consumer Key'
        });

        $authProvider.live({
            clientId: 'Microsoft Client ID'
        });

        $authProvider.twitch({
            clientId: 'Twitch Client ID'
        });

        $authProvider.bitbucket({
            clientId: 'Bitbucket Client ID'
        });

        $authProvider.spotify({
            clientId: 'Spotify Client ID'
        });

        // No additional setup required for Twitter

        $authProvider.oauth2({
            name: 'foursquare',
            url: '/auth/foursquare',
            clientId: 'Foursquare Client ID',
            redirectUri: window.location.origin,
            authorizationEndpoint: 'https://foursquare.com/oauth2/authenticate',
        });

    })
        // .run(function($http) {
        // $http.defaults.headers.post.Authorization = "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==";
        // $http.defaults.headers.common.Authorization = 'Basic YmVlcDpib29w1';
        // $http.defaults.headers.common.Authorization = 'Basic YmVlcDpib29w1';
        // $http.defaults.headers.common.Authorization = 'Bearer YmVlcDpib29w2';
        // $http.defaults.headers.common['Authorization'] = 'Bearer xxxxxxxxxxxxxx3';
        // $http.defaults.headers.common['Authorization'] = 'Basic xxxxxxxxxxxxxx4';
        // $http.defaults.headers.common['authorization'] = 'Basic xxxxxxxxxxxxxx4';
        // $http.defaults.headers.common['authorization'] = 'Bearer xxxxxxxxxxxxxx4';
        // $http.defaults.headers.common.Authorization = 'login YmVlcDpi' ;
        //or try this
        // $http.defaults.headers.common['Auth-Token'] = 'login YmVlcDpi';
    // })
    ;

})();
