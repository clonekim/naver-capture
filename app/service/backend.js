module.exports = ['$http', function($http){

	this.batchStat = function(id) {	
		return $http({ method: 'GET', url: '/api/batch-stat/' + id});
	};	
	
	this.batchStats = function() {	
		return $http({ method: 'GET', url: '/api/batch-stat'});
	};

	this.postLink = function(batch) {
		return $http({ method: 'POST', data: batch, url: '/api/batch'});
	};
	
	this.deleteBatch = function(id) {
		return $http({ method: 'DELETE', url: '/api/batch/' + id});	
	};
	
	this.clean = function() {
		return $http({ method: 'DELETE', url: '/api/reset'});	
	};

	this.getCount = function() {
		return $http({ method: 'GET', url: '/api/archive'});
	};
		
	this.searchByIndex = function(idx) {
		return $http({ method: 'GET', url: '/api/archive/' + idx});
	};
	
	this.searchByIndexWithLetter = function(idx1, idx2) {
		return $http({ method: 'GET', url: '/api/archive/' + idx1 + '/' + idx2});
	};
	
	this.getLetterMeans = function(id) {
		return $http({ method: 'GET', url: '/api/more-means/' + id});
	};
		
}];
