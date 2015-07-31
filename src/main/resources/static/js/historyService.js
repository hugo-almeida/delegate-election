angular.module('delegados').factory('history', ['$log', '$http', function(log, http){
	var degree = {}
	
	var history = {};
	
	var inspect = {};
	
	var candidates = [];
	
	function getHistory() {
		return history;
	}
	
	function inspectPeriod(index) {
		inspect = history.periods[index];
		loadCandidates(index);
	}
	
	function getInspectedPeriod() {
		return inspect;
	}
	
	function getInspectedPeriodCandidates() {
		return candidates;
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
			if(!candidates == 'No Period with that Id') {
				candidates = data;
				log.log(candidates);
				return data;
			}
		});
	}
	
	function loadHistory(degreeId, year) {
		http.get('degrees/' + degreeId + '/years/' + year + '/history').success(function(data) {
			history = data;
		});
	}
	
	return {
		getInspectedPeriod: getInspectedPeriod,
		getInspectedPeriodCandidates: getInspectedPeriodCandidates,
		getHistory: getHistory,
		getCurrentApplication: getCurrentApplication,
		getCurrentElection: getCurrentElection,
		setDegree: setDegree,
		loadHistory: loadHistory,
		loadCandidates: loadCandidates,
		inspectPeriod: inspectPeriod
	}
}]);