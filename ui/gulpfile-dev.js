var gulp = require("gulp");
var gutil = require("gulp-util");
var rename = require('gulp-rename');
var webpack = require("webpack");
var webpackConfig = require('./webpack.dev.config.js');
var gnf = require('gulp-npm-files');

gulp.task("default", ['webpack', 'copy-deps', 'copy-html', 'copy-lib', 'copy-assets']);

gulp.task("webpack", function(cb){
  webpack(webpackConfig, function(err, stats) {
    if(err) throw new gutil.PluginError("webpack", err);
    gutil.log("[webpack]", stats.toString());
    cb();
  });
});

gulp.task("copy-deps", function(){
  gulp.src(gnf(), {base:'./'}).pipe(gulp.dest('./public/ui'));
});

gulp.task("copy-lib", function(){
  gulp.src("dll/lib.js").pipe(gulp.dest('./public/ui'));
});


gulp.task("copy-html", function(){
  gulp.src("index-dev.html").pipe(rename('index.html')).pipe(gulp.dest('public/ui/'));
});

gulp.task("copy-assets", function(){
  gulp.src("assets/**/*").pipe(gulp.dest("./public/ui/assets"));
});
