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

	$scope.newHouseChanged = () => {
		$http.get(url + "/housing/"+$scope.selectedHouseForNew.id)
		.then(
			function mySuccess(response) {
				$scope.allRoomsForNew = response.data.rooms;
			},
			function myError(response) {
				alert("not reachable");
			}
		);
	}

	$scope.addNewHardwareToRoom = () => {
		$http({
			method: 'POST',
			url: url + "/room/hardware",
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			transformRequest: function(obj) {
				var str = [];
				for(var p in obj)
				str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
				return str.join("&");
			},
			data: {hid: $scope.selectedHardwareForNew.id, roomID: $scope.selectedRoomForNew.id,
				   name: $scope.nameForNew, rhdesc: $scope.descForNew }
		}).then(function(response) {
			updateHardware();
		}, function(response) {
			alert("Error during update!");
		});
	}

	$scope.addNewAnwendung = () => {
		$scope.allHardwareTypes = [];
		$http.get(url + "/hardware")
		.then(
			function mySuccess(response) {
				$scope.allHardwareTypes = response.data;
			},
			function myError(response) {
				alert("not reachable");
			}
		);

	}

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
		updateHardware();
	}

	$scope.deleteHW = () => {
		$http({
			method: 'DELETE',
			url: url + "/room/hardware",
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			transformRequest: function(obj) {
				var str = [];
				for(var p in obj)
				str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
				return str.join("&");
			},
			data: {rhid: $scope.hid.id}
		}).then(function(response) {
			updateHardware();
		}, function(response) {
			alert("Error during update!");
		});
	}

	function updateHardware() {
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

	$scope.anwendungenModalOpened = () => {
		$scope.allAnwendungen = [];
		$http.get(url + "/application")
		.then(
			function mySuccess(response) {
				$scope.allAnwendungen = response.data.filter((obj) => {
						return !( $scope.installedApps.some((el) => {
							return el.id == obj.id;
						}));
				});
			},
			function myError(response) {
				alert("not reachable");
			}
		);
	}

	$scope.SaveNetworkInfo = () => {
		$http({
			method: 'POST',
			url: url + "/room/hardware/networkInfo",
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			transformRequest: function(obj) {
				var str = [];
				for(var p in obj)
				str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
				return str.join("&");
			},
			data: {hid: $scope.hid.id, isDHCP: ($scope.newDHCP)?1:0, addInfo: $scope.newNetworkInfo}
		}).then(function(response) {
			$scope.getInfos($scope.hid);
		}, function(response) {
			alert("Error during update!");
		});
	}

	$scope.NetworkModalOpenend = () => {
		$scope.newDHCP = $scope.furtherDHCP;
		$scope.newNetworkInfo = $scope.furtherNetInfo;
	}

	$scope.getInfos = (hid) => {
		$scope.showNetworkInfo = false;
		$scope.hid = hid;
		$scope.furtherName = hid.name;
		$scope.furtherDesc = hid.desc;
		$scope.furtherType = hid.hname;
		$scope.furtherTypeDesc = hid.hdesc;
		$scope.installedApps = [];
		$scope.furtherDHCP = undefined;
		$scope.furtherNetInfo = undefined;

		$http.get(url + "/room/hardware/"+hid.id)
		.then(
			function mySuccess(response) {
				if(response.data.networkInfo != undefined) {
					$scope.installedApps = response.data.applications;
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

	$scope.allowDrop = (ev) => {
		ev.preventDefault();
	}
	
	$scope.drag = (ev) => {
		ev.dataTransfer.setData("anwendungsID", ev.target.getAttribute("name"));
	}
	
	$scope.droppedToAvail = (ev) => {
		ev.preventDefault();
		var id = ev.dataTransfer.getData("anwendungsID");
		
		var obj = $scope.installedApps.find((elem) => {
			return elem.id == id;
		});
		if(obj != undefined) {
			$scope.allAnwendungen.push(obj);
			$scope.installedApps = $scope.installedApps.filter((elem) => {
				return elem.id != id;
			});
			$scope.$apply();
			removeAnwendungFromHardware(id, $scope.hid.id);
		}
	}

	$scope.droppedToInstalled = (ev) => {
		ev.preventDefault();
		var id = ev.dataTransfer.getData("anwendungsID");

		var obj = $scope.allAnwendungen.find((elem) => {
			return elem.id == id;
		});
		if(obj != undefined) {
			$scope.installedApps.push(obj);
			$scope.allAnwendungen = $scope.allAnwendungen.filter((elem) => {
				return elem.id != id;
			});
			$scope.$apply();
			addAnwendungToHardware(id, $scope.hid.id);
		}
	}
	
	function removeAnwendungFromHardware(id, hid) {
		var myObj = {aID: id, hid: hid};
		$http({
			method: 'DELETE',
			url: url + "/room/hardware/application",
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			transformRequest: function(obj) {
				var str = [];
				for(var p in obj)
				str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
				return str.join("&");
			},
			data: myObj
		}).then(function(response) {
			console.log("Added");
		}, function(response) {
			alert("Error during update!");
		});
	}

	function addAnwendungToHardware(id, hid) {
		var myObj = {aID: id, hid: hid};
		$http({
			method: 'POST',
			url: url + "/room/hardware/application",
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			transformRequest: function(obj) {
				var str = [];
				for(var p in obj)
				str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
				return str.join("&");
			},
			data: myObj
		}).then(function(response) {
			console.log("Added");
		}, function(response) {
			alert("Error during update!");
		});
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

		if($scope.current == undefined) {
			var myObj = {aname: $scope.furtherName, adesc: $scope.furtherDesc};
			$http({
				method: 'POST',
				url: url + "/application",
				headers: {'Content-Type': 'application/x-www-form-urlencoded'},
				transformRequest: function(obj) {
					var str = [];
					for(var p in obj)
					str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
					return str.join("&");
				},
				data: myObj
			}).then(function(response) {
				update();
			}, function(response) {
				alert("Error during update!");
			});
		}
		else {
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
	}


	$scope.addHardware = () => {
		$scope.furtherName = "";
		$scope.furtherDesc = "";
		$scope.current = undefined;
	}

	$scope.delete = () => {
		if($scope.current != undefined) {
			var a = {aid: $scope.current.id};			
			$http({
				method: 'DELETE',
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

	$scope.change = (hw) => {
		$scope.furtherName = hw.name;
		$scope.furtherDesc = hw.desc;
		$scope.current = hw;
	}

	$scope.save = () => {
		
		if($scope.current == undefined) {
			var myObj = {hname: $scope.furtherName, hdesc: $scope.furtherDesc};
			$http({
				method: 'POST',
				url: url + "/hardware",
				headers: {'Content-Type': 'application/x-www-form-urlencoded'},
				transformRequest: function(obj) {
					var str = [];
					for(var p in obj)
					str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
					return str.join("&");
				},
				data: myObj
			}).then(function(response) {
				update();
			}, function(response) {
				alert("Error during update!");
			});
		}
		else {
			var a = {hid: $scope.current.id, hname: $scope.furtherName, hdesc: $scope.furtherDesc};			
			$http({
				method: 'PUT',
				url: url + "/hardware",
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
	}

	$scope.addHardware = () => {
		$scope.furtherName = "";
		$scope.furtherDesc = "";
		$scope.current = undefined;
	}

	$scope.delete = () => {
		if($scope.current != undefined) {
			var a = {hid: $scope.current.id};			
			$http({
				method: 'DELETE',
				url: url + "/hardware",
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
	}
}]);