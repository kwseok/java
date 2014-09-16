define([
    'jquery-extends',
    'angular',
    'angular',
    '../../layouts/admin'
], function($, angular) {
    return angular.module('site.app.user', ['layouts.default'])
        .config(function($) {

        })
        .controller('UserLoginController', function($scope) {
            $scope.model = {
                username: '',
                password: ''
            };

            $scope.signIn = function() {
                alert('test');
            };
        });
});