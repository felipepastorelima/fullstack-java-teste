"use strict";

describe('Lista de impostos', function () {
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

            var taxes = [];

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
            $httpBackend.whenDELETE(environment.apiUrl + '/company/1/invoice/1').respond(function(method, url, data) {
              invoices.pop();
              return [codeOK];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/1/tax?referenceDate=' + (+moment().subtract(1, 'month').startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, []];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/1/tax?referenceDate=' + (+moment().startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, taxes];
            });
            $httpBackend.whenPUT(environment.apiUrl + '/company/1/tax/1/payed').respond(function(method, url, data) {
              taxes[0].payed = true;
              return [codeOK, taxes[0]];
            });
            $httpBackend.whenPOST(environment.apiUrl + '/company/1/tax').respond(function(method, url, data) {
              taxes.push(
                {
                  "id": 1,
                  "name": "IRPJ",
                  "dueDate": +moment(),
                  "amount": "4.85",
                  "referenceDate": +moment(),
                  "payed": false
                }
              );
              return [codeOK, taxes];
            });

          });
      }
    });

    describe('data de referência', function() {
      it('busca impostos da referência correta', function() {
        element(by.className('previous-company-reference-date')).click();
        expect(
          element(by.className('tax-list-card')).getText()
        ).toContain('Adicione notas fiscais');
      });
    });

    describe('exibição', function() {
      beforeEach(function(){
        element(by.className('tax-generation-button')).click();
      });

      it('exibe data de vencimento', function() {
        var expectedDate = require('moment')().format('DD/MM/YYYY');
        expect(element(by.className('tax-list-card')).getText()).toContain(expectedDate);
      });

      it('exibe nome', function() {
        expect(element(by.className('tax-list-card')).getText()).toContain('IRPJ');
      });

      it('exibe valor formatado em reais', function() {
        expect(element(by.className('tax-list-card')).getText()).toContain('R$4,85');
      });
    });

    describe('botoes', function() {
      describe('quando notas ficais == 0 e impostos > 0', function(){
        it('exibe APAGAR IMPOSTOS', function() {
          // gera impostos
          element(by.className('tax-generation-button')).click();
          // apaga notas fiscais
          element(by.className('delete-invoice-button')).click();

          expect(
            element(by.className('tax-generation-button')).getText()
          ).toContain("APAGAR");
        });
      });

      describe('quando notas fiscais > 0 e impostos == 0', function() {
        it('exibe CALCULAR IMPOSTOS', function() {
          expect(
            element(by.className('tax-generation-button')).getText()
          ).toContain("CALCULAR");
        });
      });

      describe('quando notas fiscais > 0 e impostos > 0', function() {
        it('exibe RECALCULAR IMPOSTOS', function() {
          element(by.className('tax-generation-button')).click();

          expect(
            element(by.className('tax-generation-button')).getText()
          ).toContain("RECALCULAR");
        });
      });

      describe('quando notas ficais == 0 e impostos == 0', function(){
        it('não exibe opção de gerar impostos', function() {
          element(by.className('delete-invoice-button')).click();

          expect(
            element(by.className('tax-generation-button')).isPresent()
          ).toBeFalsy();
        });
      });

    });

    describe('pagamento', function() {
      it('marca como pago', function() {
        element(by.className('tax-generation-button')).click();

        expect(
          element(by.id('tax-payment-button-1')).isPresent()
        ).toBe(true);

        element(by.id('tax-payment-button-1')).click();

        expect(
          element(by.id('tax-payment-info-1')).isPresent()
        ).toBe(true);
      });
    });

});
