(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('photo', photoDirective);

    photoDirective.$inject = ['API_ENDPOINT'];

    function photoDirective(API_ENDPOINT) {
        var directive = {
            restrict: 'AE',
            scope: false,
            link: link
        };
        return directive;

        function link(scope, elem, attrs) {
            scope.$watch(attrs.photo, function (photo) {
                if (photo instanceof File) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        elem.attr('src', e.target.result);
                    };
                    reader.readAsDataURL(photo);
                } else {
                    elem.attr('src', API_ENDPOINT + 'photos/' + photo + '/file');
                }
            });
        }
    }

})();
