(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('photo', photoDirective);

    photoDirective.$inject = ['$parse'];

    function photoDirective($parse) {
        var directive = {
            restrict: 'AE',
            link: link
        };
        return directive;

        function link(scope, elem, attrs) {
            scope.$watch(attrs.photo, function (photo) {
                // var photo = $parse(attrs.photo)(scope);
                if (photo instanceof File) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        elem.attr('src', e.target.result);
                    };
                    reader.readAsDataURL(photo);
                } else {
                    elem.attr('src', 'photos/' + photo + '/file');
                }
            });
        }
    }

})();
