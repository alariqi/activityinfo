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

// CSS
const postcss = require('gulp-postcss');
const apply = require('postcss-apply');
const assets = require('postcss-assets');
const autoprefixer = require('autoprefixer');
const calc = require('postcss-calc');
const colorFunction = require('postcss-color-function');
const rgb = require('postcss-rgb');
const customMedia = require('postcss-custom-media');
const importer = require('postcss-easy-import');
const mapper = require('postcss-map');
const mediaMinMax = require('postcss-media-minmax');
const nano = require('cssnano');
const nested = require('postcss-nested');
const responsiveType = require('postcss-responsive-type');
const simpleVars = require('postcss-simple-vars');
const svgSprite = require('gulp-svg-sprites');
const svgInline = require('postcss-inline-svg');

// Misc
const imagemin = require('gulp-imagemin');
const sourcemaps = require('gulp-sourcemaps');


// --------------------------------------------------------
// Our own post-css plugin to generate Java source files with
// class names so that the compiler can check our names
// --------------------------------------------------------

// Paths
const paths = {
    build: `${__dirname}/build/assets/org/activityinfo/theme`,
    src: `${__dirname}/src`,
    modules: `${__dirname}/node_modules`
};

// PostCSS plugins
const processors = [
    importer({
        glob: true
    }),
    // mapper({
    //     maps: [
    //         `${paths.src}/tokens/borders.json`,
    //         `${paths.src}/tokens/breakpoints.json`,
    //         `${paths.src}/tokens/colors.json`,
    //         `${paths.src}/tokens/fonts.json`,
    //         `${paths.src}/tokens/layers.json`,
    //         `${paths.src}/tokens/sizes.json`,
    //         `${paths.src}/tokens/spaces.json`
    //     ]
    // }),
    // assets({
    //     loadPaths: [`${paths.src}/assets/vectors`]
    // }),
    simpleVars,
    svgInline,
    apply,
    calc,
    customMedia,
    colorFunction,
    rgb,
    mediaMinMax,
    nested,
    responsiveType,
    autoprefixer
 //   nano
];

// --------------------------------------------------------
// Tasks
// --------------------------------------------------------



// Styles
// Write to the /public section for now so that we
// can benefit from sourcemaps
function styles() {
    return gulp.src(`${paths.src}/main/css/*.css`)
        .pipe(sourcemaps.init())
        .pipe(postcss(processors))
        .pipe(sourcemaps.write('./'))
        .pipe(gulp.dest(`${paths.build}/public`));
}

function sprites() {
    // return gulp.src('assets/svg/*.svg')
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
            svg             : {symbols: "icons.svg"}

        }))
        .pipe(gulp.dest(`${paths.build}/client`));
}


// Watch
function watch() {
    gulp.watch(`${paths.src}/**/*.css`, styles);
}


gulp.task('build', gulp.parallel(sprites, styles));
gulp.task('watch', watch);



