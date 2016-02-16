(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('carousel', carouselDirective);

    carouselDirective.$inject = [];

    function carouselDirective() {
        var directive = {
            restrict: 'AE',
            controller: CarouselController,
            controllerAs: 'ctrl',
            bindToController: {
                event: '=',
                photos: '=',
                index: '@'
            },
            template: '<img photo="ctrl.photos[ctrl.active]" ng-click="ctrl.next()">',
            link: link
        };
        return directive;

        function link(scope, elem, attrs) {}

        function CarouselController() {
            var ctrl = this;
            angular.extend(ctrl, {
                active: isNaN(parseInt(ctrl.index)) ? 0 : parseInt(ctrl.index),
                next: next
            });

            function next() {
                ctrl.active = parseInt(ctrl.active);
                ctrl.active = (ctrl.active == ctrl.photos.length - 1) ? 0 : ctrl.active + 1;
            }
        }
    }

})();
