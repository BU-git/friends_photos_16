(function() {
    'use strict';

    angular
        .module('friends_photos')
        .service('photosService', photosService);

    photosService.$inject = ['$http', '$q', 'API_ENDPOINT'];

    function photosService($http, $q, API_ENDPOINT) {
        var service = this;
        // export public properties and functions
        angular.extend(service, {
            getEventPhotos: getEventPhotos,
            uploadPhotos: uploadPhotos
        });

        function uploadPhotos(eventId, photos) {
            var deferred = $q.defer();
            uploadSequence(eventId, photos, 0, deferred);
            return deferred.promise;
        }

        function uploadSequence(eventId, photos, pointer, deferred) {
            if (pointer === photos.length) {
                deferred.resolve(pointer);
            } else {
                var params = {
                    event_id: eventId,
                    file: photos[pointer]
                };
                $http.form(API_ENDPOINT + 'photos/', params, true).then(function () {
                    pointer++;
                    deferred.notify(pointer);
                    uploadSequence(eventId, photos, pointer, deferred);
                });
            }
        }

        function getEventPhotos(eventId) {
            return $http.get(API_ENDPOINT + 'photos/id/events/' + eventId).then(function (res) {
                return res.data.photos;
            });
        }
    }

})();
