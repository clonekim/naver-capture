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

	this.getDics = function() {
		return $http({ method: 'GET', url: '/api/dic'});
	};
	
	this.getLetterMeans = function() {
		return $http({ method: 'GET', url: '/api/dic-annot'});
	};
	
}];
