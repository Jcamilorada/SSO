module.exports = function(grunt)
{
   require('load-grunt-tasks')(grunt);

   grunt.initConfig({

      clean : {
         main : {
            src : [ '../resources/static/build' ]
         }
      },

      jshint : {
         main : {
            src : [ 'app/**/*.js' ]
         }
      },

      cssmin : {
         main : {
            src : [ 'app/styles/*.css' ],
            dest : '../resources/static/build/dist/login.min.css'
         },
         libraries : {
            src : [ 'bower_components/bootstrap/dist/css/bootstrap.css'],
            dest : '../resources/static/build/dist/libraries.min.css'
         }
      },

      copy: {
          files: {
            cwd: 'bower_components/bootstrap/fonts/',
            src: ['**/*.{woff,woff2,ttf}'],
            dest: '../resources/static/build/fonts/',
            expand: true
          }
      },

      ngtemplates : {
         main : {
            options : {
               module : 'login',
               htmlmin : {
                  collapseBooleanAttributes : true,
                  collapseWhitespace : true,
                  removeAttributeQuotes : true,
                  removeComments : true,
                  removeEmptyAttributes : true,
                  removeRedundantAttributes : true,
                  removeScriptTypeAttributes : true,
                  removeStyleLinkTypeAttributes : true
               }
            },
            src : [ 'app/templates/*.html' ],
            dest : '../resources/static/build/temp/templates.js'
         }
      },

      ngAnnotate : {
         main : {
            src : [
                  'app/module.js',
                  'app/**/*.js',
                  'build/temp/templates.js' ],
            dest : '../resources/static/build/dist/login.js'
         }
      },

      uglify : {
         main : {
            options : {
               sourceMap : true,
               sourceMapName : '../resources/static/build/dist/login.min.map'
            },
            src : [ '../resources/static/build/dist/login.js' ],
            dest : '../resources/static/build/dist/login.min.js'
         },
         libraries: {
            src : [
                'bower_components/angular/angular.js',
                'bower_components/angular-route/angular-route.js',
                'bower_components/angular-resource/angular-resource.js'],
            dest : '../resources/static/build/dist/libraries.min.js'
         }
      }
   });

   grunt.registerTask('build', [ 'jshint', 'cssmin', 'ngtemplates', 'ngAnnotate', 'uglify', 'copy']);
};