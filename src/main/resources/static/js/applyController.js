angular.module('delegados').controller('applyCtrl', ['$rootScope', '$scope', '$http', '$log', function(rc, sc, http, log) {
		
		sc.apply = function() {
			log.log('bye');
			http.post('degrees/'+rc.degree.id+'/years/'+rc.degree.curricularYear+'/candidates', rc.credentials.username)
			.success(function(data) { 
				rc.applied = true;
				http.get('degrees/'+rc.degree.id+'/years/'+rc.degree.curricularYear+'/candidates')
				.success(function(data) { 
					rc.candidatos = data;
					sc.feedback = true;
					log.log(sc.feedback);
				});
			});
		};
		
		sc.unapply = function() {
			log.log('hi');
			http.delete('degrees/'+rc.degree.id+'/years/'+rc.degree.curricularYear+'/candidates/'+rc.credentials.username)
			.success(function(data) { 
				rc.applied = false;
				http.get('degrees/'+rc.degree.id+'/years/'+rc.degree.curricularYear+'/candidates')
				.success(function(data) { 
					rc.candidatos = data;
					sc.feedback = false;
					log.log(sc.feedback);
				});
			});
		};
}]);
