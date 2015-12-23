(function() {
    'use strict';

    angular
        .module('friends_photos')
        .service('photosService', photosService);

    photosService.$inject = ['$http', '$q'];

    function photosService($http, $q) {
        var service = this;
        // export public properties and functions
        angular.extend(service, {
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
                $http.form('photos', params, true).then(function () {
                    pointer++;
                    deferred.notify(pointer);
                    uploadSequence(eventId, photos, pointer, deferred);
                });
            }
        }
    }

})();
