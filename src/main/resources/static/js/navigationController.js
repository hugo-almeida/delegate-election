angular.module('delegados').controller('navigationCtrl', ['$rootScope', '$scope', '$http', '$log',
                                                          function($rootScope, $scope, $http, $log)  {
	$rootScope.subTitle = 'Início';
	
	$http.get('user').success(function(data) {
		if (data) {
		
			$rootScope.authenticated = true;
			
			$rootScope.id = data.name;
			
			$rootScope.credentials = data;
			
			$rootScope.degrees = [];

			$http.get('students/'+$rootScope.credentials.username+'/degrees').success(function(data){
				$rootScope.degrees = data;
			});

			$rootScope.votePeriod = false;
		
		} else {
			$rootScope.authenticated = false;
		}
	}).error(function() {
		$rootScope.authenticated = false;
	});
	
	$scope.specialLogin = function(){		//debug

		$http.post('get-user', $scope.special_username)
			.success(function(data) {
				$rootScope.credentials = data;
				$rootScope.id = data.name;
			});
		$rootScope.authenticated = true;
	}
	
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
	
	$scope.debugReloadDegrees = function() {
		$http.get('students/'+$rootScope.credentials.username+'/degrees').success(function(data){
			$rootScope.degrees = data;
		});
	}
	
	$scope.debugVotePeriod = function() {
		$rootScope.votePeriod = !$rootScope.votePeriod;
	}
	
	$rootScope.debug = true;
	
	$rootScope.toggleDebug = function() {
		$rootScope.debug = !$rootScope.debug;
	}
}]);

