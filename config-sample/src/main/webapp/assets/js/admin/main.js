'use strict';

require.config({
    paths: {
        'layouts-admin': '../layouts/admin',
        'layouts-default': '../layouts/default'
    }
});

require([
    'jquery-extends',
    'angular',
    'angular-ui-router',
    'layouts-admin',
    './app/info/patent',
    './app/kipris/trialChanges'
], function($, angular) {
    angular.module('app', ['ui.router',
                           'layouts.admin',
                           'admin.app.info.patent',
                           'admin.app.kipris.trialChanges'])
        .config(function($stateProvider, $urlRouterProvider) {
            $stateProvider.state('main', {
                url: '/main',
                templateUrl: 'admin/main.html'
            }).state('patent', {
                url: '/patent',
                templateUrl: 'admin/info/patent.html'
            });
            $urlRouterProvider.otherwise('/main');
        });

    $(document).ready(function() {
        angular.bootstrap(document, ['app']);
    });
});
