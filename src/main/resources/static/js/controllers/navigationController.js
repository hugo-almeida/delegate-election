angular.module('delegados').controller('navigationCtrl', ['$rootScope', '$scope', '$http', '$log',
                                                          function($rootScope, $scope, $http, $log)  {
	
	$rootScope.imgURL = "https://fenix.tecnico.ulisboa.pt/user/photo/";
	
	
	$scope.selected = "none";
	
	$scope.roles = [];
	
	$http.get('roles').success(function(data) {
		$scope.roles = data;
		if($scope.roles.aluno != false) {
			$scope.selected = 'student';
		} else if($scope.roles.pedagogico != false) {
			$scope.selected = 'management';
		}
		$log.log($scope.selected);
	});
	
	$scope.selectStudentTab = function() {
		$scope.selected = "student";
	}
	
	$scope.selectManagementTab = function() {
		$scope.selected = "management";
	}
}]);