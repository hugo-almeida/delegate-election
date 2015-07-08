angular.module('delegados').controller('navigationCtrl', ['$rootScope', '$scope', '$http', '$log',
                                                          function($rootScope, $scope, $http, $log)  {
	
	/***
	 * INIT
	 */
	$http.get('user').success(function(data) {
		if (data) {
		
			$rootScope.degree = null;
			
			$rootScope.authenticated = true;
			
			$rootScope.id = data.name;
			
			$rootScope.credentials = data;
			
			$rootScope.degrees = [];
			
			$rootScope.reloadDegrees();
			
			$rootScope.votePeriod = false;
		
		} else {
			$rootScope.authenticated = false;
		}
	}).error(function() {
		$rootScope.authenticated = false;
	});
	
	$scope.subTitle = 'Início'
		
	$rootScope.debug = true;
	
	$rootScope.voted = false;
	
	$rootScope.applied = false;

	/***
	 * LOGOUT
	 */
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
	
	/***
	 * DEGREES
	 */
	$rootScope.reloadDegrees = function() {
		$http.get('students/'+$rootScope.credentials.username+'/degrees').success(function(data){
			$rootScope.degrees = data;
			
			if($rootScope.degrees.length == 1) {
				$scope.selection = 0;
				$scope.setDegree();
			}
		});
	}
	
	$rootScope.reloadCandidates = function() {
		$http.get('degrees/'+$rootScope.degree.id+'/years/'+$rootScope.degree.curricularYear+'/candidates')
		.success(function(data) { 
			$rootScope.candidatos = data;
			/*for(var i = 0; i < $rootScope.candidatos.length; i++) {
			    if ($rootScope.candidatos[i].username == $rootScope.credentials.username) {
			        break;
			    }
			}*/
		});
		
		$http.get('degrees/'+$rootScope.degree.id+'/years/'+$rootScope.degree.curricularYear+'/candidates/'+$rootScope.credentials.username)
		.success(function(data) { 
			if(data != '') {
				$rootScope.applied = true;
			}
		});
	}
	
	$scope.setDegree = function() {
		$rootScope.degree = $rootScope.degrees[$scope.selection];
		$rootScope.reloadCandidates();
		$scope.setSubtitle();
	}
	
	$scope.setSubtitle = function() {
		if($rootScope.degree.currentPeriod.type == 'APPLICATION') {
			$scope.subTitle = 'Candidatura';
		}
		else if($rootScope.degree.currentPeriod.type == 'ELECTION')  {
			$scope.subTitle = 'Votação';
		}
	}
	
	/***
	 * DEBUG
	 */
	$scope.debugVotePeriod = function() {
		$rootScope.votePeriod = !$rootScope.votePeriod;
	}
	
	$rootScope.toggleDebug = function() {
		$rootScope.debug = !$rootScope.debug;
	}
	
	
	$scope.specialLogin = function(){		//debug

		$http.post('get-user', $scope.special_username)
			.success(function(data) {
				$rootScope.credentials = data;
				$rootScope.id = data.name;
			});
		$rootScope.authenticated = true;
	}
	
}]);

