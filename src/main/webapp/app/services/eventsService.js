(function() {
    'use strict';

    angular
        .module('friends_photos')
        .service('eventsService', eventsService);

    eventsService.$inject = ['$http', '$q', 'cfpLoadingBar'];

    function eventsService($http, $q, cfpLoadingBar) {
        var service = this;
        // export public properties and functions
        angular.extend(service, {
            getById: getById,
            getList: getList,
            save: save,
            uploadPhotos: uploadPhotos
        });

        function getById(id) {
            id = parseInt(id);
            return $http.get('events/' + id).then(function (res) {
                var event = res.data;
                return $http.get('events/' + id + '/photos/id').then(function (res) {
                    return angular.extend(event, res.data);
                });
            });
        }

        function getList() {
            var deferred = $q.defer();
            var events = [];
            cfpLoadingBar.start();
            deferred.promise.then(function () {
                cfpLoadingBar.complete();
            });
            $http.get('events').then(function (res) {
                var list = res.data.events;
                loadSequence(events, list, list.length - 1, 10, deferred);
            });
            return deferred.promise;
        }

        function loadSequence(events, list, pointer, limit, deferred) {
            if (pointer === list.length - limit) {
                deferred.resolve(events);
            } else {
                var eventId = list[pointer].event_id;
                $http.get('events/' + eventId).then(function (res) {
                    return res.data;
                }).then(function (event) {
                    $http.get('events/' + eventId + '/photos/id').then(function (res) {
                        angular.extend(event, res.data);
                        events.push(event);
                        loadSequence(events, list, --pointer, limit, deferred)
                    })
                });
            }
        }

        function save(event) {
            if (event.event_id) {
                return $http.put('events/' + event.event_id, event).then(function (res) {
                    // FIXME
                    return event.event_id;
                });
            } else {
                return $http.post('events', event).then(function (res) {
                    return res.data.id;
                });
            }
        }

        function uploadPhotos(eventId, photos) {
            var params = {
                event_id: eventId,
                file: photos
            };
            return $http.form('photos', params, true).then(function (res) {
                return res;
            });
        }
    }

})();
