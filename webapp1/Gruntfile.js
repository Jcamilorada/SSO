module.exports = function(grunt)
{
   require('load-grunt-tasks')(grunt);

   grunt.initConfig({
      bower: {
        install: {
            options: {
                    copy: false,
            }
        }
      },

      clean : {
         main : {
            src : [ 'src/main/resources/static/build' ]
         }
      },

      jshint : {
         main : {
            src : [ 'src/main/web/app/**/*.js' ]
         }
      },

      cssmin : {
         main : {
            src : [ 'bower_components/bootstrap/dist/css/bootstrap.css', 'src/main/web/app/styles/*.css' ],
            dest : 'src/main/resources/static/build/dist/app.min.css'
         },
      },

      copy: {
          files: {
            cwd: 'bower_components/bootstrap/fonts/',
            src: ['**/*.{woff,woff2,ttf}'],
            dest: 'src/main/resources/static/build/fonts/',
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
            cwd: 'src/main/web',
            src : [ 'app/templates/*.html' ],
            dest : 'src/main/resources/static/build/temp/templates.js'
         }
      },

      ngAnnotate : {
         main : {
            src : [
                  'bower_components/angular/angular.js',
                  'bower_components/angular-route/angular-route.js',
                  'bower_components/angular-resource/angular-resource.js',
                  'src/main/web/app/module.js',
                  'src/main/web/app/**/*.js',
                  'src/main/resources/static/build/temp/templates.js' ],
            dest : 'src/main/resources/static/build/dist/app.js'
         }
      },

      uglify : {
        main : {
              options : {
                 sourceMap : true,
                 sourceMapName : 'build/dist/login.min.map'
              },
              src : [ 'src/main/resources/static/build/dist/app.js' ],
              dest : 'src/main/resources/static/build/dist/app.min.js'
           }
      }
   });

   grunt.registerTask('build', ['clean', 'bower:install','jshint', 'cssmin', 'ngtemplates', 'ngAnnotate', 'uglify', 'copy']);
};
