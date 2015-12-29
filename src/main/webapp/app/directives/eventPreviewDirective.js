(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('eventPreview', eventPreviewDirective);

    eventPreviewDirective.$inject = ['$mdDialog'];

    function eventPreviewDirective($mdDialog) {
        var directive = {
            restrict: 'AE',
            templateUrl: 'app/directives/templates/event-preview-directive.html',
            scope: {
                event: '='
            },
            link: link
        };
        return directive;

        function link(scope, elem, attrs) {
            angular.extend(scope, {
                showCarousel: $mdDialog.showCarousel
            });
        }
    }

})();
