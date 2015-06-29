angular.module('delegados').controller('navigationCtrl', ['$rootScope', '$scope', '$http', '$log',
                                                          function($rootScope, $scope, $http, $log)  {
	$http.get('user').success(function(data) {
		if (data) {
			$rootScope.authenticated = true;
			$rootScope.id = data.name;
			$rootScope.credentials = data;
			$rootScope.degrees = [];
			for (index in data.roles) {
				//$log.log(data.roles[index]);
				if(data.roles[index].type=='STUDENT') {
					$rootScope.degrees.push(data.roles[index]);
				//	$log.log('pushed');
				}
			}
			$rootScope.appPeriod = false;
			$rootScope.votePeriod = false;
			//$log.log($rootScope.degrees[0].registrations[0].name)
		} else {
			$rootScope.authenticated = false;
		}
	}).error(function() {
		$rootScope.authenticated = false;
	});

	//$scope.credentials = {};

	$scope.logout = function() {
		$http.post('logout', {}).success(function() {
			$rootScope.authenticated = false;
			$rootScope.credentials = {};
			//$location.path("/");
		}).error(function(data) {
			console.log("Logout failed")
			$rootScope.authenticated = false;
		});
	};
	
	$scope.debugApplyPeriod = function() {
		$rootScope.appPeriod = !$rootScope.appPeriod;
	}
	
	$scope.debugVotePeriod = function() {
		$rootScope.votePeriod = !$rootScope.votePeriod;
	}
	
	$rootScope.debug = true;
}]);

