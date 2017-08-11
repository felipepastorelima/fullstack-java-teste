"use strict";

describe('Cadastro de empresas', function () {
    beforeEach(function () {
      browser.addMockModule('httpBackendMock', httpBackendMock);
      browser.get('/');
      element(by.className('new-company-button')).click();

      function httpBackendMock() {
        angular
          .module('httpBackendMock', ['ngMockE2E'])
          .run(function ($httpBackend, environment) {
            var companies = [];
            var codeOK = 200;

            $httpBackend.whenGET(/^app\//).passThrough();
            $httpBackend.whenGET(environment.apiUrl + '/company/1').respond(function () {
              return [codeOK, companies[0]];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company').respond(function () {
              return [codeOK, companies];
            });
            $httpBackend.whenPOST(environment.apiUrl + '/company').respond(function(method, url, data) {
              var company = angular.fromJson(data);
              company.id = 1;
              companies = [company];
              return [codeOK, company];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/1/invoice?referenceDate=' + (+moment().startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, []];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/1/tax?referenceDate=' + (+moment().startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, []];
            });
          });
      }
    });

    it('salva empresa', function () {
      var name = element(by.model('vm.company.name'));
      name.sendKeys('Company 1');
      var cnpj = element(by.model('vm.company.cnpj'));
      cnpj.sendKeys('01374916000174');
      var email = element(by.model('vm.company.email'));
      email.sendKeys('felipepastorelima@gmail.com');
      selectDropdownOption(
        element(by.model('vm.company.taxRegime')),
        'LUCRO_PRESUMIDO'
      );

      element(by.className('save-button')).click();

      var EC = protractor.ExpectedConditions;
      browser
        .wait(EC.urlContains('company/1'), 5000)
        .then(function(result){
        expect(result).toBe(true);
      });
    });

    describe('nome', function () {
      it('valida preenchimento', function () {
        element(by.className('save-button')).click();
        var error = element(by.className('name-textfield'));
        expect(error.getText()).toContain('obrigatório');
      });

      it('valida menor ou igual a 255', function () {
        var name = element(by.model('vm.company.name'));
        name.sendKeys(
          new Array(258).join('i')
        );
        element(by.className('save-button')).click();
        var error = element(by.className('name-textfield'));
        expect(error.getText()).toContain('muito longo');
      });
    });

    describe('cnpj', function() {
      it('valida preenchimento', function () {
        element(by.className('save-button')).click();
        var error = element(by.className('cnpj-textfield'));
        expect(error.getText()).toContain('obrigatório');
      });

      it('valida valido', function () {
        var cnpj = element(by.model('vm.company.cnpj'));
        cnpj.sendKeys('00000000000000');
        element(by.className('save-button')).click();
        var error = element(by.className('cnpj-textfield'));
        expect(error.getText()).toContain('inválido');
      });
    });

    describe('email', function () {
      it('valida preenchimento', function () {
        element(by.className('save-button')).click();
        var error = element(by.className('email-textfield'));
        expect(error.getText()).toContain('obrigatório');
      });

      it('valida menor ou igual a 255', function () {
        var email = element(by.model('vm.company.email'));
        email.sendKeys(
          new Array(258).join('i')
        );
        element(by.className('save-button')).click();
        var error = element(by.className('email-textfield'));
        expect(error.getText()).toContain('muito longo');
      });
    });

    describe('regime tributario', function () {
      it('inicia com simples nacional', function () {
        expect(
          element(
            by.model('vm.company.taxRegime')
          ).element(
              by.css('option:checked')
            ).getAttribute('value')
        ).toEqual('SIMPLES_NACIONAL');
      });
    });

    describe('anexos', function() {
      it('exibe quando simples nacional', function() {
        expect(
          element(by.className('taxAnnexes-div')).isDisplayed()
        ).toBe(true);
      });

      it('obrigatório quando simples nacional', function() {
        element(by.className('save-button')).click();
        var error = element(by.className('taxAnnexes-div'));
        expect(error.getText()).toContain('Selecione ao menos um anexo');
      });

      it('esconde quando lucro presumido', function() {
        selectDropdownOption(
          element(by.model('vm.company.taxRegime')),
          'LUCRO_PRESUMIDO'
        );

        expect(
          element(by.className('taxAnnexes-div')).isDisplayed()
        ).toBe(false);
      });
    });

});

var selectDropdownOption = function ( element, value ) {
  if (value){
    element.$('[value=\''+ value +'\']').click();
  }
};
