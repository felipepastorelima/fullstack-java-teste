(function() {
  'use strict';

  angular
    .module('app.core')
    .directive('appPastOrEqualDate', appPastOrEqualDate);

  function appPastOrEqualDate() {
    return {
      require: 'ngModel',
      link: function(scope, elm, attrs, ctrl) {
        ctrl.$validators.pastOrEqualDate = function(modelValue, viewValue) {
          if (ctrl.$isEmpty(modelValue)) {
            return true;
          }

          if (!moment(viewValue, 'DD/MM/YYYY').isValid()) return true;

          return moment(viewValue, 'DD/MM/YYYY').startOf('day').isSameOrBefore(
            moment().startOf('day')
          );
        };
      }
    };
  }

})();
