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
            scope: {
                onFiles: '&'
            },
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
                model.assign(scope, e.dataTransfer.files);
                ngModel.$setViewValue(e.dataTransfer.files);
                ngModel.$render();
                //return false;
            };

            function render() {
                angular.forEach(this.$viewValue, function (file) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        elem.append(angular
                                .element('<img>')
                                .attr('src', e.target.result)
                                .css({width: '30%'})
                        );
                    };
                    reader.readAsDataURL(file);
                });
            }
        }
    }

})();
