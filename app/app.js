var angular = require('angular');
var backend = require('./service/backend');
var indexCtrl = require('./controller/index');
var batchCtrl = require('./controller/batch');
var archiveCtrl = require('./controller/archive');

angular
	.module('app', [ require('angular-route')])
	.config(function($routeProvider) {
	
		$routeProvider
		.when('/', {
			templateUrl: 'views/index.html',
			controller: 'indexCtrl'
		})
		.when('/archives', {
			templateUrl: 'views/archive.html',
			controller: 'archiveCtrl'
		})		
		.when('/batch-stat', {
			templateUrl: 'views/batch.html',
			controller: 'batchCtrl'
		})
		.when('/batch-stat/:id', {
			templateUrl: 'views/batch.html',
			controller: 'batchCtrl'
		});		
	
	})
	.service('backend', backend)
	.controller('indexCtrl', indexCtrl)
	.controller('batchCtrl', batchCtrl)
	.controller('archiveCtrl', archiveCtrl);
