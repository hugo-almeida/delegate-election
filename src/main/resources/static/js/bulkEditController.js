angular.module('delegados').controller('bulkCtrl', ['$rootScope', '$scope', '$http', '$log', 'bulkEdit', 'formatDate',
                                                          function(rc, sc, http, log, bulkEdit, formatDate)  {
	
	sc.applicationStart = new Date();
	sc.applicationEnd = new Date();
	sc.electionStart = new Date();
	sc.electionEnd = new Date();
		
	sc.selectedDegrees = function() {
		console.log(sc.selection);
//		console.log(rc.selection);
		return sc.selection;
	}
	
	sc.academicYear = function() {
		return rc.currentYear;
	}
	
	sc.bulkEdit = function() {
		bulkEdit.edit(sc.selection, sc.years, formatDate(sc.applicationStart), formatDate(sc.applicationEnd), formatDate(sc.electionStart), formatDate(sc.electionEnd));
	}
}]);
