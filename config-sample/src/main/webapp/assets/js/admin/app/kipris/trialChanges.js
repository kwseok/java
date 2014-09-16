'use strict';

define([
    'jquery-extends',
    'angular',
    'angular-resource',
    'angular-ui-router',
    'layouts-admin'
], function($, angular) {
    var PACKAGE = 'admin.app.kipris';

    return angular.module(PACKAGE + '.trialChanges', ['ngResource',
                                                      'ui.router',
                                                      'layouts.admin'])
        .config(function($stateProvider) {
            $stateProvider.state('kipris-trialChanges', {
                url: '/kipris/trialChanges',
                templateUrl: 'admin/kipris/trialChanges.html',
                controller: PACKAGE + '.TrialChangesController',
                resolve: {
                    trialChanges: [PACKAGE + '.TrialChanges', function(TrialChanges) {
                        return TrialChanges.query().$promise;
                    }]
                }
            });
        })
        .factory(PACKAGE + '.TrialChanges', function($resource) {
            return $resource('/admin/kipris/trialChanges/:taskId', {taskId: '@taskId'}, {
                query: {method: 'GET', isArray: false}
            });
        })
        .controller(PACKAGE + '.TrialChangesController', [
            '$scope',
            'trialChanges',
            PACKAGE + '.TrialChanges',
            function($scope, trialChanges, TrialChanges) {
                $scope.task = trialChanges.task;
                $scope.totalCount = trialChanges.totalCount;
                $scope.successCount = trialChanges.successCount;
                $scope.failureCount = trialChanges.failureCount;
                $scope.restCount = $scope.totalCount - $scope.successCount - $scope.failureCount;

                function filterChanges(changes, flag, ranks) {
                    return changes.filter(function(value) {
                        return value.changeFlag === flag && ranks.indexOf(value.trialRank) > -1;
                    });
                }

                $scope.firstNewChanges = filterChanges(trialChanges.changes, 'Y', ['1']);
                $scope.firstGeneralChanges = filterChanges(trialChanges.changes, 'G', ['1']);
                $scope.firstDecisionChanges = filterChanges(trialChanges.changes, 'D', ['1']);
                $scope.courtNewChanges = filterChanges(trialChanges.changes, 'Y', ['2', '3']);
                $scope.courtGeneralChanges = filterChanges(trialChanges.changes, 'G', ['2', '3']);
                $scope.courtDecisionChanges = filterChanges(trialChanges.changes, 'D', ['2', '3']);

                $scope.status = {
                    isFirstOpen: $scope.firstNewChanges.length > 0,
                    isFirstDisabled: $scope.firstNewChanges.length === 0
                };

                $scope.taskStateToString = function(taskState) {
                    if (taskState) {
                        switch (taskState) {
                        case 'R': return '준비';
                        case 'P': return '진행';
                        case 'C': return '완료';
                        case 'F': return '실패';
                        }
                    }
                };
                $scope.recollectFailure = function() {
                    alert('Comming soon...');
                };
                $scope.isAppliable = function(value) {
                    return value.taskState === 'C' && (value.trialRank === '1' || value.changeFlag === 'Y');
                };
                $scope.apply = function(value, values) {
                    TrialChanges.save({taskId: value.taskId}, function(response) {
                        if (response.trial) values.remove(value);
                    }, function(response) {
                        alert(response.data.errorMessage || response.statusText);
                    });
                };
            }
        ])
        ;
});