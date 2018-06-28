// --------------------------------------------------------
// Dependencies
// --------------------------------------------------------

// Utils
const fs = require('fs');
const del = require('del');
const gulp = require('gulp');
const util = require('gulp-util');
const notify       = require('gulp-notify');
const plumber      = require('gulp-plumber');
const rename = require("gulp-rename");
const touch = require("touch");


// CSS
const sass = require('gulp-sass');
const sassGlob = require('gulp-sass-glob');
const rtlcss = require('gulp-rtlcss');
const nano = require('cssnano');

// Icons/Svg
const svgSprite = require('gulp-svg-sprites');


// Misc
const imagemin = require('gulp-imagemin');
const sourcemaps = require('gulp-sourcemaps');


// --------------------------------------------------------
// Our own post-css plugin to generate Java source files with
// class names so that the compiler can check our names
// --------------------------------------------------------

// Paths
const paths = {
    build: `${__dirname}/build/assets/org/activityinfo/ui`,
    src: `${__dirname}/src`,
    modules: `${__dirname}/node_modules`
};


// --------------------------------------------------------
// Tasks
// --------------------------------------------------------



// Styles
function styles() {
    return gulp.src(`${paths.src}/main/css/app.scss`)
        .pipe(sassGlob())
        .pipe(sass.sync().on('error', sass.logError))
        .pipe(gulp.dest(`${paths.build}/client`))
        .pipe(rtlcss())
        .pipe(rename({basename: 'app-rtl'}))
        .pipe(gulp.dest(`${paths.build}/client`))
        .on('end', function() {
            console.log("Touching ThemeBundle.java...");
            touch.sync(`${paths.src}/main/java/org/activityinfo/ui/client/ThemeBundle.java`)
        })
}

function sprites() {

    // See https://github.com/jkphl/svg-sprite/blob/master/docs/configuration.md
    // for configuration details

    return gulp.src(`${paths.src}/main/icons/*.svg`)
        .pipe(svgSprite({

            // Set prefix for every svg
            selector        : "%f",

            // Set mode for the sprites
            mode            : "symbols",
            // css file for the svgs
            // cssFile         : false,
            //turned off preview, because it generates a different html file
            // preview         : {symbols:  "icons.html"},
            preview         : false,
            svg             : {symbols: "icons.svg"},

            shape: {
                transform: [
                    {svgo: {
                            plugins: [
                                {removeTitle: true},
                            ]
                        }}
                ]
            },

            // Fix problem with gradient name conflicts
            // See https://github.com/shakyShane/gulp-svg-sprites/issues/108
            transformData: function (data, config) {
                data.svg.map(function(item) {
                    //change id attribute
                    item.data=item.data.replace(/id=\"([^\"]+)\"/gm, 'id="'+item.name+'-$1"');

                    //change id in fill attribute
                    item.data=item.data.replace(/fill=\"url\(\#([^\"]+)\)\"/gm, 'fill="url(#'+item.name+'-$1)"');

                    //change id in mask attribute
                    item.data=item.data.replace(/mask=\"url\(\#([^\"]+)\)\"/gm, 'mask="url(#'+item.name+'-$1)"');

                    //replace double id for the symbol tag
                    item.data=item.data.replace('id="'+item.name+'-'+item.name+'"', 'id="'+item.name+'"');
                    return item;
                });
                return data; // modify the data and return it
            }

        }))
        .pipe(gulp.dest(`${paths.build}/client`));
}


// Watch
function watch() {
    gulp.watch(`${paths.src}/main/**/*.scss`, styles);
}


gulp.task('build', gulp.parallel(sprites, styles));
gulp.task('watch', watch);



