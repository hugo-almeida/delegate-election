angular.module('delegados').controller('navigationCtrl', ['$rootScope', '$scope', '$http',
                                                          function($rootScope, $scope, $http)  {
	$http.get('user').success(function(data) {
		if (data) {
			$rootScope.authenticated = true;
			$rootScope.id = data.name;
			$rootScope.credentials = data;
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
			//$location.path("/");
		}).error(function(data) {
			console.log("Logout failed")
			$rootScope.authenticated = false;
		});
	};
}]);
