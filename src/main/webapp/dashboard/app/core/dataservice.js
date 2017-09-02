(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('dataservice', dataservice);

    dataservice.$inject = ['$http', '$q', 'exception', 'logger', '$window'];
    /* @ngInject */
    function dataservice($http, $q, exception, logger, $window) {
        var service = {
            getRoutes: getRoutes,
            getMessageCount: getMessageCount
        };

        return service;

        function getMessageCount() {
            return $q.when(72);
        }

        function getRoutes() {
            if ($window.sessionStorage.accessToken != null) {
                var token = $window.sessionStorage.accessToken;
                // $http.defaults.headers.common.Authorization = 'Basic YmVlcDpib29w';

                // var headers = {Authorization: 'Bearer ' + $window.sessionStorage.accessToken};
                // var headers = {Authorization: 'Bearer mytoken:' + $window.sessionStorage.accessToken};
                // return $http.get(getBase($window) + 'rest/admin/routes', headers)
                //     .then(success)
                //     .catch(fail);
                return $http({
                    method: 'GET',
                    url: getBase($window) + 'rest/admin/routes',
                    headers: {
                        'Authorization': 'Bearer ' + token
                    }
                }).then(success)
                    .catch(fail);
            } else {
                return {data: ''};
            }
            function success(response) {
                return response.data;
            }

            function fail(e) {
                return exception.catcher('XHR Failed for getRoutes')(e);
            }

        }
    }
})();

var getBase = function (window) {
    var browserSync = false;
    if (browserSync) {
        return '//localhost:3000/';
    }
    console.log(window.location.hostname);
    console.log(window.location.port);
    if (window.location.hostname === 'localhost') {
        return 'http://localhost:8080/taxisurfr-1.0/';
    } else {

        return 'https://app.taxisurfr.com/';
    }
};
