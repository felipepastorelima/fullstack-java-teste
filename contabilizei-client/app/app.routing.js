(function () {
  'use strict';

  angular
    .module('app')
    .config(statesConfig);

  statesConfig.$inject = ['$stateProvider', '$urlRouterProvider'];
  function statesConfig($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');
  }

})();
