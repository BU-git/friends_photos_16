(function () {
    'use strict';

    angular
        .module('friends_photos')
        .config(config);

    config.$inject = ['$urlRouterProvider', '$stateProvider'];

    function config($urlRouterProvider, $stateProvider) {
        //$urlRouterProvider.when('', '/events');

        // home state
        $stateProvider
            .state('home', {
                url: '',
                views: {
                    header: {
                        templateUrl: 'app/views/header.html'
                    },
                    content: {
                        templateUrl: 'app/views/index.html'
                    },
                    footer: {
                        templateUrl: 'app/views/footer.html'
                    }
                }
            })

            // Account states
            .state('account', {
                url: '/account',
                views: {
                    content: {
                        template: '<account>'
                    }
                },
                onEnter: ['$document', function ($document) {
                    $document.find('body').addClass('account-container');
                }],
                onExit: ['$document', function ($document) {
                    $document.find('body').removeClass('account-container');
                }]
            })
            .state('account.login', {
                url: '/login',
                views: {
                    'login': {
                        template: '<login>'
                    }
                }
            })
            .state('account.create', {
                url: '/create',
                views: {
                    'content@': {
                        template: '<register>'
                    }
                }
            })

            // event states
            .state('home.events', {
                url: '/events',
                views: {
                    'content@': {
                        template: '<events action="list">'
                        // templateUrl: 'app/views/events.html'
                    },
                    'usermenu': {
                        templateUrl: 'app/views/usermenu.html'
                    }
                }
            })
            .state('home.events.preview', {
                url: '/events',
                views: {
                    'content@': {
                        template: '<events action="list">'
                    }
                }
            })
            .state('home.events.create', {
                url: '/create',
                views: {
                    'content@': {
                        template: '<events-create-edit action="edit">'
                    }
                }
            })
            .state('home.events.edit', {
                url: '/edit/:id',
                views: {
                    'content@': {
                        templateProvider: ['$stateParams', function ($stateParams) {
                            return '<events-create-edit action="edit" event-id="' + $stateParams.id + '">';
                        }]
                    }
                }
            })
            .state('home.events.search', {
                url: '/search',
                views: {
                    'content@': {
                        templateProvider: ['$stateParams', function ($stateParams) {
                            return '<search></search>';
                        }]
                    }
                }
            })
            .state('home.events.details', {
                url: '/details/:id',
                views: {
                    'content@': {
                        templateProvider: ['$stateParams', function ($stateParams) {
                            return '<event-details></event-details>';
                        }]
                    }
                }
            })

            // search state
            .state('home.search', {
                url: '/search',
                views: {
                    'content@': {
                        template: '<events>'
                    }
                }
            });
    }

})();
