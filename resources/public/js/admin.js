app.config(['$routeProvider', function($routeProvider, $locationProvider) {
    $routeProvider.
	when('/', {redirectTo:'/dashboard'}).
	when('/dashboard', {templateUrl: '/admin/dashboard', controller:"AdminCtrl"}).
	when('/site', {templateUrl: '/admin/site', controller:"SitesCtrl"}).
	when('/site/edit/:id', {templateUrl: function(params){return '/admin/site/edit/'+params.id}, controller:"SiteEditCtrl"}).
	when('/page', {templateUrl: '/admin/page', controller: "PageCtrl"}).
	when('/profile', {templateUrl: '/admin/profile', controller: "ProfileCtrl"}).
	when('/help', {templateUrl: '/admin/help', controller: "HelpCtrl"}).
	when('/settings', {templateUrl: '/admin/settings', controller: "SettingsCtrl"}).
	when('/business', {templateUrl: '/admin/business', controller: "BusinessCtrl"}).
	otherwise({templateUrl: '/admin/404',controller:"Admin404Ctrl"});
}]);


