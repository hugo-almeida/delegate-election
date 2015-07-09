angular.module('delegados').controller('tablesCtrl', ['$rootScope', '$scope', '$http', '$log',
                                                          function(rc, sc, http, log)  {
	sc.active = 'Candidaturas';
	
	sc.activeTab = function(tab) {
		if(sc.active == tab)
			return true;
		else return false;
	}
	
	sc.setActiveTab = function (tab) {
		sc.active = tab;
		log.log(sc.active);
	}
}]);