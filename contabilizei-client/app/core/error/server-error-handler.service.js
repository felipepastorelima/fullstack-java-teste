(function () {
  'use strict';

  angular
    .module('app.core')
    .service('ServerErrorHandler', ServerErrorHandler);

  ServerErrorHandler.$inject = ['$state', 'toast'];
  function ServerErrorHandler($state, toast) {
    this.handle = handle;
    this.handleErrorFromStateChange = handleErrorFromStateChange;

    ////////////////

    function handle(error) {
      var badRequestStatus = 400;

      if (error.status === badRequestStatus) {
        toast.error(error.data.message);
        return;
      }

      console.error(error);
      $state.go('error');
    }

    function handleErrorFromStateChange(error) {
      console.error(error);
      $state.go('error');
    }

  }
})();
