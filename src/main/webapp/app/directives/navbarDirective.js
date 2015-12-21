(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('navbar', navbarDirective);

    navbarDirective.$inject = ['$document', '$window'];

    function navbarDirective($document, $window) {
        return {
            restrict: 'C',
            link: function (scope, elem, attrs) {
                var scrollOffset = 64;
                $document.on('scroll', onScroll);

                onScroll();

                function onScroll() {
                    console.log($window.scrollY);
                    if ($window.scrollY > scrollOffset) {
                        elem.addClass('navbar-fixed-top');
                    } else {
                        elem.removeClass('navbar-fixed-top');
                    }
                }

                elem.on('$destroy', function () {
                    $document.off('scroll');
                });
            }
        };
    }

})();
