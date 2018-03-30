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
const generateSource = require('./generate.js');

// Misc
const imagemin = require('gulp-imagemin');
const sourcemaps = require('gulp-sourcemaps');


// --------------------------------------------------------
// Our own post-css plugin to generate Java source files with
// class names so that the compiler can check our names
// --------------------------------------------------------

// Paths
const paths = {
    build: `${__dirname}/build/dev-assets/org/activityinfo/theme/dev/public`,
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
    apply,
    calc,
    customMedia,
    colorFunction,
    rgb,
    mediaMinMax,
    nested,
    responsiveType,
    generateSource,
    autoprefixer
 //   nano
];

// --------------------------------------------------------
// Tasks
// --------------------------------------------------------


// Vectors
function vectors() {
    return gulp.src(`${paths.src}/main/vectors/**/*`)
        .pipe(gulp.dest(`${paths.build}/vectors`));
}

// Styles
function styles() {
    return gulp.src(`${paths.src}/main/css/*.css`)
        .pipe(sourcemaps.init())
        .pipe(postcss(processors))
        .pipe(sourcemaps.write('./'))
        .pipe(gulp.dest(paths.build));
}


// Watch
function watch() {
    gulp.watch(`${paths.src}/**/*.css`, styles);
}


gulp.task('build', gulp.parallel(vectors, styles));
gulp.task('watch', watch);



