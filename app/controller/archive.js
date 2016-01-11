module.exports = [ '$scope', '$routeParams', 'backend', function($scope, $routeParams, backend){
	
	$scope.consonants = ['ㄱ', 'ㄴ', 'ㄷ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅅ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ','ㅎ'];
	$scope.results = [];
	$scope.pronuns = [];
	
	function fetch(res, status, config, headers){
		$scope.results.rows = res.rows;
		$scope.results.pronuns = res.pronuns;
	}
	
	if($routeParams.id && $routeParams.pronun) {
		$scope.index = $routeParams.id;
		$scope.pronun = $routeParams.pronun;
		
		backend
		.searchByIndexWithLetter($routeParams.id, $routeParams.pronun)
		.success(fetch)
		.error(function(res, status, config, headers){
			alert('Error');
		});
	}
	else if($routeParams.id){
		
		$scope.index = $routeParams.id;
		
		backend
		.searchByIndex($routeParams.id)
		.success(fetch)
		.error(function(res, status, config, headers){
			alert('Error');
		});
	
	}else {
		backend
		.getCount()
		.success(function(res){
			$scope.count = res.count;;
		})
		.error(function(res, status, config, headers){
			alert('Error');
		});
	};
	
	$scope.getMore = function(hanja) {
		
		backend
		.getLetterMeans(hanja.id)
		.success(function(res) {
			$scope.results.means = res;
			$scope.results.hanja = hanja;
		})
		.error(function(res, status, config, headers){
			alert('Error');
		});	
	};
	
	$scope.clearMore = function() {
		$scope.results.means = null;
	}

}];
