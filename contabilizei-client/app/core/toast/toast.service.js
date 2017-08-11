(function () {
  'use strict';

  angular
    .module('app.core')
    .service('toast', toast);

  function toast() {
    this.error = error;
    this.success = success;

    ////////////////
    function error(message) {
      show('#toast_error', message);
    }

    function success(message) {
      show('#toast_success', message);
    }

    function show(toastSelector, message) {
      var $toast = $(toastSelector)[0];
      $toast.MaterialSnackbar.showSnackbar(
        {
          message: message
        }
      );
    }
  }
})();
