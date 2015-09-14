angular.module('delegados').controller('detailsCtrl', ['$rootScope', '$scope', '$http', '$log', 'history', 
                                                          function(rc, sc, http, log, history)  {
	
	sc.loaded = false;
	
	sc.students = [];
	
	sc.add = false;
	
	sc.toggleAdd = function() {
		sc.add = !sc.add;
	} 
	
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
		log.log(history.getCurrentApplication());
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
	
	sc.loadStudents = function(query) {
		if(query != null) {
			if(query.length >= 2){
				/*http.get('https://fenix.tecnico.ulisboa.pt/api/bennu-core/users/find?begins='+query)
				.success(function(data) { 
					sc.students = data;
				});*/
				http.get('degrees/2761663971606/years/1/students?begins='+query)	//for testing purposes only
				.success(function(data) { 
					sc.students = data;
				});
			}
			else {
				sc.students = [];
			}
		}
	}
	
	sc.addCandidate = function(istid) {
		log.log(sc.getCurrentApplication());
		http.post('periods/' + sc.getCurrentApplication().applicationPeriodId + '/candidates/' + istid).success(function(data) {
			log.log(data);
		}).then(function(data) {
			history.loadCandidates();
			sc.query = '';
			sc.students = [];
		});
	}
	
	sc.colSpan = function() {
		if(getCurrentApplication().state && getCurrentApplication().state =='presente') {
			return 1;
		}
		else if (getCurrentElection().state && getCurrentElection().state =='presente') {
			return 3;
		}
	}
}]);