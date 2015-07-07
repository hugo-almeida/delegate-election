angular.module('delegados').controller('applyCtrl', ['$rootScope', '$scope', '$http', '$log', function(rc, sc, http, log) {
		sc.applied = false;
	
		sc.candidatos = [];
		http.get('degrees/'+sc.$parent.degree.id+'/years/'+sc.$parent.degree.curricularYear+'/candidates')
					.success(function(data) { 
						sc.candidatos = data;
						for(var i = 0; i < sc.candidatos.length; i++) {
						    if (sc.candidatos[i].username == rc.credentials.username) {
						        sc.applied = true;
						        break;
						    }
						}
					});
		
		sc.apply = function() {
			http.post('degrees/'+sc.$parent.degree.id+'/years/'+sc.$parent.degree.curricularYear+'/candidates', rc.credentials.username)
			.success(function(data) { 
				sc.applied = true;
				http.get('degrees/'+sc.$parent.degree.id+'/years/'+sc.$parent.degree.curricularYear+'/candidates')
				.success(function(data) { 
					sc.candidatos = data;
					sc.feedback = true;
					log.log(sc.feedback);
				});
			});
		};
		
		sc.unapply = function() {
			http.delete('degrees/'+sc.$parent.degree.id+'/years/'+sc.$parent.degree.curricularYear+'/candidates/'+rc.credentials.username)
			.success(function(data) { 
				sc.applied = false;
				http.get('degrees/'+sc.$parent.degree.id+'/years/'+sc.$parent.degree.curricularYear+'/candidates')
				.success(function(data) { 
					sc.candidatos = data;
					sc.feedback = false;
					log.log(sc.feedback);
				});
			});
		}
		
		sc.reloadCandidates = function() {
			http.get('degrees/'+sc.$parent.degree.id+'/years/'+sc.$parent.degree.curricularYear+'/candidates')
			.success(function(data) { 
				sc.candidatos = data;
			});
		}
	}
]);
