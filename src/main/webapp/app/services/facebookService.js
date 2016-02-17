(function () {
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
            appId: '1525097901120630',
            cookie: true,  // enable cookies to allow the server to access
                           // the session
            xfbml: true,  // parse social plugins on this page
            version: 'v2.4' // use version 2.2
        };

        function init($window, $q) {
            return $q.when($window.FB || function () {
                    var deferred = $q.defer();
                    $window.fbAsyncInit = function () {
                        FB.init(defaultParams);
                        deferred.resolve(FB);
                    };
                    // Load the SDK asynchronously
                    (function (d, s, id) {
                        var js, fjs = d.getElementsByTagName(s)[0];
                        if (d.getElementById(id)) return;
                        js = d.createElement(s);
                        js.id = id;
                        js.src = "//connect.facebook.net/en_US/sdk.js";
                        fjs.parentNode.insertBefore(js, fjs);
                    }(document, 'script', 'facebook-jssdk'));

                    return deferred.promise;
                }());

        }

        function setParams(params) {
            angular.extend(defaultParams, params);
        }

        getService.$inject = ['$window', '$q', '$http', 'API_ENDPOINT'];

        function getService($window, $q, $http, API_ENDPOINT) {
            var service = {
                login: login
            };
            return service;

            function login() {
                return init($window, $q).then(function (FB) {
                    var deferred = $q.defer();
                    FB.login(function (res) {
                        var authResponse = res.authResponse;
                        if (authResponse) {
                            var account = {
                                "token": authResponse.accessToken,
                                "social_id": authResponse.userID,
                            };
                            console.log('Welcome!  Fetching your information.... ');
                            FB.api('/me', {locale: 'en_US', fields: 'email,name,first_name,last_name'}, function (res) {
                                angular.extend(account, {
                                    "email": res.email,
                                    "username": res.name,
                                    "first_name": res.first_name,
                                    "last_name": res.last_name,
                                    "image_url": ""
                                });
                                console.log('Good to see you, ' + res.name + '.');
                                $http.post(API_ENDPOINT + 'auth/fb', account).then(function (res) {
                                    deferred.resolve(res);
                                });
                            });
                        } else {
                            console.log('User cancelled login or did not fully authorize.');
                        }
                    }, {scope: 'email', return_scopes: true});
                    return deferred.promise;
                });
            }
        }
    }

})();
