var postcss = require('postcss');
var fs = require('fs');
var mkdirp = require('mkdirp');


var classRe = /\.[A-Za-z][A-Za-z_-]+/g;

var findClassNames = function(selector) {
    var matches = selector.match(classRe);
    if(matches) {
        return matches.map(function(n) {
            return n.substring(1);
        });
    } else {
        return [];
    }
};
var onlyUnique = function(value, index, self) {
    return self.indexOf(value) === index;
};

var toJavaIdentifier = function(className) {
    return className.toUpperCase().replace(/-/g, "_");
};

module.exports = postcss.plugin('postcss-gwt-plugin', function (opts) {
    return function (root, result) {

        var classNames = [];
        root.walkRules(function(rule) {
            findClassNames(rule.selector).forEach(function(n) {
                classNames.push(n);
            })
        });

        var constants = classNames.filter(onlyUnique).sort().map(function(n) {
            return "public static final String " + toJavaIdentifier(n) +
                        " = \"" + n + "\";"
        });


        var source = [
            "package org.activityinfo.theme.client;",
            "public final class ClassNames {",
            "private ClassNames() {}",
            constants.join("\n"),
            "}"
        ];

        var packageDir = `${__dirname}/build/generated-styles/org/activityinfo/theme/client`;
        console.log(`Writing to ${packageDir}...`);
        mkdirp(packageDir, function(err) {
            if(err) {
                console.log(`Failed to create directory at ${packageDir}: ${err}`);
            } else {
                fs.writeFile(packageDir + "/ClassNames.java", source.join("\n"), function(err) {
                    if(err) {
                        console.log(`Failed to write java sources: ${err}`);
                    }
                });
            }
        });
    };
});