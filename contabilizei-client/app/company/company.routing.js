(function () {
  'use strict';

  angular
    .module('app.company')
    .config(statesConfig);

  statesConfig.$inject = ['$stateProvider', '$urlRouterProvider'];
  function statesConfig($stateProvider, $urlRouterProvider) {
    $stateProvider
      .state('company-list', {
        url: '/',
        templateUrl: 'app/company/company-list/company-list.html',
        controller: 'CompanyListController',
        controllerAs: 'vm',
        resolve: {
          companies: companiesResolve
        }
      })
      .state('company-new', {
        url: '/company/new',
        templateUrl: 'app/company/company-form/company-form.html',
        controller: 'CompanyFormController',
        controllerAs: 'vm'
      })
      .state('company-show', {
        url: '/company/:id?referenceDate',
        templateUrl: 'app/company/company-show/company-show.html',
        controller: 'CompanyShowController',
        controllerAs: 'vm',
        resolve: {
          company: companyResolve
        }
      });
  }

  companyResolve.$inject = ['$stateParams', 'CompanyService'];
  function companyResolve($stateParams, CompanyService) {
    return CompanyService.find($stateParams.id);
  }

  companiesResolve.$inject = ['CompanyService'];
  function companiesResolve(CompanyService) {
    return CompanyService.findAll();
  }

})();
