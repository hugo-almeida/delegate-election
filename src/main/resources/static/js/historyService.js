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
	
	function loadCandidates(){ //Isto ta um poop
		log.log(degree);
		if(degree.applicationPeriod != null && degree.applicationPeriod.state == 'presente' || degree.applicationPeriod.state == 'futuro') {
			http.get('periods/' + degree.applicationPeriod.applicationPeriodId + '/candidates').success(function(data) {
				if(!(candidates == 'No Period with that Id')) {
					candidates = data;
					var result = {usernames: []};
					for (index in candidates) {
						result.usernames.push(candidates[index].username); 
					}
					log.log(result);
					http.post('periods/' + degree.applicationPeriod.applicationPeriodId + '/selfProposed', result)
						.success(function(data) {
						log.log(data);
					});
				}
			});
		}
		else if(degree.electionPeriod != null && degree.electionPeriod.state == 'presente' || degree.electionPeriod.state == 'passado') {
			http.get('periods/' + degree.electionPeriod.electionPeriodId + '/candidates').success(function(data) {
				if(!(candidates == 'No Period with that Id')) {
					candidates = data;
					var result = {usernames: []};
					for (index in candidates) {
						result.usernames.push(candidates[index].username); 
					}
					log.log(result);
					result.usernames = result.username.substring(0, result.username.length - 1);
					http.get('periods/' + degree.applicationPeriod.applicationPeriodId + '/students/ist168268', result)
						.success(function(data) {
						log.log(data);
					});
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