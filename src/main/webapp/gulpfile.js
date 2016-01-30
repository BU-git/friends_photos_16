var gulp = require('gulp');
var htmlreplace = require('gulp-html-replace');
var filelist = require('gulp-filelist');
var mainBowerFiles = require('main-bower-files');
var rename = require("gulp-rename");
var fs = require('fs');
var runSequence = require('run-sequence');
var concat = require('gulp-concat');
var ngHtml2Js = require("gulp-ng-html2js");
var minifyCss = require('gulp-minify-css');
var uglify = require('gulp-uglify');

gulp.task('include_files', function() {
    return gulp
        .src('index.dev.html')
        .pipe(htmlreplace({
            //'css': 'styles.min.css',
            'app_js': JSON.parse(fs.readFileSync('filelist.json', 'utf8'))
        }))
        .pipe(rename('index.html'))
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

gulp.task('prod', function(done) {
    runSequence('html2js', 'concat_js', 'concat_css', function() {
        //console.log('Run something else');
        done();
    });
});

gulp.task('concat_css', function() {
    var bower = mainBowerFiles('**/*.css');
    console.log(bower);
    return gulp.src(bower.concat(['./css/**/*.css']))
        .pipe(concat('app.min.css'))
        .pipe(minifyCss())
        .pipe(gulp.dest('./dist/'));
});

gulp.task('concat_js', function() {
    var bower = mainBowerFiles('**/*.js');
    console.log(bower);
    return gulp.src(bower.concat(['./app/**/*.js', './dist/templates.html.js']))
        .pipe(concat('app.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest('./dist/'));
});

gulp.task('bower_files_js', function() {
    return gulp.src(mainBowerFiles('**/*.js').concat(['./app/**/*.js']))
        .pipe(concat('bower.js'))
        .pipe(gulp.dest('./dist/'));
});

gulp.task('bower_files_css', function() {
    return gulp.src(mainBowerFiles('**/*.css'))
        .pipe(concat('bower.css'))
        .pipe(gulp.dest('./dist/'));
});

gulp.task('bower_files', function(done) {
    runSequence('bower_files_js', 'bower_files_css', function() {
        //console.log('Run something else');
        done();
    });
});

gulp.task('html2js', function() {
    return gulp.src("./app/**/*.html")
        .pipe(ngHtml2Js({
            moduleName: "friends_photos",
            prefix: "app/"
        }))
        .pipe(concat("templates.html.js"))
        .pipe(gulp.dest("./dist"));
});
