angular.module('delegados').controller('voteCtrl', ['$rootScope', '$scope', '$http', '$log', function(rc, sc, http,log) {
		sc.students = [];
		
		sc.voted = false;	//change to rootscope on navigation controller
		
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
			var username = sc.selected;
			if (sc.selected == 'other') {
				username = sc.otherSelected;
			}
			http.post('students/'+rc.credentials.username+'/degrees/'+rc.degree.id+'/votes', username)
			.success(function(data) { 
				sc.feedback = true;
				sc.voted = true;
			});
		}
		
		sc.loadedStudents = false;
		
		sc.loadStudents = function() {
			if(!sc.loadedStudents){
				http.get('degrees/'+rc.degree.id+'/years/'+rc.degree.curricularYear+'/students')
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
