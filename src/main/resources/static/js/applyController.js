angular.module('delegados').controller('applyCtrl', ['$rootScope', '$scope', '$http', '$log', function(rc, sc, http, log) {
		sc.candidatos = [];
		http.get('degrees/'+sc.$parent.degree.id+'/years/'+sc.$parent.degree.curricularYear+'/candidates')
					.success(function(data) { 
						sc.candidatos = data;
					});
		//sc.candidatos = [  {name:'Ricardo Pires', username:'ist167066'}, {name:'Hugo Almeida', username:'ist166997'}, {name:'Fernando Santos', username:'ist123456'} ];
		
		log.log(sc.$parent.degrees[0]);
		
		sc.applied = false;
		sc.apply = function() {
			http.post('degrees/'+sc.$parent.degree.id+'/years/'+sc.$parent.degree.curricularYear+'/candidates', rc.credentials.username)
			.success(function(data) { 
				sc.applied = true;
			});
			log.log(rc.credentials.name + 'applied');
			//sc.candidatos.push({name:rc.credentials.name, username:rc.credentials.username});
		};
		
		sc.unapply = function() {
			/*http.post('unapply', rc.credentials.username)
			.success(function(data) { 
			});*/
		}
		
		sc.reloadCandidates = function() {
			http.get('degrees/'+sc.$parent.degree.id+'/years/'+sc.$parent.degree.curricularYear+'/candidates')
			.success(function(data) { 
				sc.candidatos = data;
			});
		}
	}
]);
