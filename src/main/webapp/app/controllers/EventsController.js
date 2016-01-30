(function () {
    'use strict';

    angular
        .module('friends_photos')
        .controller('EventsController', EventsController);

    EventsController.$inject = ['$injector', '$scope', 'eventsService', 'photosService'];

    function EventsController($injector, $scope, eventsService, photosService) {
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
                chooseLocation: chooseLocation,
                save: save
            });
            if (ctrl.eventId) {
                eventsService.getById(ctrl.eventId).then(function (event) {
                    ctrl.event = event;
                });
                //photosService.getEventPhotos(ctrl.eventId).then(function (photos) {
                //    ctrl.photos = photos;
                //});
            } else {
                ctrl.event = testEvent;
                ctrl.photos = [];
            }
        }

        function save(event) {
            eventsService.save(event).then(function (eventId) {
                photosService
                    .uploadPhotos(eventId, ctrl.photos.filter(function (file) {
                        return file instanceof File;
                    }))
                    .then(function success(result) {
                        ctrl.uploadedPhotos = result;
                    }, function error() {
                        debugger
                    }, function progress(count) {
                        ctrl.uploadedPhotos = count;
                    });
            });
        }

        function chooseLocation(e) {
            $injector.invoke(['$mdDialog', function ($mdDialog) {
                $mdDialog.show({
                    templateUrl: 'app/views/choose-location-map.html',
                    parent: angular.element(document.body),
                    targetEvent: e,
                    clickOutsideToClose: true,
                    fullscreen: true,
                    scope: angular.extend($scope.$new(), {
                        map: {
                            center: {
                                latitude: ctrl.event.lat || 50.42270673841995,
                                longitude: ctrl.event.lng || 30.55322265625
                            },
                            zoom: 8
                        },
                        marker: {
                            id: 0,
                            coords: {
                                latitude: ctrl.event.lat || 50.42270673841995,
                                longitude: ctrl.event.lng || 30.55322265625
                            },
                            options: {
                                draggable: true
                            }
                        }
                    }),
                    controller: ['$mdDialog', '$scope', function ($mdDialog, $scope) {
                        angular.extend($scope, {
                            ok: function () {
                                $mdDialog.hide($scope.marker.coords);
                            },
                            closeDialog: $mdDialog.cancel
                        });
                    }]
                }).then(function (coords) {
                    ctrl.event.lat = coords.latitude;
                    ctrl.event.lng = coords.longitude;
                });
            }]);
        }
    }

})();
