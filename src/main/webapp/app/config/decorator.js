(function () {
    'use strict';

    angular
        .module('friends_photos')
        .config(config);

    config.$inject = ['$provide'];

    function config($provide) {
        $provide.decorator('$http', ['$delegate', function ($delegate) {
            $delegate.form = form;

            return $delegate;

            function form(url, data, multipart) {
                return $delegate({
                    url: url,
                    method: 'POST',
                    data: data,
                    headers: {
                        'Content-Type': multipart ? undefined : 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (data) {
                        if (multipart) {
                            var formData = new FormData();
                            angular.forEach(data, function (val, name) {
                                if (val instanceof File) {
                                    formData.append(name, val);
                                } else if (val instanceof FileList) {
                                    formData.append(name, val);
                                } else if (angular.isDefined(val)) {
                                    val = angular.isObject(val) ? JSON.stringify(val) : val;
                                    formData.append(name, val);
                                }
                            });
                            return formData;
                        } else {
                            var params = [];
                            angular.forEach(data, function (val, name) {
                                if (angular.isDefined(val)) {
                                    val = angular.isObject(val) ? encodeURI(JSON.stringify(val)) : encodeURIComponent(val);
                                    params.push(name + '=' + val);
                                }
                            });
                            return params.join('&');
                        }
                    }
                });
            }
        }]);

        $provide.decorator('$state', ['$delegate', function ($delegate) {
            $delegate.history = [];
            $delegate.goToPrev = function () {debugger
                if ($delegate.history.length) {

                }
            };
            return $delegate;
        }]);
    }

})();
