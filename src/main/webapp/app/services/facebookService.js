(function() {
    'use strict';

    angular
        .module('friends_photos')
        .provider('facebook', facebookProvider);

    facebookProvider.$inject = [];

    function facebookProvider() {
        var provider = this;
        angular.extend(provider, {
            $get: getService,
            setParams: setParams
        });

        var defaultParams = {
            appId      : '1525097901120630',
            cookie     : true,  // enable cookies to allow the server to access
                                // the session
            xfbml      : true,  // parse social plugins on this page
            version    : 'v2.4' // use version 2.2
        };

        function init($window, $q) {
            var deferred = $q.defer();
            $window.fbAsyncInit = function() {
                FB.init(defaultParams);
                deferred.resolve('initialized');
            };
            // Load the SDK asynchronously
            (function(d, s, id) {
                var js, fjs = d.getElementsByTagName(s)[0];
                if (d.getElementById(id)) return;
                js = d.createElement(s); js.id = id;
                js.src = "//connect.facebook.net/en_US/sdk.js";
                fjs.parentNode.insertBefore(js, fjs);
            }(document, 'script', 'facebook-jssdk'));

            return deferred.promise;
        }

        function setParams(params) {
            angular.extend(defaultParams, params);
        }

        getService.$inject = ['$window', '$q', '$http'];

        function getService($window, $q, $http) {
            var service = {
                login: login
            };
            return service;

            function login() {
                var deferred = $q.defer();
                init($window, $q).then(function () {
                    FB.getLoginStatus(function(res) {
                        if (res.status === 'connected' && res.authResponse) {
                            $http.form('accounts/fb', {
                                fbId: res.authResponse.userID,
                                fbToken: res.authResponse.accessToken
                            }).then(function (res) {
                                deferred.resolve(res);
                            });
                        }
                    });
                });
                return deferred.promise;
            }
        }
    }

})();
