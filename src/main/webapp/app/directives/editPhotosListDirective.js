(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('editPhotosList', editPhotosListDirective);

    editPhotosListDirective.$inject = ['$parse'];

    function editPhotosListDirective($parse) {
        var directive = {
            restrict: 'AE',
            template:
                '<div flex ng-cloak>' +
                    '<md-grid-list md-cols="6" md-row-height="130px"> ' +
                        '<md-grid-tile ng-repeat="photo in photosList"> ' +
                            '<img photo="photo" style="width: 100%; height: 100%;" />' +
                            '<md-grid-tile-footer><h3>Test</h3></md-grid-tile-footer>' +
                        '</md-grid-tile>' +
                    '</md-grid-list>' +
                '</div>',
            link: link
        };
        return directive;

        function link(scope, elem, attrs) {
            // var model = $parse(attrs.ngModel);
            angular.extend(scope, {
                photosList: [],
                getPhotoSrc: getPhotoSrc
            });

            function getPhotoSrc(photo) {

            }

            scope.$watchCollection(attrs.photosList, function (list) {
                scope.photosList = list;
            });

/*            scope.$watchCollection(attrs.photosList, function (list) {
                elem.empty();
                angular.forEach(list, function (file) {
                    var photoElem = angular
                        .element('<img>')
                        .addClass('img-thumbnail')
                        .css({width: '100%', height: '130px', margin: '7px 0'});


                    elem.append(photoElem.wrap('<div class="col-lg-2">').parent());

                    if (file instanceof File) {
                        var reader = new FileReader();
                        reader.onload = function (e) {
                            photoElem.attr('src', e.target.result);
                        };
                        reader.readAsDataURL(file);
                    } else {
                        photoElem.attr('src', 'photos/' + file + '/file');
                    }
                });
            });*/
        }
    }

})();
