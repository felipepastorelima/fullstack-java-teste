(function() {
  'use strict';

  angular
    .module('app.company')
    .directive('appCompanyReferenceDate', appCompanyReferenceDate);

  function appCompanyReferenceDate() {
    var directive = {
        bindToController: true,
        controller: CompanyReferenceDateController,
        controllerAs: 'vm',
        restrict: 'EA',
        templateUrl: 'app/company/company-reference-date/company-reference-date.html',
        scope: {
          company: '='
        }
    };
    return directive;
  }

  CompanyReferenceDateController.$inject = ['$state', '$stateParams'];
  function CompanyReferenceDateController ($state, $stateParams) {
    var vm = this;
    vm.referenceDate = buildReferenceDate($stateParams.referenceDate);
    vm.referenceDateLabel = buildReferenceDateLabel();
    vm.lastReferenceDate = isLastReferenceDate();
    vm.previousReferenceDate = previousReferenceDate;
    vm.nextReferenceDate = nextReferenceDate;

    function previousReferenceDate() {
      var previousReferenceDateParam = vm.referenceDate.subtract(1, 'month').format('YYYYMM');
      $state.go('company-show', { id: vm.company.id, referenceDate: previousReferenceDateParam });
    }

    function nextReferenceDate() {
      var nextReferenceDateParam = vm.referenceDate.add(1, 'month').format('YYYYMM');
      $state.go('company-show', { id: vm.company.id, referenceDate: nextReferenceDateParam });
    }

    function isLastReferenceDate() {
      return vm.referenceDate.isSame(moment(), 'month');
    }

    function buildReferenceDate(param) {
      if (!param) {
        return moment();
      }

      return moment(param, 'YYYYMM');
    }

    function buildReferenceDateLabel(referenceDate) {
      return moment(vm.referenceDate, 'YYYYMM').format('MMM/YYYY');
    }
  }
})();
