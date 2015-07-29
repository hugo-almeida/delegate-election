angular.module('delegados').factory('history', ['$log', '$http', function(log, http){
	var degree = {}
	
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
	
	function setDegree(d) {
		degree = d;
	}
	
	function getCurrentApplication() {
		return degree.applicationPeriod;
	}
	
	function getCurrentElection() {
		return degree.electionPeriod;
	}
	
	function loadCandidates(id) {
		http.get('periods/' + id + '/candidates').success(function(data) {
			candidates = data;
			return candidates;
		});
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
		getCurrentApplication: getCurrentApplication,
		getCurrentElection: getCurrentElection,
		setDegree: setDegree,
		loadHistory: loadHistory,
		inspectPeriod: inspectPeriod
	}
}]);