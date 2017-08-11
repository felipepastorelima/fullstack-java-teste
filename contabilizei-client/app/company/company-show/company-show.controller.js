(function () {
  'use strict';

  angular
    .module('app.company')
    .controller('CompanyShowController', CompanyShowController);

  CompanyShowController.$inject = ['$state', 'company'];
  function CompanyShowController($state, company) {
    var vm = this;
    vm.company = company;
  }

})();
