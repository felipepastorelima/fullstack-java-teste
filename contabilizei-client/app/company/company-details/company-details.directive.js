(function() {
  'use strict';

  angular
    .module('app.company')
    .directive('appCompanyDetails', appCompanyDetails);

  function appCompanyDetails() {
    var directive = {
        bindToController: true,
        controller: CompanyDetailsController,
        controllerAs: 'vm',
        restrict: 'EA',
        templateUrl: 'app/company/company-details/company-details.html',
        scope: {
          company: '='
        }
    };
    return directive;
  }

  CompanyDetailsController.$inject = ['$state', 'CompanyService', 'ServerErrorHandler', 'TaxRegime', 'TaxAnnex'];
  function CompanyDetailsController ($state, CompanyService, ServerErrorHandler, TaxRegime, TaxAnnex) {
    var vm = this;
    vm.destroy = destroy;
    vm.taxRegimeLabel = taxRegimeLabel;
    vm.taxAnnexLabel = taxAnnexLabel;
    vm.isSimplesNacional = isSimplesNacional;

    function destroy() {
      CompanyService
        .destroy(vm.company.id)
        .then(function() {
          $state.go('company-list');
        }).catch(function(error) {
          ServerErrorHandler.handle(error);
        });
    }

    function isSimplesNacional() {
      return TaxRegime.SIMPLES_NACIONAL === TaxRegime[vm.company.taxRegime];
    }

    function taxRegimeLabel(key) {
      if (!key) return null;
      return TaxRegime[key];
    }

    function taxAnnexLabel(key) {
      if (!key) return null;
      return TaxAnnex[key];
    }
  }
})();
