'use strict';

requirejs.config({
    paths: {
        'jquery': '../../webjars/jquery/1.11.1/jquery',
        'jquery-cookie': '../../webjars/jquery-cookie/1.4.0/jquery.cookie',
        'jquery-form': '../../webjars/jquery-form/3.28.0-2013.02.06/jquery.form',
        'jquery-i18n-properties': '../../webjars/jquery-i18n-properties/1.0.9/jquery.i18n.properties',
        'jquery-qtip2': '../../webjars/qtip2/2.1.1/jquery.qtip',
        'jquery-validation': '../../webjars/jquery-validation/jquery.validate',
        'jquery-validation-additional-methods': '../../webjars/jquery-validation/additional-methods',
        'jquery-ui': '../../webjars/jquery-ui/1.10.4/ui',
        'jquery-extends': '../../webjars/jquery-extends/1.0.0/jquery.extends',
        'jquery-param': '../../webjars/jquery-extends/1.0.0/jquery.param',
        'jquery-tmpl': '../../webjars/jquery-extends/1.0.0/jquery.tmpl',
        'jquery-validation-extends': '../../webjars/jquery-extends/1.0.0/jquery.validate.extends',
        'jquery-validation-qtip': '../../webjars/jquery-extends/1.0.0/jquery.validate.qtip',
        'jquery-inputmask': '../../webjars/jquery-extends/1.0.0/jquery.inputmask',
        'jquery-inputmask-extensions': '../../webjars/jquery-extends/1.0.0/jquery.inputmask.extends',
        'jquery-inputmask-date-extensions': '../../webjars/jquery-extends/1.0.0/jquery.inputmask.date.extensions',
        'jquery-inputmask-numeric-extensions': '../../webjars/jquery-extends/1.0.0/jquery.inputmask.numeric.extensions',
        'jquery-inputmask-phone-extensions': '../../webjars/jquery-extends/1.0.0/jquery.inputmask.phone.extensions',
        'jquery-inputmask-regex-extensions': '../../webjars/jquery-extends/1.0.0/jquery.inputmask.regex.extensions',
        'jquery-bpopup': '../../webjars/jquery-extends/1.0.0/jquery.bpopup',
        'jquery-popup': '../../webjars/jquery-extends/1.0.0/jquery.popup',
        'jquery-applyfield': '../../webjars/jquery-extends/1.0.0/jquery.applyfield'
    },
    shim: {
        'jquery': { exports: '$' },
        'jquery-cookie': { deps: ['jquery'] },
        'jquery-form': { deps: ['jquery'] },
        'jquery-i18n-properties': { deps: ['jquery'] },
        'jquery-qtip2': { deps: ['jquery'] },
        'jquery-validation': { deps: ['jquery'] },
        'jquery-validation-additional-methods': { deps: ['jquery-validation'] },
        'jquery-ui': { deps: ['jquery'] },
        'jquery-extends': { deps: ['jquery'] },
        "jquery-param": { deps: ["jquery-extends"] },
        "jquery-tmpl": { deps: ["jquery"] },
        "jquery-validation-extends": { deps: ["jquery-extends", "jquery-validation"] },
        "jquery-validation-qtip": { deps: ["jquery-extends", "jquery-validation", "jquery-qtip2"] },
        "jquery-inputmask": { deps: ["jquery"] },
        "jquery-inputmask-extensions": { deps: ["jquery-inputmask"] },
        "jquery-inputmask-date-extensions": { deps: ["jquery-inputmask"] },
        "jquery-inputmask-numeric-extensions": { deps: ["jquery-inputmask"] },
        "jquery-inputmask-phone-extensions": { deps: ["jquery-inputmask"] },
        "jquery-inputmask-regex-extensions": { deps: ["jquery-inputmask"] },
        "jquery-bpopup": { deps: ["jquery"] },
        "jquery-popup": { deps: ["jquery-extends", "jquery-tmpl", "jquery-bpopup"] },
        "jquery-applyfield": {deps: ["jquery-extends"] }
    }
});

require(['app'], function(){
    console.log('loaded application')
}, function (error) {
    console.warn('failed load application', arguments);
});