(function() {
'use strict';

  angular
    .module('app.tax')
    .constant(
      'TaxRegime',
      {
        SIMPLES_NACIONAL: "Simples Nacional",
        LUCRO_PRESUMIDO: "Lucro Presumido",
      }
    );

})();
