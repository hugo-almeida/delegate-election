angular.module('delegados').factory('periodEdit', ['$log', '$http', function(log, http){
	var selectedPeriodType = 'none';
	
	var selectedPeriodOperation = 'none';
	
	var selectedDegree = {};
	
	var selectedYear = {};
	
	function getSelectedPeriodType() {
		return selectedPeriodType;
	}
	
	function getSelectedPeriodOperation() {
		return selectedPeriodOperation;
	}
	
	function getSelectedDegree() {
		return selectedDegree;
	}
	
	function getSelectedYear() {
		return selectedYear;
	}
	
	function setSelectedPeriodType(type) {
		selectedPeriodType = type;
	};
	
	function setSelectedPeriodOperation(operation) {
		selectedPeriodOperation = operation;
	};
	
	function setSelectedDegree(degree) {
		selectedDegree = degree;
	};
	
	function setSelectedYear(year) {
		selectedYear = year;
	};
	
	function edit(start, end) {
		var dates = {start:start, end:end};
		
		log.log('start: ' + start + ' end ' + end);
		if(selectedPeriodOperation == 'Estender') {
			log.log('editing ' + selectedPeriodType);
			
			var requestUrl = 'periods/';
			if(selectedPeriodType == 'Votação') {
				log.log('vote');
			}
			else if(selectedPeriodType == 'Candidatura') {
				log.log('apply');
			}
			
			http.put(requestUrl, dates).success(function(data) {
				log.log('success edit apply');
			});
		}
		else if(selectedPeriodOperation == 'Criar') {
			log.log(selectedDegree);
			
			var changes = {degreeId:selectedDegree.degreeId, degreeYear:selectedYear.degreeYear, periodType:'', start:start, end:end};
			
			log.log('creating ' + selectedPeriodType);
			if(selectedPeriodType == 'Votação') {
				log.log('vote');
				log.log(dates);
				changes.periodType = 'ELECTION';
			}
			else if(selectedPeriodType == 'Candidatura') {
				log.log('apply');
				log.log(dates);
				changes.periodType = 'APPLICATION'
			}
			
			http.post('periods', changes).success(function(data) {
				log.log('success edit apply');
			});
		}
			
	};
	
	return {
		getSelectedPeriodType: getSelectedPeriodType,
		getSelectedPeriodOperation: getSelectedPeriodOperation,
		getSelectedDegree: getSelectedDegree,
		getSelectedYear: getSelectedYear,
		setSelectedPeriodType: setSelectedPeriodType,
		setSelectedPeriodOperation: setSelectedPeriodOperation,
		setSelectedDegree: setSelectedDegree,
		setSelectedYear: setSelectedYear,
		edit: edit
	};
}]);