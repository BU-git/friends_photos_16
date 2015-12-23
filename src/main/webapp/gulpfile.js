var gulp = require('gulp');
var htmlreplace = require('gulp-html-replace');
var filelist = require('gulp-filelist');
var mainBowerFiles = require('main-bower-files');
var rename = require("gulp-rename");
var fs = require('fs');
var runSequence = require('run-sequence');

gulp.task('include_files', function() {
    return gulp
        .src('index.dev.html')
        .pipe(htmlreplace({
            //'css': 'styles.min.css',
            'app_js': JSON.parse(fs.readFileSync('filelist.json', 'utf8'))
        }))
        .pipe(rename('index.jsp'))
        .pipe(gulp.dest('./'));
});

gulp.task('filelist', function() {
    return gulp
        .src('./app/**/*.js')
        .pipe(require('gulp-filelist')('filelist.json'))
        .pipe(gulp.dest('./'));
});

gulp.task('develop', function(done) {
    runSequence('filelist', 'include_files', function() {
        //console.log('Run something else');
        done();
    });
});
