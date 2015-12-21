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

        function init($window, $http) {
            $window.fbAsyncInit = function() {
                FB.init(defaultParams);

                // Now that we've initialized the JavaScript SDK, we call
                // FB.getLoginStatus().  This function gets the state of the
                // person visiting this page and can return one of three states to
                // the callback you provide.  They can be:
                //
                // 1. Logged into your app ('connected')
                // 2. Logged into Facebook, but not your app ('not_authorized')
                // 3. Not logged into Facebook and can't tell if they are logged into
                //    your app or not.
                //
                // These three cases are handled in the callback function.

                FB.getLoginStatus(function(res) {
                    if (res.status === 'connected' && res.authResponse) {
                        $http.form('accounts/fb', {
                            fbId: res.authResponse.userID,
                            fbToken: res.authResponse.accessToken
                        }).then(function (res) {
                            debugger
                        });
                    }
                });

            };

            // Load the SDK asynchronously
            (function(d, s, id) {
                var js, fjs = d.getElementsByTagName(s)[0];
                if (d.getElementById(id)) return;
                js = d.createElement(s); js.id = id;
                js.src = "//connect.facebook.net/en_US/sdk.js";
                fjs.parentNode.insertBefore(js, fjs);
            }(document, 'script', 'facebook-jssdk'));
        }

        function setParams(params) {
            angular.extend(defaultParams, params);
        }

        getService.$inject = ['$window', '$q', '$http'];

        function getService($window, $q, $http) {
            init($window, $http);
            return {
                getCredentials: function () {
                    return getCredentials($q);
                }
            };
        }

        function getCredentials($q) { debugger
            var deferred = $q.defer();
            FB.api('/me', {
                fields: 'last_name'
            }, function(res) { debugger
                if (!response || response.error) {
                    deferred.reject('Error occured');
                } else {
                    deferred.resolve(res);
                }
            });
            return deferred.promise;
        }
    }

})();
