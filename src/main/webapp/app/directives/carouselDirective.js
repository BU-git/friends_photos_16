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
                event: '=',
                photos: '=',
                index: '@'
            },
            template:
                '<img photo="ctrl.photos[ctrl.index]" ng-click="ctrl.next()">',
            link: link
        };
        return directive;

        function link(scope, elem, attrs) {}

        function Controller() {
            var ctrl = this;
            angular.extend(ctrl, {
                index: angular.isDefined(ctrl.index) ? parseInt(ctrl.index) : 0,
                next: next
            });

            function next() {
                ctrl.index = parseInt(ctrl.index);
                ctrl.index = (ctrl.index == ctrl.photos.length - 1) ? 0 : ctrl.index + 1;
            }
        }
    }

})();
