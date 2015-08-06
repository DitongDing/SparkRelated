'use strict';

angular.module('zeppelinWebApp').controller(
		'WebsocketResultCtrl',
		function($scope, $rootScope) {
			$scope.paragraphID = null;
			$scope.websocket = null;

			// Test
			$scope.content = null;

			$scope.init = function(paragraphID) {
				$scope.paragraphID = paragraphID;
				console.log(paragraphID);
			}
			
			$scope.open = function() {
				var uri = "ws://" + location.hostname + ":820?paragraphID="
						+ $scope.paragraphID;
				$scope.websocket = new WebSocket(uri);
				var websocket = $scope.websocket;
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
					// Test
					$scope.content = evt.data;
					console.log($scope.content);
					//var obj = JSON.parse(evt.data);
				};
				websocket.onerror = function(evt) {
					console.log(evt.data);
				};
			}

			$scope.close = function() {
				if ($scope.websocket != null)
					$scope.websocket.close();
			}
			
			$scope.$on("openWebsocket", $scope.open);
			
			$scope.$on("closeWebsocket", $scope.close);
		});