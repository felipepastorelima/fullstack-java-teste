(function() {
'use strict';

  angular
    .module('app.invoice')
    .service('InvoicePresenceCheckerService', InvoicePresenceCheckerService);

  /*
  * Este serviço serve apenas para armazenar se existem notas ficais
  * no período
  */
  function InvoicePresenceCheckerService() {
    this.hasInvoices = false;
  }

})();
