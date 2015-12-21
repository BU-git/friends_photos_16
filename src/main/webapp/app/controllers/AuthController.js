(function() {
    'use strict';

    angular
        .module('friends_photos')
        .controller('AuthController', AuthController);

    AuthController.$inject = ['authService'];

    function AuthController(authService) {
        var ctrl = this;
        // export public properties and functions
        angular.extend(ctrl, {
            login: login
        });

        function login() {

        }
    }

})();
