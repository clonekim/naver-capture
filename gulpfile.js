var gulp = require('gulp'),
	uglify = require('gulp-uglify'),
	browserify = require('browserify'),
	buffer = require('vinyl-buffer'),
	source = require('vinyl-source-stream'),
	ngAnnotate = require('gulp-ng-annotate');
 
gulp.task('build', function() {
	var b = browserify('./app/app.js', {debug:true});
	return b.bundle()		 
		 .pipe(source('app.js'))
 		 .pipe(gulp.dest('./resources/public/js'));
});
 

gulp.task('dist', function() {
	var b = browserify('./app/app.js', {debug:true});
	return b.bundle()		 
		 .pipe(source('app.js'))
		 .pipe(buffer())
		 .pipe(ngAnnotate())		 
		 .pipe(uglify())
 		 .pipe(gulp.dest('./resources/public/js'));
});

gulp.task('watch', function() {
	gulp.watch('./app/**/*.js', ['build']);
});
