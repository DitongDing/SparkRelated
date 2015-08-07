'use strict';

angular.module('zeppelinWebApp').controller(
		'WebsocketResultCtrl',['$scope', '$rootScope', 
		function($scope, $rootScope) {
			$scope.paragraphID = null;

			// Test
			$scope.content = null;
			
			var closure = {
				"update" : function(data) {
					var obj = JSON.parse(data);
					$scope.content = obj.message;
				} 
			}

			$scope.init = function(pID) {
				$scope.paragraphID = pID;
				$rootScope.ddt_map[$scope.paragraphID] = closure;
				if($rootScope.ddt_websocket == null) {
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
						// alert("ddt_map = " + JSON.stringify($rootScope.ddt_map));
						// console.log("websocket receive: " + evt.data);
						var obj = JSON.parse(evt.data);
						var ID = obj.paragraphID;
						$rootScope.ddt_map[ID].update(evt.data);
					};
					websocket.onerror = function(evt) {
						console.log(evt.data);
					};
				}
			}
		}]);