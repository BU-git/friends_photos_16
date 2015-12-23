(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('events', events);

    function events() {
        return {
            templateUrl: 'app/views/events.html',
            controller: 'EventsController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                action: '@'
            }
        };
    }

})();
