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
            create: create
        });

        function getList() {

        }

        function create() {

        }
    }

})();
