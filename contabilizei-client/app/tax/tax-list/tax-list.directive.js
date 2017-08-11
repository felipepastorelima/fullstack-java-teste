(function() {
  'use strict';

  angular
    .module('app.tax')
    .directive('appTaxList', appTaxList);

  function appTaxList() {
    var directive = {
        bindToController: true,
        controller: TaxListController,
        controllerAs: 'vm',
        restrict: 'EA',
        templateUrl: 'app/tax/tax-list/tax-list.html',
        scope: {
          company: '='
        },
        link: link
    };
    return directive;

    function link(scope, el, attr, vm) {
      vm.activate();
    }
  }

  TaxListController.$inject = ['$timeout', '$stateParams', 'TaxService', 'ServerErrorHandler', 'InvoicePresenceCheckerService'];
  function TaxListController($timeout, $stateParams, TaxService, ServerErrorHandler, InvoicePresenceCheckerService) {
    var vm = this;
    vm.loading = true;
    vm.taxes = [];
    vm.formatCurrency = formatCurrency;
    vm.formatDate = formatDate;
    vm.referenceDate = buildReferenceDate($stateParams.referenceDate);
    vm.referenceDateLabel = buildReferenceDateLabel(vm.referenceDate);
    vm.markAsPayed = markAsPayed;
    vm.generate = generate;
    vm.activate = activate;
    vm.hasInvoices = hasInvoices;

    function activate() {
      list();
    }

    function list() {
      return TaxService.findAllByCompanyAtMonth(
        vm.company.id,
        vm.referenceDate
      ).then(function(taxes){
        vm.taxes = taxes;
        vm.loading = false;

        $timeout(function() {
          componentHandler.upgradeAllRegistered();
        });
      }).catch(function(error) {
        ServerErrorHandler.handle(error);
        vm.loading = false;
      });
    }

    function generate() {
      vm.loading = true;

      return TaxService
        .generateForCompanyAtMonth(
          vm.company.id,
          vm.referenceDate
        )
        .then(function() {
          return list();
        }).catch(function(error) {
          ServerErrorHandler.handle(error);
          vm.loading = false;
        });
    }

    function formatCurrency(value) {
      return numeral(Number(value)).format('$0,0.00');
    }

    function formatDate(value) {
      return moment(value).format('DD/MM/YYYY');
    }

    function buildReferenceDate(param) {
      if (!param) {
        return +moment().startOf('month');
      }

      return +moment(param, 'YYYYMM').startOf('month');
    }

    function markAsPayed(id) {
      vm.loading = true;

      return TaxService
        .markAsPayed(
          vm.company.id,
          id
        )
        .then(function() {
          return list();
        }).catch(function(error) {
          ServerErrorHandler.handle(error);
          vm.loading = false;
        });
    }

    function buildReferenceDateLabel(referenceDate) {
      return moment(referenceDate).format('MMM/YYYY');
    }

    function hasInvoices() {
      return InvoicePresenceCheckerService.hasInvoices;
    }

  }

})();
