"use strict";

describe('Detalhes da empresa', function () {
    beforeEach(function () {
      browser.addMockModule('httpBackendMock', httpBackendMock);
      browser.get('/');

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
                cnpj: "25681965000118",
                taxRegime: "LUCRO_PRESUMIDO",
                email: "felipepastorelima@gmail.com"
              }
            ];
            var codeOK = 200;

            $httpBackend.whenGET(/^app\//).passThrough();
            $httpBackend.whenGET(environment.apiUrl + '/company/1').respond(function () {
              return [codeOK, companies[0]];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/2').respond(function () {
              return [codeOK, companies[1]];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company').respond(function () {
              return [codeOK, companies];
            });
            $httpBackend.whenDELETE(environment.apiUrl + '/company/1').respond(function(method, url, data) {
              return [codeOK];
            });
            $httpBackend.whenDELETE(environment.apiUrl + '/company/2').respond(function(method, url, data) {
              return [codeOK];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/1/invoice?referenceDate=' + (+moment().startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, []];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/1/tax?referenceDate=' + (+moment().startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, []];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/2/invoice?referenceDate=' + (+moment().startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, []];
            });
            $httpBackend.whenGET(environment.apiUrl + '/company/2/tax?referenceDate=' + (+moment().startOf('month')) ).respond(function (method, url, data) {
              return [codeOK, []];
            });
          });
      }
    });

    describe('simples nacional', function() {
      beforeEach(function() {
        var simplesNacionalIndex = 0;
        element.all(by.className('company-list-item')).get(simplesNacionalIndex).click();
      });

      it('exibe cnpj', function() {
        expect(element(by.css('.company-details-list')).getText()).toContain('67676776000105');
      });

      it('exibe regime tributário', function() {
        expect(element(by.css('.company-details-list')).getText()).toContain('Simples Nacional');
      });

      it('exibe anexos', function() {
        expect(element(by.css('.company-details-list')).getText()).toContain('Indústria');
      });

      it('exibe email', function() {
        expect(element(by.css('.company-details-list')).getText()).toContain('felipepastorelima@gmail.com');
      });
    });

    describe('lucro presumido', function() {
      beforeEach(function() {
        var lucroPresumidoIndex = 1;
        element.all(by.className('company-list-item')).get(lucroPresumidoIndex).click();
      });

      it('exibe cnpj', function() {
        expect(element(by.css('.company-details-list')).getText()).toContain('25681965000118');
      });

      it('exibe regime tributário', function() {
        expect(element(by.css('.company-details-list')).getText()).toContain('Lucro Presumido');
      });

      it('ESCONDE anexos', function() {
        expect(element(by.css('.company-details-list')).getText()).not.toContain('Anexos');
      });

      it('exibe email', function() {
        expect(element(by.css('.company-details-list')).getText()).toContain('felipepastorelima@gmail.com');
      });

    });

    describe('exclusão', function() {
      it('redireciona para / após excluir empresa', function() {
        element.all(by.className('company-list-item')).get(0).click();
        element(by.className('delete-company-button')).click();

        var EC = protractor.ExpectedConditions;
        browser.wait(browser.driver.getCurrentUrl().then(function(actualUrl){
          return actualUrl;
        }), 5000).then(function(actualUrl){
          expect(actualUrl).toMatch(/\#\!\/+$/g);
        });
      });
    });

});
