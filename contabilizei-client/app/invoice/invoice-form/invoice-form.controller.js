(function () {
  'use strict';

  angular
    .module('app.invoice')
    .controller('InvoiceFormController', InvoiceFormController);

  InvoiceFormController.$inject = ['$state', 'company', 'ServerErrorHandler', 'InvoiceService', 'TaxAnnex', 'TaxRegime'];
  function InvoiceFormController($state, company, ServerErrorHandler, InvoiceService, TaxAnnex, TaxRegime) {
    var vm = this;
    vm.loading = false;
    vm.company = company;
    vm.save = save;
    vm.taxAnnexOptions = buildTaxAnnexOptions();
    vm.isSimplesNacional = isSimplesNacional;
    vm.invoice = {
      company: company,
      taxAnnex: vm.company.taxAnnexes[0]
    };

    function isSimplesNacional() {
      return TaxRegime.SIMPLES_NACIONAL === TaxRegime[vm.company.taxRegime];
    }

    function buildTaxAnnexOptions() {
      var keys = vm.company.taxAnnexes.slice(0);
      var options = {};
      angular.forEach(keys, function(key) {
        options[key] = TaxAnnex[key];
      });
      return options;
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

      vm.invoice.issueDate = +moment(vm.invoice.issueDate);

      vm.loading = true;
      InvoiceService
        .save(vm.invoice)
        .then(function(invoice) {
          $state.go('company-show', {
            id: vm.company.id,
            referenceDate: moment(invoice.issueDate).format('YYYYMM')
          });
        })
        .catch(function(error) {
          ServerErrorHandler.handle(error);
          vm.loading = false;
        });
    }
  }

})();
