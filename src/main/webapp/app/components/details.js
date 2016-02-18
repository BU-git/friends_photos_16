(function () {
    'use strict';

    angular
        .module('friends_photos')
        .directive('eventDetails', account);

    function account() {
        return {
            templateUrl: 'app/views/details.html',
            controllerAs: 'ctrl',
            controller: ['$scope', '$http', '$state', '$stateParams', 'eventsService', 'API_ENDPOINT',
                function ($scope, $http, $state, $stateParams, eventsService, API_ENDPOINT) {
                    var ctrl = this;
                    ctrl.join = function (event) {
                        $http.post(API_ENDPOINT + 'events/' + event.event_id + '/accounts', {}).then(function (res) {
                            $state.go('home.events');
                        });
                    };
                    eventsService.getById($stateParams.id).then(function (event) {
                        ctrl.event = event;
                    });
                }]
        };
    }

})();
