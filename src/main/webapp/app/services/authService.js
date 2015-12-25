(function() {
    'use strict';

    angular
        .module('friends_photos')
        .service('authService', authService);

    authService.$inject = ['facebook'];

    function authService(facebook) {
        var service = this;
        // export public properties and functions
        angular.extend(service, {
            login: login
        });

        function login(data) {
            if (angular.isString(data)) {
                if (data === 'facebook') return facebook.login();
            }
        }
    }

})();
