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
	.when("/hardware", {
        templateUrl : "./parts/hardware.html",
		controller : "HardwareCtrl"
	})
});


app.controller('mainCtrl', ["$scope", "$http", "$timeout", "$location", function ($scope, $http, $timeout, $location) {
	switch($location.path()) {
		case "/applications":
			setApplicationsActive();
			break
		case "/hardware":
			setHardwareActive();
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

	$scope.HardwareClicked = () => {
		setHardwareActive();
		$location.url("hardware");
	}

	function setOverviewActive() {
		$scope.overviewClassActive = 'active';
		$scope.ApplicationsClassActive = '';
		$scope.HardwareClassActive = '';
	}

	function setApplicationsActive() {
		$scope.overviewClassActive = '';
		$scope.ApplicationsClassActive = 'active';
		$scope.HardwareClassActive = '';
	}

	function setHardwareActive() {
		$scope.overviewClassActive = '';
		$scope.ApplicationsClassActive = '';
		$scope.HardwareClassActive = 'active';
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

	$scope.getInfos = (hid) => {
		$scope.showNetworkInfo = false;
		$scope.showApplications = false;

		$scope.furtherName = hid.name;
		$scope.furtherDesc = hid.desc;
		$scope.furtherType = hid.hname;
		$scope.furtherTypeDesc = hid.hdesc;

		$http.get(url + "/room/hardware/"+hid.id)
		.then(
			function mySuccess(response) {
				if(response.data.networkInfo != undefined) {
					$scope.furtherDHCP = response.data.networkInfo.isDHCP;
					$scope.furtherNetInfo = response.data.networkInfo.furtherInfo;
					$scope.showNetworkInfo = true;
				}
			},
			function myError(response) {
				alert("not reachable");
			}
		);
	}
}]);

app.controller('applicationsCtrl', ["$scope", "$http", "$timeout", "$location", function ($scope, $http, $timeout, $location) {
	update();

	function update() {
		$scope.allAnwendungen = [];
		$http.get(url + "/application")
		.then(
			function mySuccess(response) {
				$scope.allAnwendungen = response.data;
			},
			function myError(response) {
				alert("not reachable");
			}
		);
	}

	$scope.change = (app) => {
		$scope.furtherName = app.name;
		$scope.furtherDesc = app.desc;
		$scope.current = app;
	}

	$scope.save = () => {
		var a = {aid: $scope.current.id, aname: $scope.furtherName, adesc: $scope.furtherDesc};

		$http({
			method: 'PUT',
			url: url + "/application",
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			transformRequest: function(obj) {
				var str = [];
				for(var p in obj)
				str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
				return str.join("&");
			},
			data: a
		}).then(function(response) {
			update();
		 }, function(response) {
			alert("Error during update!");
		 });
	}
}]);

app.controller('HardwareCtrl', ["$scope", "$http", "$timeout", "$location", function ($scope, $http, $timeout, $location) {
	update();

	function update() {
		$scope.allHardware = [];
		$http.get(url + "/hardware")
		.then(
			function mySuccess(response) {
				$scope.allHardware = response.data;
			},
			function myError(response) {
				alert("not reachable");
			}
		);
	}

	$scope.change = (app) => {
		$scope.furtherName = app.name;
		$scope.furtherDesc = app.desc;
		$scope.current = app;
	}
}]);