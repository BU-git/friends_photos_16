(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('account', account);

    function account() {
        return {
            templateUrl: 'app/views/account.html'
        };
    }

})();
