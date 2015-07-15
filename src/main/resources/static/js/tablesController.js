angular.module('delegados').controller('tablesCtrl', ['$rootScope', '$scope', '$http', '$log',
                                                          function(rc, sc, http, log)  {
	
	sc.loaded = false;
	
	sc.allSelected = false;
	
	sc.loadDegrees = function() {
		http.get('periods').success(function(data){
			sc.degrees = data;
			sc.loaded = true;
		});
	};
	
	sc.loadDegrees();
	
	sc.selection = [];
	
	sc.toggleSelection = function(degree) {
		var index = sc.selection.indexOf(degree);
		
		if(index > -1) {
			sc.selection.splice(index, 1);
		}
		else {
			sc.selection.push(degree);
		}
	};
	
	sc.toggleSelectAll = function() {
		log.log('hi');
		if(!sc.allSelected) {
			log.log('toggling off')
			for(degree in sc.degrees) {
				log.log(sc.degrees[degree])
				var name = sc.degrees[degree].degree;
				var index = sc.selection.indexOf(name);
				
				if(index <= -1) {
					sc.selection.push(name);
				}
			}
			sc.allSelected = true;
		}
		else {
			log.log('toggling on')
			for(degree in sc.degrees) {
				log.log(sc.degrees[degree])
				var name = sc.degrees[degree].degree;
				var index = sc.selection.indexOf(name);
				
				if(index > -1) {
					sc.selection.splice(index, 1);
				}
			}
			sc.allSelected = false;
		}
	}
}]);