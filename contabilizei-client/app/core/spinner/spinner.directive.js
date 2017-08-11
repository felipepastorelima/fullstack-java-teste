(function() {
  'use strict';

  angular
    .module('app.core')
    .directive('appSpinner', appSpinner);

  appSpinner.$inject = ['$timeout'];
  function appSpinner($timeout) {
    var directive = {
        restrict: 'EA',
        templateUrl: 'app/core/spinner/spinner.html',
        link: function($scope, $element, attrs, ctrl) {
          $timeout(function() {
            componentHandler.upgradeAllRegistered();
          });
        }
    };

    return directive;
  }
})();
