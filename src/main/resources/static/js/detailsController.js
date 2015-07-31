angular.module('delegados').controller('detailsCtrl', ['$rootScope', '$scope', '$http', '$log', 'history',
                                                          function(rc, sc, http, log, history)  {
	
	sc.loaded = false;
	
	sc.candidates = [];
	
	sc.history = function () {
		return history.getHistory();
	};
	
	sc.inspectedPeriod = function() {
		return history.getInspectedPeriod();
	};
	
	sc.inspectPeriod = function(index) {
		history.inspectPeriod(index);
	}
	
	sc.getCurrentApplication = function()  {
		return history.getCurrentApplication();
	}
	
	sc.getCurrentElection = function()  {
		return history.getCurrentElection();
	}
	
	sc.loadCandidates = function() {
		if(!sc.loaded) {
			if(sc.getCurrentApplication() != null && sc.getCurrentApplication().state == 'presente') {
				candidates = history.loadCandidates(sc.getCurrentApplication().applicationPeriodId);
				sc.loaded = true;
			}
			else if(sc.getCurrentElection() != null && sc.getCurrentElection().state == 'presente') {
				candidates = history.loadCandidates(sc.getCurrentElection().electionPeriodId);
				sc.loaded = true;
			}
		}
	}
	
	sc.assign = function(username) {
		log.log('assigning ' + username + ' as delegado stub');
	}
}]);