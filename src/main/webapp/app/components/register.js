(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('register', login);

    function login() {
        return {
            scope: {},
            controller: 'AuthController',
            controllerAs: 'ctrl',
            bindToController: {
                searchTerm: '='
            },
            templateUrl: 'app/views/register.html'
        };
    }

})();
