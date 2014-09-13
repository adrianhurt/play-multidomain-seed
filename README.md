## Multidomain Seed [Play 2.3 - Scala]

__Note:__ All this information is also available as a tutorial if you run the app using [Activator UI](http://typesafe.com/platform/getstarted).

Let's suppose you want to develop a whole project which has two different services: the typical public webpage and the private admin one. You also want a specific subdomain for the admin webpage, so we will have:

* `admin.myweb.com`: private administration webpage.
* `www.myweb.com` or `myweb.com`: public webpage.

And let's also suppose you prefer to have these services isolated in your production server. So you would be able to manage them separately (with different PIDs, different ports, different resources…).

Then, we have the following objectives:

* Development should be simple. `activator run` should be enough to run all services at the same time.
* Common code, dependencies and modules should be easily shared.
* We should be able to compile, test and run each service separately in development and production.
* We should distribute each service separately.
* It should be a template ready to use with the following features:
  * [Webjars](http://www.webjars.org).
  * [CoffeeScript](http://coffeescript.org) and [LESS](http://lesscss.org) Assets.
  * [Assets with RequireJS, Digest, Etag, Gzip, Fingerprint](http://www.playframework.com/documentation/2.3.x/Assets).
* It shoud explain:
  * How to share every common code to avoid duplications (models, controllers, views, CoffeeScript, LESS, ...).
  * How to use it for development, test and production.

### Multiproject

This template has 3 subprojects:

* `web`: will contain all the specific code for the public webpage service.
* `admin`: will contain all the specific code for the private administration webpage service.
* `common`: will contain all the common code shared between the other subprojects.

Obviously, this is a template, so you can easily change its names or add more modules. Once you understand how it works you will find it easy to modify.

This is the basic structure of the whole project:

```
play-multidomain-seed
 └ build.sbt
 └ app
   └ Global.scala
 └ conf
   └ application.conf
   └ routes
 └ project
   └ build.properties
   └ plugins.sbt
   └ Common.scala
 └ modules
   └ admin
     └ build.sbt
     └ app
	   └ GlobalAdmin.scala
       └ assets
         └ javascripts
           └ main-admin.coffee
           └ admin.coffee
           └ admin
             └ otherLib.coffee
         └ stylesheets
           └ admin
             └ main.less
       └ controllers
         └ admin
           └ Application.scala
           └ Assets.scala
       └ models
         └ admin
           └ Models.scala
       └ views
	       └ admin
             └ index.scala.html
             └ main.scala.html
     └ conf
       └ application.conf
       └ prod.conf
       └ admin.routes
     └ public
       └ images
         └ admin
     └ test
   └ web
     └ ...
   └ common
     └ ...
```

Let's try to explain briefly how it is configured. For running the whole project we have the following configuration files:

* `build.sbt`: configures root project and declares every subproject.
* `conf/application.conf` _(used when whole project is running)_: the default one. In the next section it is explained in detail.
* `conf/routes` _(used when whole project is running)_: routes file for the whole project. It simply imports the routes file of every subproject.
* `app/Global.scala` _(used when whole project is running)_: the GlobalSettings object for the whole project. It determines the subdomain for each request (admin or web) and delegates its behaviour to the corresponding subproject.

And for running each subproject independently:

* `modules/[subproject]/build.sbt`: configures the [subproject].
* `modules/[subproject]/conf/application.conf` _(used when only this subproject is running)_: the default one, it declares the routes file and GlobalSettings object for the subproject while running, testing or distributing only this subproject.
* `modules/[subproject]/conf/[subproject].routes` _(used when only this subproject is running)_: routes file for this subproject.
* `modules/[subproject]/app/Global[Subproject].scala` _(used when only this subproject is running)_: the GlobalSettings object for this subproject.

The common code for every  `build.sbt` file is defined at:

* `project/Common.scala`:  contains all the shared common variables and code for sbt files.

And the rest of relevant folders and files are:

* `modules/[subproject]/app/assets/javascripts/`: folder for CoffeeScript files of this subproject. Take care with the last folder for avoid namespace problems while running the whole project.
* `modules/[subproject]/app/assets/stylesheets/[subproject]/`: folder for LESS files of this subproject. Take care with the last folder for avoid namespace problems while running the whole project.
* `modules/[subproject]/app/controllers/[subproject]/`: folder for the controllers of this subproject. Take care with the last folder for avoid namespace problems while running the whole project.
* `modules/[subproject]/app/controllers/[subproject]/Assets.scala`: it is necessary to implement an `object Assets extends controllers.AssetsBuilder` for every subproject.
* `modules/[subproject]/app/views/[subproject]/`: folder for the views of this subproject. Take care with the last folder for avoid namespace problems while running the whole project.
* `modules/[subproject]/public/`: folder for every public file of this subproject. Take care with possible namespace problems while running the whole project.
* `modules/[subproject]/test/`: folder for every test file for this subproject.

Please check the _Splitting the route file_ section within the documentation page about [SBT Sub-projects](http://www.playframework.com/documentation/2.3.x/SBTSubProjects).

### Configuration files

As we want to run or test the whole project and also run, test or dist _admin_ and _web_ subprojects, we have several configuration files. Each one has its own particular purpose:

* `conf/application.conf`: the configuration file that is called by default when the whole project is running. It simply includes the `shared.dev.conf` file.
* `conf/shared.dev.conf`: declares all the development configuration shared for the whole project and every subproject.
* `conf/shared.prod.conf`: includes the `shared.dev.conf` file and overrides every configuration that is specific for production and it is shared for the whole project and every subproject.

* `modules/[subproject]/conf/application.conf`: the configuration file that is called by default when the [subproject] is running. It simply includes the `shared.dev.conf` and `[subproject].conf` files.
* `modules/[subproject]/conf/[subproject].conf`: declares the specific configuration for this subproject for development or production. It must declare the Global object and route file for this subproject.
* `modules/[subproject]/conf/prod.conf`: declares the specific configuration for this subproject for production. It includes the `shared.prod.conf` and `[subproject].conf` files.
* `modules/[subproject]/conf/shared.dev.conf`: it is simply a copy of `conf/shared.dev.conf`. It must be copied here to be available for production distribution.
* `modules/[subproject]/conf/shared.prod.conf`: it is simply a copy of `conf/shared.prod.conf`. It must be copied here to be available for production distribution.

It has been added a key called `this.file` in many of the configuration files and it is shown in the index web page when you run it. Please, play with it to see how it is overridden by each configuration file depending the project and mode (dev or prod) you are running.

__Tip:__ as files `shared.dev.conf` and `shared.prod.conf` for every subproject are the same as the general ones, you can use _aliases_ or _symbolic links_ for them in order to avoid to maintain all of them.

### Webjars

The common [Webjars](http://www.webjars.org) are included within the field `Common.commonDependencies` in the file `project/Common.scala`. In our case:

    val commonDependencies = Seq(
        ...
        "org.webjars" % "jquery" % "2.1.1",
        "org.webjars" % "bootstrap" % "3.2.0"
        ...
    )

And the specific webjars for a subproject are declared in the file `modules/[subproject]/build.sbt`. For example, for the `web` subproject:

    libraryDependencies ++= Common.commonDependencies ++: Seq(
        "org.webjars" % "bootswatch-cerulean" % "3.2.0-1"
    )

Then, to access to their resources simply remember they are inside `lib` folder. For the previous examples:

    <link rel="stylesheet" media="screen" href="@routes.Assets.versioned("lib/bootswatch-cerulean/css/bootstrap.min.css")">
    <script src="@routes.Assets.versioned("lib/jquery/jquery.min.js")"></script>
    <script src="@routes.Assets.versioned("lib/bootstrap/js/bootstrap.min.js")"></script>

If you have doubts about the specific route of any webjar resource, remember it is directly downloaded within the relative folder `target/web/web-modules/main/webjars/lib`. So you can easily check the file structure that has been downloaded by the webjar.

### CoffeeScript

The corresponding plugin needs to be active in file `project/plugins.sbt`.

The common CoffeeScript files are in the subproject `common`, within the folder `modules/common/app/assets/javascripts`. And the specific code for each subproject will be added within its corresponding folder `modules/[subproject]/app/assets/javascripts/`.  Take care with possible namespace problems while running the whole project, so it's better to put any file within a subfolder.

To access to the compiled file you simply have to reference to its JS equivalent:

    <script src="@routes.Assets.versioned("javascripts/web/main.js")"></script>

For more information, go to the documentation page about [CoffeeScript](http://www.playframework.com/documentation/2.3.x/AssetsCoffeeScript).

### LESS

The corresponding plugin needs to be active in file `project/plugins.sbt`. And the next configuration has been added to every subproject to be able to work with partial LESS source files (in `project/Common.scala`):

    includeFilter in (Assets, LessKeys.less) := "*.less"
    excludeFilter in (Assets, LessKeys.less) := "_*.less"

With that, every LESS file not prepended by an underscore (`_`) will be compiled, and they could import the code from the LESS files prepended by an underscore.

The common LESS files are in the subproject `common`, within the folder `modules/common/app/assets/stylesheets/`. And the specific code for each subproject will be added within its corresponding folder `modules/[subproject]/app/assets/stylesheets/[subproject]/`. You could add the files directly in the folder `modules/[subproject]/app/assets/stylesheets/` but you should be careful with namespace collisions.

To import a common LESS file, import it directly as (you can check an example in `modules/admin/app/assets/stylesheets/admin/_variables.less`):

    @import "../../../../../common/app/assets/stylesheets/_common.less";

To access to the compiled file you simply have to reference to its CSS equivalent:

    <link rel="stylesheet" media="screen" href="@routes.Assets.versioned("stylesheets/web/main.css")">

For more information, go to the documentation page about [LESS](http://www.playframework.com/documentation/2.3.x/AssetsLess).

### Assets: RequireJS, Digest, Etag, Gzip, Fingerprint

To configure all of these features, for each service (`web` and `admin`) we have the following:

    pipelineStages := Seq(rjs, digest, gzip)
	RjsKeys.mainModule := s"main-$module"

The first line declares the asset pipeline. The second one establishes the corresponding _RequireJS_ main config file to each module.

Then you can put the common RequireJS modules in the subproject `common`, within the folder `modules/common/app/assets/javascripts/common/`. And the specific code for each subproject will be added within its corresponding folder `modules/[subproject]/app/assets/javascripts/`. Take care with possible namespace problems while running the whole project. In the example, the subproject _admin_ has other _RJS module_ within subfolder _admin_.

The common Assets are packaged as Webjars for the other subprojects that depend on it, so you must indicate the corresponding _RequireJS path_ to the common lib in the _RJS config file_ as:

    require.config {
      paths: {
        common: "../lib/common/javascripts"
      }
    }

Now we just simply need to declare the RequireJS as:

    <script data-main="@routes.Assets.versioned("javascripts/main-web.js")" src="@routes.Assets.versioned("lib/requirejs/require.js")" type="text/javascript"></script>

For more information, go to the documentation page about [Assets](http://www.playframework.com/documentation/2.3.x/Assets), the tutorial `play-2.3-highlights` in Activator UI, or the website of [RequireJS](http://requirejs.org).

#### Public files

You can put the common public files in the subproject `common`, within the folder `modules/common/public/`. The common Assets are packaged as Webjars for the other subprojects that depend on it, so you must access to them through their correspoding lib folder:

    <img src="@routes.Assets.versioned("lib/common/images/normal-mini.png")"></img>

And the specific code for each subproject will be added within its corresponding folder `modules/[subproject]/public/`. Take care with possible namespace problems while running the whole project.

### Development

First of all, to get access to `admin` subdomain you will need modify your `/etc/hosts` files (or the equivalent in your S.O.) to map the next URLs to `localhost` or (`127.0.0.1`). For example, add the following lines:

    127.0.0.1	myweb.com
    127.0.0.1	www.myweb.com
    127.0.0.1	admin.myweb.com

Then, simply execute:

    $ activator run

or

    [play-multidomain-seed] $ run

And that's all! The whole project will run using the `conf/application.conf` file enabling all the services at once. You can go with your browser and check the URLs:

* `myweb.com:9000` or `www.myweb.com:9000`: public webpage
* `admin.myweb.com:9000`: private admin webpage

As you can see, you must add the default `9000` port, but you can use the port you want with the parameter with `activator run -Dhttp.port=9001`.

If you want to run only one subproject separately, you have to get into the subproject and run:

    [play-multidomain-seed] $ project admin
	[admin] $ run


### Test

Each subproject has its own test files within the folder `modules/[subproject]/test`.

To run the tests for every subproject at once, simply execute:

    [play-multidomain-seed] $ test

And for a unique subproject, get into it and test it:

    [play-multidomain-seed] $ project admin
	[admin] $ test

### Production

Simply execute:

    $ activator dist

or

    [play-multidomain-seed] $ dist

Now you have a zip file for each module.

    /play-multidomain-seed/modules/web/target/universal/web-1.0-SNAPSHOT.zip
    /play-multidomain-seed/modules/admin/target/universal/admin-1.0-SNAPSHOT.zip

So you can extract wherever you want and execute them separately. For example with:

    ./admin-1.0-SNAPSHOT/bin/admin -Dconfig.resource=prod.conf -Dhttp.port=9001 -Dapplication.secret=abcdefghijk &

Note it is added the `&` at the end to run the app in the background. The PID will be stored in `RUNNING_PID` file, so when you want to stop the app, just execute:

    kill $(cat path/to/RUNNING_PID)

If you would like to test the whole project in production mode, you should be able to execute the start command as:

    [play-multidomain-seed] $ start

Please, check the documentation about [Production Configuration](http://www.playframework.com/documentation/2.3.x/ProductionConfiguration) for more parameters. And also check about [Application Secret](http://www.playframework.com/documentation/2.3.x/ApplicationSecret).

### Thanks to

http://www.playframework.com/documentation/2.3.x/SBTSubProjects

http://eng.kifi.com/multi-project-deployment-in-play-framework/ -> https://github.com/kifi/multiproject

http://parleys.com/play/527f7a92e4b084eb60ac7732/chapter17/about