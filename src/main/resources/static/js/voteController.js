angular.module('delegados').controller('voteCtrl', ['$rootScope', '$scope', '$http', function(rc, sc, http) {
		sc.candidatos = "";
		var user = rc.credentials;
		/*http.post('get-candidates', 'ist163310')
					.success(function(data) { 
						sc.candidatos = data;
					});*/
		
		sc.candidatos = [  {name:'Ricardo Pires', username:'ist167066'}, {name:'Hugo Almeida', username:'ist166997'}, {name:'Fernando Santos', username:'ist123456'} ];
		
		sc.voted = false;
		
		sc.vote = function() {
			/*http.post('get-candidates', {voter:rc.credentials.username, voted:sc.selected} )
			.success(function(data) { 
				
			});*/
			sc.result = 'user ' + rc.credentials.username + ' voted on ' + sc.selected;
			sc.voted = true;
		}
	}
]);