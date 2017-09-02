(function () {
    'use strict';

    angular
        .module('app.layout')
        .controller('ShellController', ShellController);

    ShellController.$inject = ['$rootScope', '$timeout', 'config', 'logger', '$auth','$window','dataservice'];
    /* @ngInject */
    function ShellController($rootScope, $timeout, config, logger, $auth, $window,dataservice) {
        var vm = this;
        vm.busyMessage = 'Please wait ...';
        vm.isBusy = true;
        vm.signedin = false;
        $rootScope.showSplash = true;
        vm.navline = {
            title: config.appTitle,
            text: 'Created by John Papa',
            link: 'http://twitter.com/john_papa'
        };

        $window.sessionStorage.accessToken = null;
        vm.authenticate = function (provider) {
            $auth.authenticate(provider).then(function(response) {
                vm.user = response.data.taxisurfrUser;
                $window.sessionStorage.accessToken = response.data.token;
                var routes = dataservice.getRoutes();

                vm.signedin = true;

            })
                .catch(function(response) {
                    console.log('problem signing in');
                });
        };

        activate();

        function activate() {
            logger.success(config.appTitle + ' loaded!', null);
            hideSplash();
        }

        function hideSplash() {
            //Force a 1 second delay so we can see the splash.
            $timeout(function () {
                $rootScope.showSplash = false;
            }, 1000);
        }
    }
})();
