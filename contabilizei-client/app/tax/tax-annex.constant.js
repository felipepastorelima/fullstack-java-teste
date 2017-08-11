(function() {
'use strict';

  angular
    .module('app.tax')
    .constant(
      'TaxAnnex',
      {
        COMERCIO: "Comércio",
        INDUSTRIA: "Indústria",
        PRESTACAO_DE_SERVICOS: "Prestação de Serviços"
      }
    );

})();
