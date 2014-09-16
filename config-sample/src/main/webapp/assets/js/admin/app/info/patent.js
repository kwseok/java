define([
    'jquery-extends',
    'angular',
    'angular-resource',
    'angular-ui-router',
    'angular-file-upload',
    'angular-file-upload-html5-shim'
], function($, angular) {
    return angular.module('admin.app.info.patent', ['ngResource', 'ui.router', 'ui.bootstrap.progressbar', 'angularFileUpload'])
        .config(function($stateProvider) {
            $stateProvider.state('info-patent', {
                url: '/info/patent',
                templateUrl: 'admin/info/patent.html',
                controller: 'admin.app.info.patent.ListController'
            });
        })
        .factory('admin.app.info.Patents', function($resource) {
            return $resource('/admin/info/patents/:id', {}, {
                query: { method: 'GET', isArray: false }
            });
        })
        .controller('admin.app.info.patent.ListController', [
            '$scope',
            '$modal',
            'admin.app.info.Patents',
            function($scope, $modal, Patents) {
                $scope.patents = [];
                $scope.pagination = {
                    maxSize: 5,
                    totalCount: 0,
                    currentPage: 1,
                    reverseNo: 0
                };
                $scope.search = {
                    current: {},
                    form: {}
                };

                $scope.pageChanged = function() {
                    Patents.query($.extend({}, $scope.search.current, { page: $scope.pagination.currentPage }), function(response) {
                        $scope.patents = response.list;
                        $scope.pagination.totalCount = response.totalCount;
                        $scope.pagination.currentPage = response.currentPage;
                        $scope.pagination.reverseNo = response.reverseNo;
                    });
                };
                $scope.pageChanged();

                $scope.resetSearch = function() {
                    $scope.search = { current: {}, form: {} };
                    $scope.pagination.currentPage = 1;
                    $scope.pageChanged();
                };

                $scope.performSearch = function() {
                    $scope.search.current = $.extend({}, $scope.search.form);
                    $scope.pagination.currentPage = 1;
                    $scope.pageChanged();
                };

                $scope.detail = function(no) {
                    $modal.open({
                        templateUrl: 'admin/info/patent/detail.html',
                        controller: 'admin.app.info.patent.DetailController',
                        resolve: { id: function() {
                            return no;
                        } },
                        size: 'lg'
                    });
                };
            }
        ])
        .controller('admin.app.info.patent.DetailController', [
            '$scope',
            '$modalInstance',
            '$upload',
            'admin.app.info.Patents',
            'id',
            function($scope, $modalInstance, $upload, Patents, id) {
                $scope.model = {};
                $scope.no = id;

                $scope.datepickerOptions = { opened: {} };
                $scope.openDatepicker = function($event, datepicker) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.datepickerOptions.opened = {};
                    $scope.datepickerOptions.opened[datepicker] = true;
                };

                Patents.query({ id: id }, function(response) {
                    console.log(response);
                    $scope.model = response;
                });

                $scope.submit = function() {
                };

                $scope.close = function() {
                    $modalInstance.close();
                };


                // Upload files

                $scope.uploadOptions = {
                    originalOpenFile: {},
                    originalRegisterFile: {},
                    correctionFile: {},
                    allLawsuitImage: {}
                };

                $scope.selectFile = function(type, files) {
                    if (files && files[0]) {
                        $scope.uploadOptions[type] = { progress: 0, success: true };
                        $upload.upload({ url: '/admin/info/patents/upload/' + type, file: files[0] })
                            .success(function(data) { $.extend($scope.model, data); })
                            .error(function() { $scope.uploadOptions[type].success = false; })
                            .progress(function(e) { $scope.uploadOptions[type].progress = parseInt(100.0 * e.loaded / e.total); })
                        ;
                    }
                };

                $scope.deleteFile = function(type) {
                    $scope.model[type + 'Name'] = null;
                    $scope.model[type + 'Path'] = null;
                    $scope.uploadOptions[type] = {};
                };

            }
        ]);
    ;
});