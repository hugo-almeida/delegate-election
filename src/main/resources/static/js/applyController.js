angular.module('delegados').controller('applyCtrl', ['$rootScope', '$scope', '$http', '$log', function(rc, sc, http, log) {
		sc.candidatos = [];
		http.post('get-candidates', rc.credentials.username)
					.success(function(data) { 
						sc.candidatos = data;
					});
		//sc.candidatos = [  {name:'Ricardo Pires', username:'ist167066'}, {name:'Hugo Almeida', username:'ist166997'}, {name:'Fernando Santos', username:'ist123456'} ];
		
		sc.applied = false;
		sc.apply = function() {
			log.log(rc.credentials.username);
			http.post('apply', rc.credentials.username)
			.success(function(data) { 
				sc.applied = true;
			});
			sc.candidatos.push({name:rc.credentials.name, username:rc.credentials.username});
		};
		
		sc.debugCandidates = function() {
			http.post('get-candidates', rc.credentials.username)
			.success(function(data) { 
				sc.candidatos = data;
		});
		}
	}
]);
