'use strict';

angular.module('zeppelinWebApp').controller(
		'WebsocketResultCtrl',
		function($scope, $rootScope) {
			$scope.paragraphID = null;
			$scope.show = false;
			$scope.type = "";

			// Message
			$scope.content = null;

			// Table
			$scope.tables = [];
			$scope.relationTurples = [];

			var closure = {
				"update" : function(data) {
					$scope.show = true;
					var obj = JSON.parse(data);
					$scope.type = obj.type;
					if (obj.type == "CLEAN") {
						$scope.tables = [];
						$scope.relationTurples = [];
					} else if (obj.type == "MESSAGE") {
						$scope.content = obj.message;
					} else if (obj.type == "TABLE") {
						$scope.tables = obj.tables;
					} else if (obj.type == "RELATIONSHIP") {
						$scope.relationTurples.push(obj.relationTurple);
					}
				}
			}

			$scope.init = function(pID) {
				$scope.paragraphID = pID;
				$rootScope.ddt_map[$scope.paragraphID] = closure;
				if ($rootScope.ddt_websocket == null) {
					var uri = "ws://" + location.hostname + ":820";
					$rootScope.ddt_websocket = new WebSocket(uri);
					var websocket = $rootScope.ddt_websocket;
					websocket.onopen = function(evt) {
						var data = {
							type : "OPEN",
						}
						websocket.send(JSON.stringify(data));
					};
					websocket.onclose = function(evt) {
						console.log("websocket result closed");
					};
					websocket.onmessage = function(evt) {
						// alert("ddt_map = " +
						// JSON.stringify($rootScope.ddt_map));
						// console.log("websocket receive: " +
						// evt.data);
						var obj = JSON.parse(evt.data);
						var ID = obj.paragraphID;
						$rootScope.ddt_map[ID].update(evt.data);
					};
					websocket.onerror = function(evt) {
						console.log(evt.data);
					};
				}
			}

			$scope.getType = function() {
				if ($scope.type == "MESSAGE")
					return "MESSAGE";
				else if ($scope.type == "TABLE"
						|| $scope.type == "RELATIONSHIP")
					return "RELATIONSHIP";
				return null;
			}
		});