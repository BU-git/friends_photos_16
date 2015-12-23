(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('eventPreview', eventPreviewDirective);

    eventPreviewDirective.$inject = ['$mdDialog', '$mdMedia'];

    function eventPreviewDirective($mdDialog, $mdMedia) {
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
                showCarousel: showCarousel
            });

            function showCarousel(e) {
                var useFullScreen = ($mdMedia('sm') || $mdMedia('xs')) && scope.customFullscreen;
                $mdDialog.show({
                    //controller: DialogController,
                    template:
                        '<md-dialog class="carousel-modal" aria-label="List Photos">' +
                            //'<md-dialog-content>' +
                                '<carousel photos="event.photos" layout="row">' +
                            //'</md-dialog-content>' +
                        '</md-dialog>',
                    //autoWrap: true,
                    parent: angular.element(document.body),
                    targetEvent: e,
                    clickOutsideToClose: true,
                    fullscreen: useFullScreen,
                    scope: scope.$new()
                }).then(function () {

                }, function () {

                });

                // FIXME
                scope.$watch(function () {
                    return $mdMedia('xs') || $mdMedia('sm');
                }, function(wantsFullScreen) {
                    scope.customFullscreen = (wantsFullScreen === true);
                });
            }
        }


    }

})();
