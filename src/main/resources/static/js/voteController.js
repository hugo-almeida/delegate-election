angular.module('delegados').controller('voteCtrl', ['$rootScope', '$scope', '$http', '$log', function(rc, sc, http,log) {
		sc.candidatos = [];
		sc.students = [];
		http.post('get-candidates', rc.credentials.username)
					.success(function(data) { 
						sc.candidatos = data;
					});
		
		//sc.candidatos = [  {name:'Ricardo Pires', username:'ist167066'}, {name:'Hugo Almeida', username:'ist166997'}, {name:'Fernando Santos', username:'ist123456'} ];
		
		sc.voted = false;
		
		sc.reloadCandidates = function() {
			http.post('get-candidates', rc.credentials.username).success(function(data) { 
							sc.candidatos = data;
			});
		    var re = new RegExp(sc.query, 'i');
		}
		
		sc.vote = function() {
			/*http.post('get-candidates', {voter:rc.credentials.username, voted:sc.selected} )
			.success(function(data) { 
				
			});*/
			if(sc.selected == 'other') {
				sc.selected = sc.otherSelected;
			}
			sc.result = 'user ' + rc.credentials.username + ' voted on ' + sc.selected;
			sc.voted = true;
		}
		
		sc.loadedStudents = false;
		
		sc.loadStudents = function() {
			if(!sc.loadedStudents){
				http.post('get-students', rc.credentials.username )
				.success(function(data) { 
					sc.students = data;
					sc.loadedStudents = true;
				});
			}
		}
		
		sc.searchFilter = function (obj) {
		    var re = new RegExp(sc.query, 'i');
		    return !sc.query || re.test(obj.username) || re.test(obj.name);
		};
	}
]);
