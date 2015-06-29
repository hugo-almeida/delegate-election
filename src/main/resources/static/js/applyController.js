angular.module('delegados').controller('applyCtrl', ['$rootScope', '$scope', '$http', function(rc, sc, http) {
		sc.candidatos = "";
		var user = rc.credentials;
		/*http.post('get-candidates', 'ist163310')
					.success(function(data) { 
						sc.candidatos = data;
					});*/
		sc.candidatos = [  {name:'Ricardo Pires', username:'ist167066'}, {name:'Hugo Almeida', username:'ist166997'}, {name:'Fernando Santos', username:'ist123456'} ];
		
		sc.applied = false;
		sc.apply = function() {
			/*http.post('apply', 'ist163310')
			.success(function(data) { 
				sc.applied = true;
			});*/
			sc.candidatos.push({name:rc.credentials.name, username:rc.credentials.username});
			sc.applied = true;
		};
	}
]);
