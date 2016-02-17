(function () {
    'use strict';

    angular
        .module('friends_photos')
        .controller('EventsController', EventsController);

    EventsController.$inject = ['$injector', '$state', '$scope', '$mdDialog', 'eventsService', 'photosService'];

    function EventsController($injector, $state, $scope, $mdDialog, eventsService, photosService) {
        var ctrl = this;
        angular.extend(ctrl, {
            listAction: listAction,
            editAction: editAction,
            showCarousel: $mdDialog.showCarousel
        });
        var eventModel = {
            "name": "",
            "description": "",
            "type_id": undefined,
            "visible": undefined,
            "private": undefined,
            "password": undefined,
            "geo": undefined,
            "location": {
                "lat": undefined,
                "lng": undefined
            }
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
                    ctrl.event = angular.extend(eventModel, event);
                });
                photosService.getEventPhotos(ctrl.eventId).then(function (photos) {
                    ctrl.photos = photos;
                });
            } else {
                ctrl.event = eventModel;
                ctrl.photos = [];
            }
        }

        function save(event) { debugger
            eventsService.save(event).then(function (eventId) {
                return eventId;
            }).then(function (eventId) {
                photosService
                    .uploadPhotos(eventId, ctrl.photos.filter(function (file) {
                        return file instanceof File;
                    }))
                    .then(function success(result) {
                        ctrl.uploadedPhotos = result;
                        if (!ctrl.eventId) $state.go('home.events.edit', {id: eventId});
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
                                latitude: ctrl.event.location.lat || 50.42270673841995,
                                longitude: ctrl.event.location.lng || 30.55322265625
                            },
                            zoom: 8
                        },
                        marker: {
                            id: 0,
                            coords: {
                                latitude: ctrl.event.location.lat || 50.42270673841995,
                                longitude: ctrl.event.location.lng || 30.55322265625
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
                    ctrl.event.location.lat = coords.latitude;
                    ctrl.event.location.lng = coords.longitude;
                });
            }]);
        }
    }

})();
