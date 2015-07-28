angular.module('delegados').factory('history', ['$log', '$http', function(log, http){
	var history = {};
	
	var inspect = {};
	
	var candidates = {};
	
	function getHistory() {
		return history;
	}
	
	function inspectPeriod(index) {
		inspect = history.periods[index];
		log.log(inspect);
	}
	
	function getInspectedPeriod() {
		return inspect;
	}
	
	function loadCandidates() {
		/*http.get('/degrees/' + degreeId + '/years/' + year + '/candidates').success(function(data) {
			candidates = data;
		});*/
	}
	
	function loadHistory(degreeId, year) {
		http.get('degrees/' + degreeId + '/years/' + year + '/history').success(function(data) {
			history = data;
			inspectPeriod(0);
			
		});
	}
	
	return {
		getInspectedPeriod: getInspectedPeriod,
		getHistory: getHistory,
		loadHistory: loadHistory,
		inspectPeriod: inspectPeriod
	}
}]);