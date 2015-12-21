(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('eventPreview', eventPreviewDirective);

    function eventPreviewDirective() {
        var directive = {
            restrict: 'AE',
            templateUrl: 'app/directives/templates/event-preview-directive.html',
            link: link
        };
        return directive;

        function link(scope, elem, attrs) {

        }
    }

})();
