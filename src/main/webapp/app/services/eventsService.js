(function() {
    'use strict';

    angular
        .module('friends_photos')
        .service('eventsService', eventsService);

    eventsService.$inject = ['$http', '$q', 'API_ENDPOINT'];

    function eventsService($http, $q, API_ENDPOINT) {
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
            return $http.get(API_ENDPOINT + 'events/' + id).then(function (res) {
                return res.data;
            });
        }

        function getList() {
            var deferred = $q.defer();
            $http.get(API_ENDPOINT + 'events/accounts/self/owner').then(function (res) {
                loadSequence(res.data.events, res.data.events.length - 1, 10, deferred);
            });
            return deferred.promise;
        }

        function loadSequence(events, pointer, limit, deferred) {
            if (pointer === events.length - limit || pointer < 0) {
                deferred.resolve(events);
            } else {
                var eventId = events[pointer].event_id;
                $http.get(API_ENDPOINT + 'photos/id/events/' + eventId).then(function (res) {
                    angular.extend(events[pointer], res.data);
                    loadSequence(events, --pointer, limit, deferred)
                });
            }
        }

        function save(event) {
            if (event.event_id) {
                return $http.put(API_ENDPOINT + 'events/' + event.event_id, event).then(function (res) {
                    // FIXME
                    return event.event_id;
                });
            } else {
                return $http.post(API_ENDPOINT + 'events', event).then(function (res) {
                    return res.data.id;
                });
            }
        }

        function uploadPhotos(eventId, photos) { debugger
            var params = {
                event_id: eventId,
                file: photos
            };
            return $http.form(API_ENDPOINT + 'events/' + eventId +'/photos', params, true).then(function (res) {
                return res;
            });
        }
    }

})();
