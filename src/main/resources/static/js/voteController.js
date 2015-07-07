angular.module('delegados').controller('voteCtrl', ['$rootScope', '$scope', '$http', '$log', function(rc, sc, http,log) {
		sc.candidatos = [];
		sc.students = [];
		http.get('degrees/'+sc.$parent.degree.id+'/years/'+sc.$parent.degree.curricularYear+'/candidates')
		.success(function(data) { 
			sc.candidatos = data;
			//sc.voto = sc.candidatos[0];
			//log.log(sc.voto);
		});
		
		sc.voted = false;	//true; //should be false, true for testing purposes
		
		sc.reloadCandidates = function() {
			http.get('degrees/'+sc.$parent.degree.id+'/years/'+sc.$parent.degree.curricularYear+'/candidates')
			.success(function(data) { 
				sc.candidatos = data;
			});
		}
		
		sc.selection = function() {
			if(sc.selected == 'other') {
				for(var i = 0; i < sc.students.length; i++) {
				    if (sc.students[i].username == sc.otherSelected) {
				        name = sc.students[i].name;
				        break;
				    }
				}
				sc.selectionResult = name + ' (' + sc.otherSelected +')';
			}
			else if(sc.selected == 'nil')
				sc.selectionResult = 'branco';
			else {
				for(var i = 0; i < sc.candidatos.length; i++) {
				    if (sc.candidatos[i].username == sc.selected) {
				        name = sc.candidatos[i].name;
				        break;
				    }
				}
				sc.selectionResult = name + ' (' + sc.selected +')';
			}
		}
		
		sc.vote = function() {
			http.post('students/'+rc.credentials.username+'/degrees/'+sc.$parent.degree.id+'/votes', sc.selected)
			.success(function(data) { 
				sc.feedback = true;
				sc.voted = true;
			});
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
		
		sc.searchFilter = function (obj) {					//not the right way to do this, should query server on key press 
		    var re = new RegExp(sc.query, 'i');				//every x seconds, with at least 2 characters
		    return !sc.query || re.test(obj.username) || re.test(obj.name);
		};
	}
]);
