'use strict';

var require = {
    baseUrl: (configs.contextPath || '') + '/',
    paths: {
        'jquery': '../../webjars/jquery/2.1.1/jquery',
        'jquery-cookie': '../../webjars/jquery-cookie/1.4.0/jquery.cookie',
        'jquery-form': '../../webjars/jquery-form/3.28.0-2013.02.06-3/jquery.form',
        'jquery-i18n-properties': '../../webjars/jquery-i18n-properties/1.0.9/jquery.i18n.properties',
        'jquery-validation': '../../webjars/jquery-validation/1.13.0/jquery.validate',
        'jquery-validation-additional-methods': '../../webjars/jquery-validation/1.13.0/additional-methods',
        'jquery-ui': '../../webjars/jquery-ui/1.10.4/ui',
        'jquery-extends': '../../webjars/jquery-extends/1.0.0-SNAPSHOT/jquery.extends',
        'jquery-extends-locale_ko': '../../webjars/jquery-extends/1.0.0-SNAPSHOT/i18n/jquery.extends-locale_ko',
        'jquery-param': '../../webjars/jquery-extends/1.0.0-SNAPSHOT/jquery.param',
        //'jquery-validation-extends': '../../webjars/jquery-extends/1.0.0-SNAPSHOT/jquery.validate.extends',
        'jquery-inputmask': '../../webjars/jquery-extends/1.0.0-SNAPSHOT/jquery.inputmask.bundle.min',
        'jquery-inputmask-extensions': '../../webjars/jquery-extends/1.0.0-SNAPSHOT/jquery.inputmask.extensions',
        'jquery-applyfield': '../../webjars/jquery-extends/1.0.0-SNAPSHOT/jquery.applyfield'
    },
    shim: {
        'jquery': { exports: '$' },
        'jquery-cookie': ['jquery'],
        'jquery-form': { deps: ['jquery'] },
        'jquery-i18n-properties': { deps: ['jquery'] },
        'jquery-validation': { deps: ['jquery'] },
        'jquery-validation-additional-methods': { deps: ['jquery-validation'] },
        'jquery-ui': { deps: ['jquery'] },
        'jquery-extends': { deps: ['jquery'] },
        'jquery-extends-locale_ko': { deps: ['jquery-extends'] },
        "jquery-param": { deps: ["jquery-extends"] },
        //"jquery-validation-extends": { deps: ["jquery-extends", "jquery-validation"] },
        "jquery-inputmask": { deps: ["jquery"] },
        "jquery-inputmask-extensions": { deps: ["jquery-inputmask"] },
        "jquery-applyfield": {deps: ["jquery-extends"] }
    }
};
