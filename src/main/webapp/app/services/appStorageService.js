(function () {
    'use strict';

    angular
        .module('friends_photos')
        .service('appStorage', appStorageService);

    appStorageService.$inject = [];

    function appStorageService() {
        var service = this;
        // export public properties and functions
        angular.extend(service, {});

    }

})();
