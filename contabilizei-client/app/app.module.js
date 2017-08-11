(function () {
  'use strict';

  angular.module('app', [
    // Vendor
    'ui.router',
    'ngMessages',
    'ui.utils.masks',

    // App
    'app.core',
    'app.company',
    'app.invoice',
    'app.tax',
  ]);

})();
