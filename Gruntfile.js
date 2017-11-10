module.exports = function(grunt) {
    'use strict';
    require('time-grunt')(grunt);

    /**
     * Setup
     */
    var pkg = grunt.file.readJSON('package.json');
    var isDev = (grunt.option('dev') !== undefined) ? Boolean(grunt.option('dev')) : process.env.GRUNT_ISDEV === '1';
    var singleRun = grunt.option('single-run') !== false;

    /**
     * Load all grunt-* tasks
     */
    require('load-grunt-tasks')(grunt);
    
    //load path for absolute paths
    var path = require('path');

    if (isDev) {
        grunt.log.subhead('Running Grunt in DEV mode');
    }

    grunt.initConfig({
        pkg: pkg,

        dirs: {
            assets: {
                root:        'assets',
                javascripts: '<%= dirs.assets.root %>/javascripts',
                stylesheets: '<%= dirs.assets.root %>/stylesheets',
                images:      '<%= dirs.assets.root %>/images'
            },
            public: {
                root:        'public',
                javascripts: '<%= dirs.public.root %>/javascripts',
                stylesheets: '<%= dirs.public.root %>/stylesheets',
                images:      '<%= dirs.public.root %>/images'
            }
        },

        /***********************************************************************
         * Copy & Clean
         ***********************************************************************/

        clean: {
            assets: ['<%= dirs.public.javascripts %>','<%= dirs.public.stylesheets %>', '<%= dirs.public.images %>'],
            dist: ['<%= dirs.public.root %>/dist/', 'conf/assets.map', '<%= dirs.public.root %>/webpack/'],
            icons: ['<%= dirs.assets.images %>/inline-svgs/compressed/*.svg'],
        },

        copy: {
            jsVendor: {
                cwd: '<%= dirs.assets.javascripts %>/vendor',
                src: ['**'],
                dest: '<%= dirs.public.javascripts %>/vendor',
                expand: true
            },
            zxcvbn: {
              src: '<%= dirs.assets.javascripts %>/bower_components/zxcvbn/dist/zxcvbn.js',
              dest: '<%= dirs.public.javascripts %>/vendor',
              expand: true,
              flatten: true
            },
            images: {
                cwd: '<%= dirs.assets.images %>',
                src: ['**'],
                dest: '<%= dirs.public.images %>',
                expand: true
            }
        },

        /***********************************************************************
         * Assets
         ***********************************************************************/

        asset_hash: {
            options: {
                preserveSourceMaps: true,
                assetMap: isDev ? false : 'conf/assets.map',
                hashLength: 8,
                algorithm: 'md5',
                srcBasePath: 'public/',
                destBasePath: 'public/',
                references: [
                    '<%= dirs.public.root %>/dist/stylesheets/**/*.css'
                ]
            },
            staticfiles: {
                files: [{
                    src: [
                        '<%= dirs.public.stylesheets %>/**/*',
                        '<%= dirs.public.javascripts %>/**/*',
                        '<%= dirs.public.images %>/**/*'
                    ],
                    dest: '<%= dirs.public.root %>/dist/'
                }]
            }
        },

        sass: {
            options: {
                sourceMap: true,
                outputStyle: 'compressed'
            },
            dist: {
                files: {
                    '<%= dirs.public.stylesheets %>/main.min.css': '<%= dirs.assets.stylesheets %>/main.scss',
                    '<%= dirs.public.stylesheets %>/ie9.min.css': '<%= dirs.assets.stylesheets %>/ie9.scss',
                    '<%= dirs.public.stylesheets %>/ie-old.min.css': '<%= dirs.assets.stylesheets %>/ie-old.scss'
                }
            }
        },

        px_to_rem: {
            dist: {
                options: {
                    map: isDev,
                    base: 16,
                    fallback: false,
                    max_decimals: 5
                },
                files: [{
                    expand: true,
                    cwd: '<%= dirs.public.stylesheets %>',
                    src: ['*.css', '!ie-old*'],
                    dest: '<%= dirs.public.stylesheets %>'
                }]
            }
        },

        postcss: {
            options: {
                map: isDev ? true : false,
                processors: [
                    require('autoprefixer-core')({browsers: ['> 5%', 'last 2 versions']}),
                    require('postcss-object-fit-images')
                ]
            },
            dist: { src: '<%= dirs.public.stylesheets %>/*.css' }
        },

        /***********************************************************************
         * Webpack
         ***********************************************************************/

        webpack: {
            options: require('./webpack.conf.js')(isDev),
            frontend: {
                output: {
                    path: path.resolve('public/'),
                    chunkFilename:  'webpack/[chunkhash].js',
                    filename: "javascripts/[name].min.js",
                    publicPath: '/assets/'
                },

                entry: {
                    main: "./main"
                }
            }
        },

        /***********************************************************************
         * SVGs
         ***********************************************************************/

        svgmin: {
            options: {
                plugins: [
                    { removeViewBox: false },
                    { removeUselessStrokeAndFill: false },
                    { removeEmptyAttrs: false },
                    { cleanUpIds: false }
                ]
            },
            dist: {
                expand: true,
                cwd: '<%= dirs.assets.images %>/inline-svgs/raw',
                src: ['*.svg'],
                dest: '<%= dirs.assets.images %>/inline-svgs/compressed'
            }
        },

        svgstore: {
            options: {
                prefix: 'icon-',
                symbol: true,
                inheritviewbox: true,
                cleanup: ['fill'],
                svg: {
                    id: 'svg-sprite',
                    width: 0,
                    height: 0
                }
            },
            icons: {
                files: {
                    '<%= dirs.assets.images %>/svg-sprite.svg': ['<%= dirs.assets.images %>/inline-svgs/compressed/*.svg']
                }
            }
        },

        /***********************************************************************
         * Watch
         ***********************************************************************/

        watch: {
            css: {
                files: ['<%= dirs.assets.stylesheets %>/**/*.scss'],
                tasks: ['build:css']
            },
            js: {
                files: ['<%= dirs.assets.javascripts %>/**/*.js'],
                tasks: ['compile:js']
            },
            jsx: {
                files: ['<%= dirs.assets.javascripts %>/**/*.jsx'],
                tasks: ['compile:js']
            }
        },

        /***********************************************************************
         * Test & Validate
         ***********************************************************************/

         /**
          * Javascript unit tests
          */
        karma: {
            options: {
                reporters: isDev ? ['dots', 'coverage'] : ['progress'],
                singleRun: singleRun
            },

            unit: {
                configFile: 'karma.conf.js',
                browsers: ['PhantomJS']
            }
        },

        /**
         * Lint Javascript sources
         */
        eslint: {
            options: {
                configFile: '.eslintrc'
            },
            app: {
                files: [{
                    expand: true,
                    cwd: '<%= dirs.assets.javascripts %>/',
                    src: [
                        'modules/**/*.js',
                        'utils/**/*.js',
                        'main.js'
                    ]
                }]
            }
        },


    });

    /***********************************************************************
     * Compile & Validate
     ***********************************************************************/

    grunt.registerTask('validate', ['eslint']);

    grunt.registerTask('build:images', ['svgSprite', 'copy:images']);
    grunt.registerTask('build:css', ['sass', 'px_to_rem', 'postcss']);
    grunt.registerTask('build:js', function(){
        if (!isDev) {
            grunt.task.run(['validate']);
        }
        grunt.task.run([
            'webpack',
            'copy:jsVendor',
            'copy:zxcvbn'
        ]);
    });

    grunt.registerTask('compile', function(){
        grunt.task.run([
            'clean:assets',
            'clean:dist',
            'build:images',
            'build:css',
            'build:js'
        ]);

        /**
         * Only version files for prod builds
         * Wipe out unused non-versioned assets for good measure
         */
        if (!isDev) {
            grunt.task.run([
                'asset_hash',
                'clean:assets'
            ]);
        }
    });

    grunt.registerTask('default', ['compile']);

    /***********************************************************************
     * Icons
     ***********************************************************************/

    grunt.registerTask('svgSprite', ['clean:icons', 'svgmin', 'svgstore']);


    /***********************************************************************
     * Test
     ***********************************************************************/

    grunt.registerTask('test', function(){
        grunt.task.run(['test:unit']);
    });
    grunt.registerTask('test:unit', function() {
        grunt.config.set('karma.options.singleRun', (singleRun === false) ? false : true);
        grunt.task.run(['karma:unit']);
    });
};
