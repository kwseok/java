define([
    'jquery-extends',
    'angular',
    'angular-loading-bar',
    'ui-bootstrap',
    'ui-bootstrap-tpls'
], function($, angular) {
    return angular.module('layouts.admin', ['angular-loading-bar',
                                            'ui.bootstrap',
                                            'ui.bootstrap.tpls',
                                            'ui.bootstrap.datepicker',
                                            'ui.bootstrap.pagination'])
        .config(function(datepickerConfig, datepickerPopupConfig) {
            datepickerConfig.showWeeks = false;
            datepickerConfig.formatDayTitle = 'yyyy MM';
            datepickerPopupConfig.datepickerPopup = 'yyyy-MM-dd';
            datepickerPopupConfig.currentText = '오늘';
            datepickerPopupConfig.clearText = '초기화';
            datepickerPopupConfig.closeText = '닫기';
        })
        .directive('datepickerPattern', function() {
            return {
                restrict: 'A',
                require: 'ngModel',
                link: function(scope, elem, attrs, ngModelCtrl) {
                    var regex = new RegExp(attrs.datepickerPattern || '^(\\d{4})-(\\d{2})-(\\d{2})$');
                    ngModelCtrl.$parsers.unshift(function(value) {
                        if (typeof value === 'string') {
                            var valid = regex.test(value);
                            ngModelCtrl.$setValidity('pattern', valid);
                            if (!valid) return undefined;
                        }
                        return value;
                    });
                }
            };
        });
    ;

    // TODO: 기타설정
});