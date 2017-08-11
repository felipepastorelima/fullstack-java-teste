(function () {
  'use strict';

  angular
    .module('app.invoice')
    .service('InvoiceService', InvoiceService);

  InvoiceService.$inject = ['$http', 'environment'];
  function InvoiceService($http, environment) {
    this.find = find;
    this.findAllByCompanyAtMonth = findAllByCompanyAtMonth;
    this.save = save;
    this.destroy = destroy;

    ////////////////

    function destroy(companyId, id) {
      return $http.delete(environment.apiUrl + '/company/' + companyId + '/invoice/' + id)
        .then(function(){});
    }

    function find(companyId, id) {
      return $http.get(environment.apiUrl + '/company/' + companyId + '/invoice/' + id)
        .then(complete);

      function complete(response) {
        return response.data;
      }
    }

    function findAllByCompanyAtMonth(companyId, referenceDate) {
      var params = {
        referenceDate: referenceDate
      };

      return $http.get(
        environment.apiUrl + '/company/' + companyId + '/invoice',
        { params: params }
      ).then(complete);

      function complete(response) {
        return response.data;
      }
    }

    function save(invoice) {
      return $http
        .post(
          environment.apiUrl + '/company/' + invoice.company.id + '/invoice',
          invoice
        )
        .then(complete);

      function complete(response) {
        return response.data;
      }
    }
  }
})();
