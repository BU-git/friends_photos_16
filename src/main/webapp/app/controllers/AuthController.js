(function() {
    'use strict';

    angular
        .module('friends_photos')
        .controller('AuthController', AuthController);

    AuthController.$inject = ['$state', 'authService'];

    function AuthController($state, authService) {
        var ctrl = this;
        // export public properties and functions
        angular.extend(ctrl, {
            login: login
        });

        function login(data) {
            authService.login(data).then(function (user) {
                $state.go('home.events');
            });
        }
    }

})();
