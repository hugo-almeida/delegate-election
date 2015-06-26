angular.module('delegados').controller('votar', ['$rootScope', '$scope', '$http', function(rc, sc, http) {
		sc.candidatos = "";
		var user = rc.credentials;
		http.post('get-candidates', 'ist163310')
					.success(function(data) { 
						sc.candidatos = data;
					});
	}

]);
