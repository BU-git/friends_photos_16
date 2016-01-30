(function() {
    'use strict';

    angular
        .module('friends_photos')
        .service('authService', authService);

    authService.$inject = ['$q', '$http', 'facebook', 'API_ENDPOINT'];

    function authService($q, $http, facebook, API_ENDPOINT) {
        var service = this;
        // export public properties and functions
        angular.extend(service, {
            login: login
        });

        function login(data) { debugger
            return $q.when(data).then(function (data) {
                if (angular.isString(data)) {
                    if (data === 'facebook')
                        return facebook.login();
                } else {
                    return $http.post(API_ENDPOINT + 'auth', data);
                }
            }).then(function () { debugger
                return $http.get(API_ENDPOINT + 'accounts/self').then(function (res) {
                    return res.data;
                });
            });
        }
    }

})();
