angular.module('delegados').factory('history', ['$log', '$http', function(log, http){
	var degree = {}
	
	var history = {};
	
	var candidates = [];
	
	var inspect = {};
	
	var inspectCandidates = [];
	
	function getHistory() {
		return history;
	}
	
	function inspectPeriod(index) {
		inspect = history.periods[index];
		log.log(history.periods[index]);
		log.log(inspect);
		loadInspectCandidates(inspect.periodId);
	}
	
	function getInspectedPeriod() {
		return inspect;
	}
	
	function getInspectedPeriodCandidates() {
		return inspectCandidates;
	}
	
	function getCandidates() {
		return candidates;
	}
	
	function setDegree(d) {
		degree = d;
		loadCandidates();
	}
	
	function getCurrentApplication() {
		return degree.applicationPeriod;
	}
	
	function getCurrentElection() {
		return degree.electionPeriod;
	}
	
	function loadCandidates(){
		log.log(degree);
		if(degree.applicationPeriod != null && degree.applicationPeriod.state == 'presente') {
			http.get('periods/' + degree.applicationPeriod.applicationPeriodId + '/candidates').success(function(data) {
				if(!(candidates == 'No Period with that Id')) {
					candidates = data;
				}
			});
		}
		else if(degree.electionPeriod != null && degree.electionPeriod.state == 'presente') {
			http.get('periods/' + degree.electionPeriod.electionPeriodId + '/candidates').success(function(data) {
				if(!(candidates == 'No Period with that Id')) {
					candidates = data;
				}
			});
		}
	}
	
	function loadInspectCandidates(id) {
		http.get('periods/' + id + '/candidates').success(function(data) {
			if(!(candidates == 'No Period with that Id')) {
				inspectCandidates = data;
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
		getCandidates: getCandidates,
		getInspectedPeriod: getInspectedPeriod,
		getInspectedPeriodCandidates: getInspectedPeriodCandidates,
		getHistory: getHistory,
		getCurrentApplication: getCurrentApplication,
		getCurrentElection: getCurrentElection,
		setDegree: setDegree,
		loadHistory: loadHistory,
		loadCandidates: loadCandidates,
		loadInspectCandidates: loadInspectCandidates,
		inspectPeriod: inspectPeriod
	}
}]);