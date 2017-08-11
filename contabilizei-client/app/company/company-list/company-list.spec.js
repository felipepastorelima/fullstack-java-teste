"use strict";

describe('Lista de empresas', function () {
  describe('quando possui resultados', function () {
    beforeEach(function () {
      browser.addMockModule('httpBackendMock', httpBackendMock);
      browser.get('/');

      function httpBackendMock() {
        angular
          .module('httpBackendMock', ['ngMockE2E'])
          .run(function ($httpBackend, environment) {
            $httpBackend.whenGET(/^app\//).passThrough();
            $httpBackend.whenGET(environment.apiUrl + '/company').respond(function () {
              var code = 200;
              var data = [{
                name: 'Empresa 1',
                cnpj: '01374916000174',
              }];

              return [code, data];
            });
          });
      }
    });

    it('exibe nome da empresa', function (done) {
      var firstCompany = element.all(by.className('company-list-item')).first();
      expect(firstCompany.getText()).toContain('Empresa 1');
      done();
    });

    it('exibe cnpj da empresa', function (done) {
      var firstCompany = element.all(by.className('company-list-item')).first();
      expect(firstCompany.getText()).toContain('01374916000174');
      done();
    });
  });

  describe('quando lista está vazia', function () {
    beforeEach(function () {
      browser.addMockModule('httpBackendMock', httpBackendMock);
      browser.get('/');

      function httpBackendMock() {
        angular
          .module('httpBackendMock', ['ngMockE2E'])
          .run(function ($httpBackend, environment) {
            $httpBackend.whenGET(/^app\//).passThrough();
            $httpBackend.whenGET(environment.apiUrl + '/company').respond(function () {
              var code = 200;
              var data = [];

              return [code, data];
            });
          });
      }
    });

    it('exibe mensagem de lista vazia', function (done) {
      var firstCompany = element.all(by.className('mdl-card__supporting-text')).first();
      expect(firstCompany.getText()).toContain('Cadastre a primeira empresa para começar a utilizar o sistema');
      done();
    });

  });

});
