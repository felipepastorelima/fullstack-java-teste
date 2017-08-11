(function () {
  'use strict';

  angular
    .module('app.company')
    .controller('CompanyListController', CompanyListController);

  CompanyListController.$inject = ['companies'];
  function CompanyListController(companies) {
    var vm = this;
    vm.companies = companies || [];
  }

})();
