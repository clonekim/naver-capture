module.exports = [ '$scope', '$routeParams', 'backend', function($scope, $routeParams, backend){

	$scope.results = [];
			
	if($routeParams.id) {
	
		backend
		.batchStat($routeParams.id)
		.success(function(res, status, config, headers){
			$scope.results = res;
		})
		.error(function(res, status, config, headers){
			alert('Error');
		});
	
	}else {
	
		backend
		.batchStats()
		.success(function(res, status, config, headers){
			$scope.results = res;
		})
		.error(function(res, status, config, headers){
			alert('Error');
		});
	}
}];
