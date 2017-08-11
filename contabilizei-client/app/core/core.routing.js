(function () {
  'use strict';

  angular
    .module('app.core')
    .config(statesConfig);

  statesConfig.$inject = ['$stateProvider', '$urlRouterProvider'];
  function statesConfig($stateProvider, $urlRouterProvider) {
    $stateProvider
      .state('error', {
        url: '/error',
        templateUrl: 'app/core/error/error.html'
      });
  }

})();
