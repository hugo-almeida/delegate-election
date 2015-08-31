angular.module('delegados').controller('bulkCtrl', ['$rootScope', '$scope', '$http', '$log', 'bulkEdit', 'formatDate',
                                                          function(rc, sc, http, log, bulkEdit, formatDate)  {
	
	sc.applicationStart = new Date();
	sc.applicationEnd = new Date();
	sc.electionStart = new Date();
	sc.electionEnd = new Date();
	
	sc.years = [false, false, false, false, false];
		
	sc.academicYear = function() {
		log.log(bulkEdit.getYear());
		return bulkEdit.getYear();
	}
	
	sc.bulkEdit = function() {
		bulkEdit.edit(sc.years, formatDate(sc.applicationStart), formatDate(sc.applicationEnd), formatDate(sc.electionStart), formatDate(sc.electionEnd));
	}
}]);
