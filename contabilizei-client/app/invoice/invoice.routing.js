(function () {
  'use strict';

  angular
    .module('app.invoice')
    .config(statesConfig);

  statesConfig.$inject = ['$stateProvider', '$urlRouterProvider'];
  function statesConfig($stateProvider, $urlRouterProvider) {
    $stateProvider
      .state('invoice-new', {
        url: '/company/:companyId/invoice/new',
        templateUrl: 'app/invoice/invoice-form/invoice-form.html',
        controller: 'InvoiceFormController',
        controllerAs: 'vm',
        resolve: {
          company: companyResolve
        }
      });
  }

  companyResolve.$inject = ['$stateParams', 'CompanyService'];
  function companyResolve($stateParams, CompanyService) {
    return CompanyService.find($stateParams.companyId);
  }

})();
