(function() {
  'use strict';

  angular
    .module('app.invoice')
    .directive('appInvoiceList', appInvoiceList);

  function appInvoiceList() {
    var directive = {
        bindToController: true,
        controller: InvoiceListController,
        controllerAs: 'vm',
        restrict: 'EA',
        templateUrl: 'app/invoice/invoice-list/invoice-list.html',
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

  InvoiceListController.$inject = ['$stateParams', 'InvoiceService', 'ServerErrorHandler', 'TaxAnnex', 'InvoicePresenceCheckerService'];
  function InvoiceListController($stateParams, InvoiceService, ServerErrorHandler, TaxAnnex, InvoicePresenceCheckerService) {
    var vm = this;
    vm.loading = true;
    vm.invoices = [];
    vm.formatCurrency = formatCurrency;
    vm.formatDate = formatDate;
    vm.formatTaxAnnex = formatTaxAnnex;
    vm.referenceDate = buildReferenceDate($stateParams.referenceDate);
    vm.destroy = destroy;
    vm.activate = activate;

    function activate() {
      list();
    }

    function list() {
      return InvoiceService.findAllByCompanyAtMonth(
        vm.company.id,
        vm.referenceDate
      ).then(function(invoices){
        vm.invoices = invoices;
        vm.loading = false;
        var hasInvoices = vm.invoices && vm.invoices.length > 0;
        InvoicePresenceCheckerService.hasInvoices = hasInvoices;
      }).catch(function(error) {
        ServerErrorHandler.handle(error);
        vm.loading = false;
        InvoicePresenceCheckerService.hasInvoices = false;
      });
    }

    function formatCurrency(value) {
      return numeral(Number(value)).format('$0,0.00');
    }

    function formatDate(value) {
      return moment(value).format('DD/MM/YYYY');
    }

    function formatTaxAnnex(value) {
      if (!value) {
        return 'N/A';
      }
      return TaxAnnex[value];
    }

    function buildReferenceDate(param) {
      if (!param) {
        return +moment().startOf('month');
      }

      return +moment(param, 'YYYYMM').startOf('month');
    }

    function destroy(id) {
      vm.loading = true;

      return InvoiceService
        .destroy(
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

  }

})();
