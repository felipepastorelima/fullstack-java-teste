(function () {
  'use strict';

  angular
    .module('app.tax')
    .service('TaxService', TaxService);

  TaxService.$inject = ['$http', 'environment'];
  function TaxService($http, environment) {
    this.findAllByCompanyAtMonth = findAllByCompanyAtMonth;
    this.generateForCompanyAtMonth = generateForCompanyAtMonth;
    this.markAsPayed = markAsPayed;

    ////////////////

    function findAllByCompanyAtMonth(companyId, referenceDate) {
      var params = {
        referenceDate: referenceDate
      };

      return $http.get(
        environment.apiUrl + '/company/' + companyId + '/tax',
        { params: params }
      ).then(complete);

      function complete(response) {
        return response.data;
      }
    }

    function generateForCompanyAtMonth(companyId, referenceDate) {
      return $http
        .post(
          environment.apiUrl + '/company/' + companyId + '/tax',
          { referenceDate: referenceDate }
        )
        .then(complete);

      function complete(response) {
        return response.data;
      }
    }

    function markAsPayed(companyId, id) {
      return $http
        .put(
          environment.apiUrl + '/company/' + companyId + '/tax/' + id + '/payed'
        )
        .then(complete);

      function complete(response) {
        return response.data;
      }
    }

  }
})();
