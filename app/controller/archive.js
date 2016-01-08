module.exports = [ '$scope', 'backend', function($scope, backend){
	
	$scope.results = [];
	
	backend
	.getDics()
	.success(function(res, status, config, headers){
		$scope.results = res;
		console.log(res);
	})
	.error(function(res, status, config, headers){
		alert('Error');
	});			

}];
