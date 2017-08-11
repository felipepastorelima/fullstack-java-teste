"use strict";

describe('Cadastra nota fiscal', function () {
    beforeEach(function () {
      browser.addMockModule('httpBackendMock', httpBackendMock);
      browser.get('/');
      element.all(by.className('company-list-item')).get(0).click();
      element(by.className('new-invoice-button')).click();

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
              },
              {
                id: 2,
                name: 'Empresa 2',
                cnpj: "32715378000168",
                taxRegime: "LUCRO_PRESUMIDO",
                taxAnnexes: [],
                email: "felipepastorelima@gmail.com"
              }
            ];

            var invoices = [];
            var codeOK = 200;

            $httpBackend.whenGET(/^app\//).passThrough();
            $httpBackend.whenGET(environment.apiUrl + '/company').respond(function () {
              return [codeOK, companies];
            });

            // Empresa 1
            $httpBackend.whenGET(environment.apiUrl + '/company/1').respond(function () {
              return [codeOK, companies[0]];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/1/invoice?referenceDate=' + (+moment().startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, invoices];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/1/tax?referenceDate=' + (+moment().startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, []];
            });
            $httpBackend.whenPOST(environment.apiUrl + '/company/1/invoice').respond(function(method, url, data) {
              var invoice = angular.fromJson(data);
              invoice.id = 1;
              invoices.pop();
              invoices.push(invoice);
              return [codeOK, invoice];
            });

            // Empresa 2
            $httpBackend.whenGET(environment.apiUrl + '/company/2').respond(function () {
              return [codeOK, companies[1]];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/2/invoice?referenceDate=' + (+moment().startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, invoices];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/2/tax?referenceDate=' + (+moment().startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, []];
            });
            $httpBackend.whenPOST(environment.apiUrl + '/company/2/invoice').respond(function(method, url, data) {
              var invoice = angular.fromJson(data);
              invoice.id = 2;
              invoices.pop();
              invoices.push(invoice);
              return [codeOK, invoice];
            });
          });
      }
    });

    describe('simples nacional', function() {
      it('salva nota fiscal', function () {
        var code = element(by.model('vm.invoice.code'));
        code.sendKeys('1');
        var description = element(by.model('vm.invoice.description'));
        description.sendKeys('Descrição');
        var amount = element(by.model('vm.invoice.amount'));
        amount.sendKeys('123');
        var issueDate = element(by.model('vm.invoice.issueDate'));
        issueDate.sendKeys(require('moment')().format('DDMMYYYY'));
        selectDropdownOption(
          element(by.model('vm.invoice.taxAnnex')),
          'INDUSTRIA'
        );
        element(by.className('save-button')).click();
        expect(element(by.css('.table-invoices')).getText()).toContain('R$1,23');
      });
    });

    describe('lucro presumido', function() {
      beforeEach(function(){
        browser.get('/');
        var indexLucroPresumido = 1;
        element.all(by.className('company-list-item')).get(indexLucroPresumido).click();
        element(by.className('new-invoice-button')).click();
      });

      it('salva nota fiscal', function () {
        var code = element(by.model('vm.invoice.code'));
        code.sendKeys('1');
        var description = element(by.model('vm.invoice.description'));
        description.sendKeys('Descrição');
        var amount = element(by.model('vm.invoice.amount'));
        amount.sendKeys('123');
        var issueDate = element(by.model('vm.invoice.issueDate'));
        issueDate.sendKeys(require('moment')().format('DDMMYYYY'));
        element(by.className('save-button')).click();
        expect(element(by.css('.table-invoices')).getText()).toContain('R$1,23');
      });
    });

    describe('codigo', function() {
      it('valida maior que zero', function() {
        element(by.className('save-button')).click();

        var error = element(by.className('code-textfield'));
        expect(error.getText()).toContain('obrigatório');

        var code = element(by.model('vm.invoice.code'));
        code.sendKeys('0');
        error = element(by.className('code-textfield'));
        expect(error.getText()).toContain('zero');
      });
    });


    describe('descrição', function () {
      it('valida vazia', function () {
        element(by.className('save-button')).click();
        var error = element(by.className('description-textfield'));
        expect(error.getText()).toContain('obrigatória');
      });

      it('valida maior que 255', function () {
        var name = element(by.model('vm.invoice.description'));
        name.sendKeys(
          new Array(258).join('i')
        );
        element(by.className('save-button')).click();
        var error = element(by.className('description-textfield'));
        expect(error.getText()).toContain('muito longa');
      });
    });

    describe('valor', function () {
      it('valida maior que zero', function () {
        element(by.className('save-button')).click();

        var error = element(by.className('amount-textfield'));
        expect(error.getText()).toContain('obrigatório');

        var amount = element(by.model('vm.invoice.amount'));
        amount.sendKeys('0');
        error = element(by.className('amount-textfield'));
        expect(error.getText()).toContain('zero');
      });

      it('valida valor menor de 100bi', function () {
        var amount = element(by.model('vm.invoice.amount'));
        amount.sendKeys(
          new Array(13).join('10')
        );
        element(by.className('save-button')).click();
        var error = element(by.className('amount-textfield'));
        expect(error.getText()).toContain('excede o limite');
      });
    });

    describe('data de emissão', function() {
      it('valida preenchimento', function () {
        element(by.className('save-button')).click();

        var error = element(by.className('issue-date-textfield'));
        expect(error.getText()).toContain('obrigatória');
      });

      it('valida data válida', function () {
        var issueDate = element(by.model('vm.invoice.issueDate'));
        issueDate.sendKeys(
          "00000000"
        );
        element(by.className('save-button')).click();
        var error = element(by.className('issue-date-textfield'));
        expect(error.getText()).toContain('inválida');
      });

      it('valida data passada ou atual', function () {
        var issueDate = element(by.model('vm.invoice.issueDate'));
        var tomorrow = require('moment')().add(1, 'day').format('DDMMYYYY');
        issueDate.sendKeys(tomorrow);
        element(by.className('save-button')).click();
        var error = element(by.className('issue-date-textfield'));
        expect(error.getText()).toContain('data futura');
      });

    });

    describe('anexo', function() {
      it('exibe quando empresa simples nacional', function() {
        expect(
          element(by.className('tax-annex-selectfield')).isPresent()
        ).toBe(true);
      });

      it('esconde quando empresa lucro presumido', function() {
        browser.get('/');
        var indexLucroPresumido = 1;
        element.all(by.className('company-list-item')).get(indexLucroPresumido).click();
        element(by.className('new-invoice-button')).click();

        expect(
          element(by.className('tax-annex-selectfield')).isPresent()
        ).toBeFalsy();
      });
    });

});

var selectDropdownOption = function ( element, value ) {
  if (value){
    element.$('[value=\''+ value +'\']').click();
  }
};
