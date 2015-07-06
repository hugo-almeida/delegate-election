angular.module('delegados').controller('voteCtrl', ['$rootScope', '$scope', '$http', '$log', function(rc, sc, http,log) {
		sc.candidatos = [];
		sc.students = [];
		http.get('degrees/'+sc.$parent.degree.id+'/years/'+sc.$parent.degree.curricularYear+'/candidates')
		.success(function(data) { 
			sc.candidatos = data;
		});
		
		//sc.candidatos = [  {name:'Ricardo Pires', username:'ist167066'}, {name:'Hugo Almeida', username:'ist166997'}, {name:'Fernando Santos', username:'ist123456'} ];
		
		sc.voted = false;
		
		sc.reloadCandidates = function() {
			http.get('degrees/'+sc.$parent.degree.id+'/years/'+sc.$parent.degree.curricularYear+'/candidates')
			.success(function(data) { 
				sc.candidatos = data;
			});
		}
		
		sc.period = function() {
			http.get('period?istid=' + rc.credentials.username )
			.success(function(data) { 
				console.log(data);
			});
		}
		
		sc.vote = function() {
			http.post('students/'+rc.credentials.username+'/degrees/'+sc.$parent.degree.id+'/votes', sc.selected)
			.success(function(data) { 
				
			});
//			if(sc.selected == 'other') {
//				sc.selected = sc.otherSelected;
//			}
//			sc.result = 'user ' + rc.credentials.username + ' voted on ' + sc.selected;
//			sc.voted = true;
		}
		
		sc.loadedStudents = false;
		
		sc.loadStudents = function() {
			if(!sc.loadedStudents){
				http.get('degrees/'+sc.$parent.degree.id+'/years/'+sc.$parent.degree.curricularYear+'/students')
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
