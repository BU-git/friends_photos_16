(function() {
    'use strict';

    angular
        .module('friends_photos')
        .config(config);

    config.$inject = ['$httpProvider'];

    function config($httpProvider) {
        $httpProvider.interceptors.push(['$q', '$injector', function ($q, $injector) {
            return {
                responseError: function (err) {
                    var $state = $injector.get('$state');
                    if (err.status === 401) $state.go('account.login');
                    return $q.reject(err);
                }
            };
        }]);
    }

})();
