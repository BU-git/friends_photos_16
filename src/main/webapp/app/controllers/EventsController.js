(function() {
    'use strict';

    angular
        .module('friends_photos')
        .controller('EventsController', EventsController);

    EventsController.$inject = ['$injector', 'eventsService', 'photosService'];

    function EventsController($injector, eventsService, photosService) {
        var ctrl = this;
        angular.extend(ctrl, {
            listAction: listAction,
            editAction: editAction
        });

        var testEvent = {
            name: 'test event create',
            type_id: '1',
            description: 'Description Description Description'
        };

        init();

        function init() {
            $injector.invoke(ctrl[ctrl.action + 'Action']);
        }

        function listAction() {
            eventsService.getList().then(function (list) {
                ctrl.events = list;
            });
        }

        function editAction() {
            angular.extend(ctrl, {
                save: save
            });
            if (ctrl.eventId) {
                eventsService.getById(ctrl.eventId).then(function (event) {
                    ctrl.event = event;
                });
            } else {
                ctrl.event = testEvent;
            }
            ctrl.photos = [];
        }

        function save(event) {
            eventsService.save(event).then(function (eventId) {
                photosService
                    .uploadPhotos(eventId, ctrl.photos)
                    .then(function success (result) {
                        ctrl.uploadedPhotos = result;
                    }, function error () {
                        debugger
                    }, function progress (count) {
                        ctrl.uploadedPhotos = count;
                    });
            });
        }
    }

})();
