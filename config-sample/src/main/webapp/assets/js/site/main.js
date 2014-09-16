'use strict';

require([
    'jquery-extends',
    'angular'
], function($, angular) {
    var app = angular.module('app', [

    ]);

    $(document).ready(function() {
        angular.bootstrap(document, ['app']);
    });
    return app;
});
