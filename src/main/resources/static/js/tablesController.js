angular.module('delegados').controller('tablesCtrl', ['$rootScope', '$scope', '$http', '$log', 'periodEdit', 'history',
                                                          function(rc, sc, http, log, periodEdit, history)  {
	
	sc.details = false;
	
	sc.loaded = false;
	
	sc.allSelected = false;
	
	sc.active = 'Delegados';

	rc.filteredDegrees = [];

	rc.currentYear = '';
	
	sc.loadDegrees = function() {
		http.get('periods').success(function(data){
			rc.degrees = data;
			sc.loaded = true;
			rc.filteredDegrees = rc.degrees;
			rc.currentYear = rc.degrees[0].academicYear;
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
			rc.filteredDegrees = rc.degrees;
			break;
		case 'Licenciatura Bolonha':
			rc.filteredDegrees = rc.degrees.filter(isLicBol);
			break;
		case 'Mestrado Bolonha':
			rc.filteredDegrees = rc.degrees.filter(isMesBol);
			break;
		case 'Mestrado Integrado':
			rc.filteredDegrees = rc.degrees.filter(isMesInt);
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
		if(!sc.allSelected) {
			for(degree in rc.filteredDegrees) {
				var name = rc.filteredDegrees[degree].degree;
				var index = sc.selection.indexOf(name);
				
				if(index <= -1) {
					sc.selection.push(name);
				}
			}
			sc.allSelected = true;
		}
		else {
			for(degree in rc.filteredDegrees) {
				var name = rc.filteredDegrees[degree].degree;
				var index = sc.selection.indexOf(name);
				
				if(index > -1) {
					sc.selection.splice(index, 1);
				}
			}
			sc.allSelected = false;
		}
	}
	
	sc.showDetails = function(degree, year) {
		history.loadHistory(degree.degreeId, year);
		history.setDegree(degree.years[year-1]);
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