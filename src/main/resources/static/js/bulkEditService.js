angular.module('delegados').factory('bulkEdit', ['$rootScope', '$log', '$http', function(rc, log, http) {
	var selectedPeriodOperation = 'none';
	
	var selectedDegree = {};
	
	var selectedYear = {};
	
	function getSelectedDegrees() {
		return rc.selection;
	}
	
	function getSelectedYear() {
		return selectedYear;
	}
	
	function setSelectedDegree(degree) {
		selectedDegree = degree;
	};
	
	function setSelectedYear(year) {
		selectedYear = year;
	};
	
	function edit(degrees, years, applicationStart, applicationEnd, electionStart, electionEnd) {
		var request = [];
		var dates = {electionPeriodStart:electionStart, 
					 electionPeriodEnd:electionEnd,
					 applicationPeriodStart:applicationStart,
					 applicationPeriodEnd:applicationEnd};
		
		console.log(rc.degrees);
		
		rc.degrees.forEach(function(degree) {
			console.log(degrees);
			console.log(degree.degree);
			if (degrees.indexOf(degree.degree) > -1) {
				var degreeJson = {};
				degreeJson.degreeId = degree.degreeId;
				var yearsJson = [];
				for (var i = 1; i <= 5; i++) {
					if (years[i]) {
						var yearJson = {};
						yearJson.degreeYear = i;
						yearJson.electionPeriod = {};
						yearJson.applicationPeriod = {};
						if (electionEnd && electionStart) {
							yearJson.electionPeriod.electionPeriodStart = electionStart;
							yearJson.electionPeriod.electionPeriodEnd = electionEnd;
						}
						if (applicationStart && applicationEnd) {
							yearJson.applicationPeriod.applicationPeriodStart = applicationStart;
							yearJson.applicationPeriod.applicationPeriodEnd = applicationEnd;
						}
						yearsJson.push(yearJson);
					}
				}
				degreeJson.years = yearsJson;
				request.push(degreeJson);
			}
		});
		console.log(request);
//		log.log('start: ' + start + ' end ' + end);
		var requestUrl = 'periods';
		http.put(requestUrl, request).success(function(data) {
			log.log('success edit apply');
			http.get('periods').success(function(data) {
				rc.degrees = data;
				rc.filteredDegrees = rc.degrees;
			});
		});
	};
	
	return {
		getSelectedDegrees: getSelectedDegrees,
		getSelectedYear: getSelectedYear,
		setSelectedDegree: setSelectedDegree,
		setSelectedYear: setSelectedYear,
		edit: edit
	};
}]);