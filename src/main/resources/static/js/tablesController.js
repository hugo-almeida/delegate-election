angular.module('delegados').controller('tablesCtrl', ['$rootScope', '$scope', '$http', '$log', 'periodEdit', 'history',
                                                          function(rc, sc, http, log, periodEdit, history)  {
	
	sc.details = false;
	
	sc.loaded = false;
	
	sc.allSelected = false;
	
	sc.active = 'Delegados';

	sc.filteredDegrees = [];

	sc.loadDegrees = function() {
		http.get('periods').success(function(data){
			sc.degrees = data;
			sc.loaded = true;
			sc.filteredDegrees = sc.degrees;
		});
	};
	
	sc.loadDegrees();
	
	sc.selection = [];

	function isLicBol(value) {
		return value.degreeType == 'Licenciatura Bolonha';
	}
	
	function isMesBol(value) {
		return value.degreeType == 'Mestrado Bolonha';
	}
	
	function isMesInt(value) {
		return value.degreeType == 'Mestrado Integrado';
	}
	
	sc.filterDegrees = function(filter) {
		sc.selection = [];
		sc.allSelected = false;
		switch(filter) {
		case 'Todos':
			sc.filteredDegrees = sc.degrees;
			break;
		case 'Licenciatura Bolonha':
			sc.filteredDegrees = sc.degrees.filter(isLicBol);
			break;
		case 'Mestrado Bolonha':
			sc.filteredDegrees = sc.degrees.filter(isMesBol);
			break;
		case 'Mestrado Integrado':
			sc.filteredDegrees = sc.degrees.filter(isMesInt);
			break;
		}
	}
	
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
			for(degree in sc.filteredDegrees) {
				var name = sc.filteredDegrees[degree].degree;
				var index = sc.selection.indexOf(name);
				
				if(index <= -1) {
					sc.selection.push(name);
				}
			}
			sc.allSelected = true;
		}
		else {
			log.log('toggling on')
			for(degree in sc.filteredDegrees) {
				var name = sc.filteredDegrees[degree].degree;
				var index = sc.selection.indexOf(name);
				
				if(index > -1) {
					sc.selection.splice(index, 1);
				}
			}
			sc.allSelected = false;
		}
	}
	
	sc.showDetails = function(degree, year) {
		log.log(degree);
		history.loadHistory(degree.degreeId, year);
		sc.details = true;
	}
	
	sc.hideDetails = function() {
		sc.details = false;
	}
	
	sc.isEmpty = function (obj) {
	    for (var i in obj) if (obj.hasOwnProperty(i)) return false;
	    return true;
	}
	
	sc.setSelectionDetails = function(type, operation, degree, year) {
		periodEdit.setSelectedPeriodType(type);
		periodEdit.setSelectedPeriodOperation(operation);
		periodEdit.setSelectedDegree(degree);
		periodEdit.setSelectedYear(year);
	}
	
}]);