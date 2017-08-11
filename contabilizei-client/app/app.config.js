(function () {
  'use strict';

  angular
    .module('app')
    .run(configureStateError)
    .run(configureMaterialDesignLite)
    .run(configureMoment)
    .run(configureNumeral);

  configureStateError.$inject = ['$rootScope', 'ServerErrorHandler'];
  function configureStateError($rootScope, ServerErrorHandler) {
    $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error){
      event.preventDefault();
      ServerErrorHandler.handleErrorFromStateChange(error);
    });
  }

  configureMaterialDesignLite.$inject = ['$rootScope', '$timeout'];
  function configureMaterialDesignLite($rootScope, $timeout) {
    $rootScope.$on('$viewContentLoaded', function() {
      $timeout(function() {
        componentHandler.upgradeAllRegistered();
        // Só exibe o conteúdo quando material design estiver pronto
        $(".mdl-layout").show();
      });
    });
  }

  function configureMoment() {
    moment.locale('pt-br');
  }

  function configureNumeral() {
    numeral.locale('pt-br');
  }

})();
