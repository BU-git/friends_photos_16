(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('editPhotosList', editPhotosListDirective);

    editPhotosListDirective.$inject = ['$parse', '$mdDialog'];

    function editPhotosListDirective($parse, $mdDialog) {
        var directive = {
            restrict: 'AE',
            template:
                '<div flex ng-cloak>' +
                    '<md-grid-list md-cols="6" md-row-height="130px" md-gutter="10px"> ' +
                        '<md-grid-tile ng-repeat="photo in photosList"> ' +
                            '<img photo="photo" style="width: 100%; height: 100%;" ng-click="showCarousel($event, null, photosList, $index)" />' +
                            '<md-grid-tile-footer><h3>Test</h3></md-grid-tile-footer>' +
                        '</md-grid-tile>' +
                    '</md-grid-list>' +
                '</div>',
            link: link
        };
        return directive;

        function link(scope, elem, attrs) {
            angular.extend(scope, {
                photosList: [],
                showCarousel: $mdDialog.showCarousel
            });

            scope.$watchCollection(attrs.photosList, function (list) {
                scope.photosList = list;
            });
        }
    }

})();
