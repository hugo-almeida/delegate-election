angular.module('delegados').controller('navigationCtrl', ['$rootScope', '$scope', '$http', '$log',
                                                          function($rootScope, $scope, $http, $log)  {
	
	$scope.selected = "none";
	
	$scope.roles = ['student', 'management'];
	
	if($scope.roles.indexOf('student') != -1) {
		$scope.selected = 'student';
	} else if($scope.roles.indexOf('management') != -1) {
		$scope.selected = 'management';
	}
	
	$scope.selectStudentTab = function() {
		$scope.selected = "student";
	}
	
	$scope.selectManagementTab = function() {
		$scope.selected = "management";
	}
}]);