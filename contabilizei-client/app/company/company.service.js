(function () {
  'use strict';

  angular
    .module('app.company')
    .service('CompanyService', CompanyService);

  CompanyService.$inject = ['$http', 'environment'];
  function CompanyService($http, environment) {
    this.find = find;
    this.findAll = findAll;
    this.save = save;
    this.destroy = destroy;

    ////////////////

    function destroy(id) {
      return $http.delete(environment.apiUrl + '/company/' + id)
        .then(function(){});
    }

    function find(id) {
      return $http.get(environment.apiUrl + '/company/' + id)
        .then(complete);

      function complete(response) {
        return response.data;
      }
    }

    function findAll() {
      return $http.get(environment.apiUrl + '/company')
        .then(complete);

      function complete(response) {
        return response.data;
      }
    }

    function save(company) {
      return $http
        .post(
          environment.apiUrl + '/company',
          company
        )
        .then(complete);

      function complete(response) {
        return response.data;
      }
    }
  }
})();
