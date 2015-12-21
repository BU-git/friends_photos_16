(function() {
    'use strict';

    angular
        .module('friends_photos')
        .config(config);

    config.$inject = ['$urlRouterProvider', '$stateProvider'];

    function config($urlRouterProvider, $stateProvider) {
        $urlRouterProvider.when('', '/events');

        // home state
        $stateProvider
            .state('home', {
                url: '',
                views: {
                    header: {
                        templateUrl: 'app/views/header.html'
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
                        template: '<login>'
                    }
                }
            })

            // event states
            .state('home.events', {
                url: '/events',
                views: {
                    'content@': {
                        template: '<events>'
                    }
                }
            })
            .state('home.events.create', {
                url: '/create',
                views: {
                    'content@': {
                        template: '<events-create-edit>'
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
