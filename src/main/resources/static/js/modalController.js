angular.module('delegados').controller('modalCtrl', ['$rootScope', '$scope', '$http', '$log', 'periodEdit', 'formatDate',
                                                          function(rc, sc, http, log, periodEdit, formatDate)  {
		sc.start = new Date();
		
		sc.end = new Date();
	
		sc.selectedPeriodType = function() {
			return periodEdit.getSelectedPeriodType();
		}
		
		sc.selectedPeriodOperation = function() {
			return periodEdit.getSelectedPeriodOperation();
		}
		
		sc.selectedDegree = function() {
			return periodEdit.getSelectedDegree();
		}
		
		sc.selectedYear = function() {
			return periodEdit.getSelectedYear();
		}
		
		sc.edit = function() {
			periodEdit.edit(formatDate(sc.start), formatDate(sc.end));
		}
		
}]);