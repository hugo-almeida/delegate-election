angular.module('delegados').controller('detailsCtrl', ['$rootScope', '$scope', '$http', '$log', 'history',
                                                          function(rc, sc, http, log, history)  {
	
	sc.loaded = false;
	
	sc.history = function () {
		return history.getHistory();
	};
	
	sc.inspectedPeriod = function() {
		return history.getInspectedPeriod();
	};
	
	sc.inspectPeriod = function(index) {
		log.log(index);
		history.inspectPeriod(index);
	}
	
	sc.getCurrentApplication = function()  {
		return history.getCurrentApplication();
	}
	
	sc.getCurrentElection = function()  {
		return history.getCurrentElection();
	}
	
	sc.getCandidates = function() {
		return history.getCandidates();
	}
	
	sc.assign = function(username) {
		log.log('assigning ' + username + ' as delegado stub');
	}
}]);