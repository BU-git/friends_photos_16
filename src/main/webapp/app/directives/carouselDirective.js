(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('carousel', carouselDirective);

    carouselDirective.$inject = [];

    function carouselDirective() {
        var directive = {
            restrict: 'AE',
            controller: Controller,
            controllerAs: 'ctrl',
            bindToController: {
                photos: '='
            },
            template: '<img ng-src="photos/{{ctrl.photos[ctrl.current]}}/file" ng-click="ctrl.next()" flex>',
            link: link
        };
        return directive;

        function link(scope, elem, attrs) {}

        function Controller() {
            var ctrl = this;
            angular.extend(ctrl, {
                current: 0,
                next: next
            });

            function next() {
                ctrl.current = (ctrl.current == ctrl.photos.length - 1) ? 0 : ctrl.current + 1;
            }
        }
    }

})();
