"use strict";

describe('Lista de notas fiscais', function () {
    beforeEach(function () {
      browser.addMockModule('httpBackendMock', httpBackendMock);
      browser.get('/');
      element.all(by.className('company-list-item')).get(0).click();

      function httpBackendMock() {
        angular
          .module('httpBackendMock', ['ngMockE2E'])
          .run(function ($httpBackend, environment) {
            var companies = [
              {
                id: 1,
                name: 'Empresa 1',
                cnpj: "67676776000105",
                taxRegime: "SIMPLES_NACIONAL",
                taxAnnexes: [
                  "COMERCIO",
                  "INDUSTRIA"
                ],
                email: "felipepastorelima@gmail.com"
              }
            ];

            var invoices = [
              {
                "id": 1,
                "code": 1,
                "description": "Descrição",
                "amount": "100.99",
                "issueDate": +moment(),
                "taxAnnex": "COMERCIO"
              }
            ];

            var codeOK = 200;

            $httpBackend.whenGET(/^app\//).passThrough();
            $httpBackend.whenGET(environment.apiUrl + '/company/1').respond(function () {
              return [codeOK, companies[0]];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company').respond(function () {
              return [codeOK, companies];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/1/invoice?referenceDate=' + (+moment().subtract(1, 'month').startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, []];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/1/invoice?referenceDate=' + (+moment().startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, invoices];
            });
            $httpBackend.whenDELETE(environment.apiUrl + '/company/1').respond(function(method, url, data) {
              return [codeOK];
            });
            $httpBackend.whenDELETE(environment.apiUrl + '/company/1/invoice/1').respond(function(method, url, data) {
              invoices.pop();
              return [codeOK];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/1/tax?referenceDate=' + (+moment().subtract(1, 'month').startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, []];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/1/tax?referenceDate=' + (+moment().startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, []];
            });

          });
      }
    });

    describe('data de referência', function() {
      it('busca notas fiscais da referência correta', function() {
        element(by.className('previous-company-reference-date')).click();
        expect(
          element(by.className('invoice-list-card')).getText()
        ).toContain('Não existem notas');
      });
    });

    describe('exibição', function() {
      it('exibe data de emissão', function() {
        var expectedDate = require('moment')().format('DD/MM/YYYY');
        expect(element(by.css('.table-invoices')).getText()).toContain(expectedDate);
      });

      it('exibe descrição', function() {
        expect(element(by.css('.table-invoices')).getText()).toContain('Descrição');
      });

      it('exibe valor formatado em reais', function() {
        expect(element(by.css('.table-invoices')).getText()).toContain('R$100,99');
      });

      it('exibe anexo', function() {
        expect(element(by.css('.table-invoices')).getText()).toContain('Comércio');
      });
    });

    describe('exclusão', function() {
      it('exclui nota fiscal', function() {
        element(by.className('delete-invoice-button')).click();

        expect(
          element(by.className('invoice-list-card')).getText()
        ).toContain('Não existem notas');
      });
    });

});
