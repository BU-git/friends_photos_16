(function() {
    'use strict';

    angular
        .module('friends_photos')
        .config(config);

    config.$inject = ['cfpLoadingBarProvider'];

    function config(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.latencyThreshold = 10;
    }

})();
