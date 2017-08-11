(function () {
  'use strict';

  angular
    .module('app')
    .constant('environment', {
      apiUrl: 'http://localhost:8080/contabilizei-server/api',
    });
})();
