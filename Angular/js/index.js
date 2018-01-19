var app = angular.module('myApp', ['ngRoute', 'ngCookies']);

var url = "http://localhost:8080/RESTOracle/rest/UserService";

app.config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "./parts/overview.html",
		controller : "overviewCtrl"
	})
	.when("/applications", {
        templateUrl : "./parts/applications.html",
		controller : "applicationsCtrl"
	})
	.when("/users", {
        templateUrl : "./parts/users.html",
		controller : "usersCtrl"
    })
});


app.controller('mainCtrl', ["$scope", "$http", "$timeout", "$location", function ($scope, $http, $timeout, $location) {
	switch($location.path()) {
		case "/applications":
			setApplicationsActive();
			break;
		case "/users":
			setUsersActive();
			break;
		default:
			setOverviewActive();
			break;
	}

	$scope.overviewClicked = () => {
		setOverviewActive();
		$location.url("/");
	}

	$scope.ApplicationsClicked = () => {
		setApplicationsActive();
		$location.url("applications");
	}

	$scope.UsersClicked = () => {
		setUsersActive();
		$location.url("users");
	}

	function setOverviewActive() {
		$scope.overviewClassActive = 'active';
		$scope.ApplicationsClassActive = '';
		$scope.UsersClassActive = '';
	}

	function setApplicationsActive() {
		$scope.overviewClassActive = '';
		$scope.ApplicationsClassActive = 'active';
		$scope.UsersClassActive = '';
	}

	function setUsersActive() {
		$scope.overviewClassActive = '';
		$scope.ApplicationsClassActive = '';
		$scope.UsersClassActive = 'active';
	}
}]);

app.controller('overviewCtrl', ["$scope", "$http", "$timeout", "$location", function ($scope, $http, $timeout, $location) {
	$scope.allHouses = [];
	$scope.allRooms = [];
	$scope.allHardware = [];

	$http.get(url + "/housing")
	.then(
		function mySuccess(response) {
			$scope.allHouses = response.data;
		},
		function myError(response) {
			alert("not reachable");
		}
	);

	$scope.selectedHouseChanged = () => {
		$http.get(url + "/housing/"+$scope.selectedHouse.id)
		.then(
			function mySuccess(response) {
				$scope.allRooms = response.data.rooms;
			},
			function myError(response) {
				alert("not reachable");
			}
		);
	}

	$scope.selectedRoomsChanged = () => {
		$http.get(url + "/room/"+$scope.selectedRoom.id)
		.then(
			function mySuccess(response) {
				$scope.allHardware = response.data;
			},
			function myError(response) {
				alert("not reachable");
			}
		);
	}
}]);

app.controller('applicationsCtrl', ["$scope", "$http", "$timeout", "$location", function ($scope, $http, $timeout, $location) {
	
}]);

app.controller('usersCtrl', ["$scope", "$http", "$timeout", "$location", function ($scope, $http, $timeout, $location) {

}]);