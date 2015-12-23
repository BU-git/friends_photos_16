(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('eventsCreateEdit', eventsCreateEdit);

    function eventsCreateEdit() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/events-create-edit.html',
            controller: 'EventsController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                action: '@',
                eventId: '@'
            }
        };
    }

})();
