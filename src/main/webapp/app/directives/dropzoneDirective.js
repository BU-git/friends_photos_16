(function() {
    'use strict';

    angular
        .module('friends_photos')
        .directive('dropzone', dropzoneDirective);

    dropzoneDirective.$inject = ['$parse'];

    function dropzoneDirective($parse) {
        var directive = {
            restrict: 'AE',
            require: 'ngModel',
            link: link
        };
        return directive;

        function link(scope, elem, attrs, ngModel) {
            angular.extend(ngModel, {
                $render: render
            });
            var model = $parse(attrs.ngModel);

            elem[0].ondragover = function () {
                return false;
            };

            elem[0].ondragleave = function () {
                return false;
            };

            elem[0].ondrop = function (e) {
                e.preventDefault();
                scope.$apply(function () {
                    var photosList = model(scope);
                    angular.forEach(e.dataTransfer.files, function (file) {
                        photosList.push(file);
                    });
                    model.assign(scope, photosList);
                });
            };

            function render() {}
        }
    }

})();
