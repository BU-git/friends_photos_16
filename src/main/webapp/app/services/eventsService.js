(function() {
    'use strict';

    angular
        .module('friends_photos')
        .service('events', eventsService);

    eventsService.$inject = ['$http'];

    function eventsService($http) {
        var service = this;
        // export public properties and functions
        angular.extend(service, {
            getList: getList,
            save: save
        });

        function getList() {

        }

        function save(event) {
            // TODO
            event.typeId = parseInt(event.typeId);
            $http.post('events', event).then(function (res) {
                debugger
            });
        }
    }

})();
