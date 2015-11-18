var monitorApp = angular.module("monitorApp", [ 'ngRoute' ]);
monitorApp.config([ '$routeProvider','$locationProvider', function($routeProvider,$locationProvider) {
	$routeProvider.
	when('/addService', {
		templateUrl : 'addService.html',
		controller : 'AddServiceController',
		css: 'css/bootstrap.min.css'
	}).
	when('/viewServices', {
		templateUrl : 'viewServices.html',
		controller : 'ViewServicesController',
		css: 'css/bootstrap.min.css'
	}).
	when('/serviceDetails/:id', {
		templateUrl : 'serviceDetails.html',
		controller : 'ServiceDetailsController',
		css: 'css/bootstrap.min.css'
	}).
	otherwise({
		redirectTo : '/viewServices'
	});
	
	$locationProvider.html5Mode({
		  enabled: false,
		  requireBase: false
		});
	
} ]);

monitorApp.controller('NavBarController', function($scope,$location) {
	var path =$location.path();
	$scope.isActive = function (viewLocation) { 
        return viewLocation === $location.path();
    };
});

//monitorApp.controller('BodyController', function($location,$interval) {
//	$interval(function() {
//		window.location.reload(1)
//	}, 60000)
//});

monitorApp.controller('ServiceTypes', function($scope) {
	$scope.data = {
			types: [
	                {value: "cs", display: "Content Server"},
	                {value: "dsearch", display: "Dsearch"},
	                {value: "indexagent", display: "IndexAgent"},
	                {value: "xcp", display: "xCP Application"},
	                {value: "bps", display: "BPS"},
	                {value: "bpm", display: "BPM"},
	                {value: "jms", display: "JMS"},
	                {value: "dkbrkr", display: "Docbroker"},
	                {value: "bam", display: "BAM"},
	                {value: "xms", display: "xMS Agent"},
	                {value: "da", display: "DA"},
	                {value: "cts", display: "CTS"}
	                ],
	};
});

monitorApp.controller('AddServiceController', function($scope,$http,$location) {
	
	$scope.create = function() {
		var request = $http({
			method: "POST",
			url: "/DocumentumMonitor/Monitor",
			params: {
				action: "add"
			},
			data: {
				name: $scope.newServiceForm.name,
				docbase: $scope.newServiceForm.docbase,
				host: $scope.newServiceForm.host,
				port: $scope.newServiceForm.port,
				user: $scope.newServiceForm.user,
				password: $scope.newServiceForm.pwd,
				email: $scope.newServiceForm.email,
				type: $scope.newServiceForm.type.value
			}
		});
		
		request.success(function(data, status, headers, config) {
            // alert("Success!");
			$location.path('/viewServices');
        });
        request.error(function(data, status, headers, config) {
            alert("Request failed!");
        });
	};
	

});

monitorApp.controller('ViewServicesController', function($scope, $http, $location, $interval) {
	$http({
		method: "GET",
		url: "/DocumentumMonitor/Monitor",
		params: {action: "view"}
	})
	.success(function(data, status, headers, config) {
        $scope.services = data;
    }); 
	
	$scope.isRunning = function(service) {
		return service.status == 'Running' ? 'success' : 'danger';
	};
	
	$interval(function() {
		window.location.reload(1)
	}, 60000)
});

monitorApp.controller('ServiceDetailsController', function($scope, $http,$routeParams,$location) {
	$http({
			method: "GET",
			url: "/DocumentumMonitor/Monitor",
			params: {action: "find",id: $routeParams.id}
		})
		.success(function(data, status, headers, config) {
			$scope.service = data;
        });
	$scope.remove = function() {
		var request = $http({
			method: "DELETE",
			url: "/DocumentumMonitor/Monitor",
			params: {
				id: $routeParams.id
			}
		});
		
		request.success(function(data, status, headers, config) {
            // alert("Success!");
			$location.path('/viewServices');
        });
        request.error(function(data, status, headers, config) {
            alert("Request failed!");
        });
	};
		
});
