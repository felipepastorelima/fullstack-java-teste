(function () {
  'use strict';

  angular
    .module('app.company')
    .controller('CompanyFormController', CompanyFormController);

  CompanyFormController.$inject = ['$state', 'ServerErrorHandler', 'CompanyService', 'TaxRegime', 'TaxAnnex'];
  function CompanyFormController($state, ServerErrorHandler, CompanyService, TaxRegime, TaxAnnex) {
    var vm = this;

    vm.company = {
      taxRegime: defaultTaxRegime(),
      taxAnnexes: []
    };

    vm.loading = false;
    vm.save = save;
    vm.taxRegimeOptions = TaxRegime;
    vm.taxAnnexOptions = TaxAnnex;
    vm.toggleTaxAnnex = toggleTaxAnnex;
    vm.isSimplesNacional = isSimplesNacional;

    function defaultTaxRegime() {
      for (var key in TaxRegime) {
        return key;
      }
    }

    function isSimplesNacional() {
      return TaxRegime.SIMPLES_NACIONAL === TaxRegime[vm.company.taxRegime];
    }

    function toggleTaxAnnex(key) {
      var idx = vm.company.taxAnnexes.indexOf(key);

      if (idx > -1) {
        vm.company.taxAnnexes.splice(idx, 1);
        return;
      }

      vm.company.taxAnnexes.push(key);
    }

    function save() {
      if (vm.form.$invalid) {
        angular.forEach(vm.form.$error, function(field) {
            angular.forEach(field, function(errorField) {
                errorField.$setTouched();
            });
        });

        return;
      }

      if (vm.isSimplesNacional()) {
        if (vm.company.taxAnnexes.length === 0) {
          return;
        }
      }

      vm.loading = true;
      CompanyService
        .save(vm.company)
        .then(function(company) {
          $state.go('company-show', {id: company.id});
        })
        .catch(function(error) {
          ServerErrorHandler.handle(error);
          vm.loading = false;
        });
    }
  }

})();
