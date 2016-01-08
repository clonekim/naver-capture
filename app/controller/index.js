module.exports = [ '$scope', 'backend', function($scope, backend){

	$scope.consonants = ['ㄱ', 'ㄴ', 'ㄷ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅅ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ','ㅎ'];

	$scope.status = null;

	$scope.addBatch = function(batch) {
	
		backend
			.postLink(batch)
			.success(function(res, status, config, headers){
				$scope.status = "등록 되었습니다";
			})
			.error(function(res, status, config, headers){
				alert('Error');
			})			
	};

}];
