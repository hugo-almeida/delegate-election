angular.module('delegados').controller('historyCtrl', ['$rootScope', '$scope', '$http', '$log', 'history',
                                                          function(rc, sc, http, log, history)  {
	
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
		log.log(history.getCurrentElection());
		return history.getCurrentElection();
	}
	
	sc.loadCandidates = function() {
		if(getCurrentApplication().state == 'presente') {
			candidates = history.loadCandidates(getCurrentApplication().applicationPeriodId);
		}
		else if(getCurrentElection().state == 'presente') {
			candidates = history.loadCandidates(getCurrentElection().electionPeriodId);
		}
	}
}]);