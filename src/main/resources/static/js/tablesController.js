angular.module('delegados').controller('tablesCtrl', ['$rootScope', '$scope', '$http', '$log', 'periodEdit',
                                                          function(rc, sc, http, log, periodEdit)  {
	
	sc.debug = true;
	
	sc.details = false;
	
	sc.loaded = false;
	
	sc.allSelected = false;
	
	sc.active = 'Delegados';
	
	sc.inspectDegree = {};
	
	sc.inspectDegreeYear = 0;
	
	sc.loadDegrees = function() {
		http.get('periods').success(function(data){
			sc.degrees = data;
			sc.loaded = true;
		});
	};
	
	sc.loadDegrees();
	
	sc.selection = [];
	
	sc.toggleSelection = function(degree) {
		var index = sc.selection.indexOf(degree);
		
		if(index > -1) {
			sc.selection.splice(index, 1);
		}
		else {
			sc.selection.push(degree);
		}
	};
	
	sc.toggleSelectAll = function() {
		log.log('hi');
		if(!sc.allSelected) {
			log.log('toggling off')
			for(degree in sc.degrees) {
				log.log(sc.degrees[degree])
				var name = sc.degrees[degree].degree;
				var index = sc.selection.indexOf(name);
				
				if(index <= -1) {
					sc.selection.push(name);
				}
			}
			sc.allSelected = true;
		}
		else {
			log.log('toggling on')
			for(degree in sc.degrees) {
				log.log(sc.degrees[degree])
				var name = sc.degrees[degree].degree;
				var index = sc.selection.indexOf(name);
				
				if(index > -1) {
					sc.selection.splice(index, 1);
				}
			}
			sc.allSelected = false;
		}
	}
	
	sc.showDetails = function(degree, year) {
		sc.inspectDegree = degree;
		sc.inspectDegreeYear = year;
		log.log(degree);
		log.log(year);
		http.get('degrees/' + sc.inspectDegree.degreeId + '/years/' + sc.inspectDegree.years[sc.inspectDegreeYear].degreeYear + '/candidates')
		.success(function(data) {
			sc.candidates = data;
		});	
		sc.details = true;
	}
	
	sc.hideDetails = function() {
		sc.details = false;
	}
	
	sc.isEmpty = function (obj) {
	    for (var i in obj) if (obj.hasOwnProperty(i)) return false;
	    return true;
	}
	
	sc.setSelectedRow = function(index) {
		sc.selectedRow = index;
	}
	
	sc.setSelectedPeriodType = function(type) {
		periodEdit.setSelectedPeriodType(type);
	}
	
	sc.setSelectedPeriodOperation = function(operation) {
		periodEdit.setSelectedPeriodOperation(operation);
	}
	
	sc.setSelectedDegree = function(degree){
		periodEdit.setSelectedDegree(degree);
	}
	
	sc.setSelectedYear = function(year) {
		periodEdit.setSelectedYear(year);
	}
	
	/***
	 * Debug
	 */
	
	sc.toggleDebug = function() {
		sc.debug = !sc.debug;
	}
}]);