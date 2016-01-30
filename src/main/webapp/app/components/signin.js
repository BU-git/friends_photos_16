(function () {
    'use strict';

    angular
        .module('friends_photos')
        .directive('signin', signin);

    function signin() {
        return {
            scope: {},
            controller: 'AuthController',
            controllerAs: 'authCtrl',
            templateUrl: 'app/views/signin.html'
        };
    }

})();
