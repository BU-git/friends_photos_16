(function () {
    'use strict';

    angular
        .module('friends_photos')
        .directive('search', account);

    function account() {
        return {
            templateUrl: 'app/views/search.html',
            controllerAs: 'ctrl',
            controller: ['$scope', '$http', 'API_ENDPOINT', function ($scope, $http, API_ENDPOINT) {
                var ctrl = this;
                angular.extend(ctrl, {
                    search: function (search) {
                        var req = Object.keys(search).map(function (item) {
                            return encodeURIComponent(item) + '=' + encodeURIComponent(search[item]);
                        }).join('&');
                        $http.get(API_ENDPOINT + 'events?' + req).then(function (res) {
                            ctrl.eventsList = res.data.events;
                        })
                    }
                });
                $scope.search = {
                    name: ''
                };
            }]
        };
    }

})();
