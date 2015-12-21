(function() {
    'use strict';

    angular
        .module('friends_photos')
        .controller('EventsController', EventsController);

    EventsController.$inject = ['events'];

    function EventsController(events) {
        var ctrl = this;
        angular.extend(ctrl, {
            save: events.save
        });
    }

})();
