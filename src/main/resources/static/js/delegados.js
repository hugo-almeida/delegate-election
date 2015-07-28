angular.module('delegados', ['ngRoute']).
	config(function($httpProvider, $routeProvider, $locationProvider) {
		
		$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
});
