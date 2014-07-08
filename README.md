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
	   └ GlobalAdmin.conf
       └ assets
         └ javascripts
           └ admin
             └ app.coffee
             └ main.coffee
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
       └ admin.conf
       └ admin.routes
     └ public
     └ test
   └ web
     └ ...
   └ common
     └ ...
```

Let's try to explain briefly how it is configured. For running the whole project we have the following configuration files:

* `build.sbt`: configures root project and declares every subproject.
* `conf/application.conf` _(used when whole project is running)_: the default one. It specifies the `conf/routes` and `app/Global.scala` files for the whole project.
* `conf/routes` _(used when whole project is running)_: routes file for the whole project. It simply imports the routes file of every subproject.
* `app/Global.scala` _(used when whole project is running)_: the GlobalSettings object for the whole project. It determines the subdomain for each request (admin or web) and delegates its behaviour to the corresponding subproject.

And for running each subproject independently:

* `modules/[subproject]/build.sbt`: configures the [subproject]. It also declare the corresponding `[subproject].conf` as the configuration file used for this subproject.
* `modules/[subproject]/conf/[subproject].conf` _(used when only this subproject is running)_: imports the configuration of `conf/application.conf` and specifies the routes file and GlobalSettings object for the subproject while running, testing or dist only this subproject.
* `modules/[subproject]/conf/[subproject].routes` _(used when only this subproject is running)_: routes file for this subproject.
* `modules/[subproject]/app/Global[Subproject].scala` _(used when only this subproject is running)_: the GlobalSettings object for this subproject.

The common code for every  `build.sbt` file is defined at:

* `project/Common.scala`:  contains all the shared common variables and code for sbt files.

And the rest of relevant folders and files are:

* `modules/[subproject]/app/assets/javascripts/[subproject]/`: folder for CoffeeScript files of this subproject. Take care with the last folder for avoid namespace problems while running the whole project.
* `modules/[subproject]/app/assets/stylesheets/[subproject]/`: folder for LESS files of this subproject. Take care with the last folder for avoid namespace problems while running the whole project.
* `modules/[subproject]/app/controllers/[subproject]/`: folder for the controllers of this subproject. Take care with the last folder for avoid namespace problems while running the whole project.
* `modules/[subproject]/app/controllers/[subproject]/Assets.scala`: it is necessary to implement an `object Assets extends controllers.AssetsBuilder` for every subproject.
* `modules/[subproject]/app/views/[subproject]/`: folder for the views of this subproject. Take care with the last folder for avoid namespace problems while running the whole project.
* `modules/[subproject]/public/`: folder for every public file of this subproject. Take care with possible namespace problems while running the whole project.
* `modules/[subproject]/test/`: folder for every test file for this subproject.

Please check the _Splitting the route file_ section within the documentation page about [SBT Sub-projects](http://www.playframework.com/documentation/2.3.x/SBTSubProjects).

### Webjars

The common [Webjars](http://www.webjars.org) are included within the field `Common.commonDependencies` in the file `project/Common.scala`. In our case:

    val commonDependencies = Seq(
        ...
        "org.webjars" % "jquery" % "2.1.1",
        "org.webjars" % "bootstrap" % "3.2.0"
        ...
    )

And the specific webjars for a subproject are specified in the file `modules/[subproject]/build.sbt`. For example, for the `web` subproject:

    libraryDependencies ++= Common.commonDependencies ++: Seq(
        "org.webjars" % "bootswatch-amelia" % "3.2.0"
    )

Then, to access to their resources simply remember they are inside `lib` folder. For the previous examples:

    <link rel="stylesheet" media="screen" href="@routes.Assets.versioned("lib/bootswatch-amelia/css/bootstrap.min.css")">
    <script src="@routes.Assets.versioned("lib/jquery/jquery.min.js")"></script>
    <script src="@routes.Assets.versioned("lib/bootstrap/js/bootstrap.min.js")"></script>

If you have doubts about the specific route of any webjar resource, remember it is directly downloaded within the relative folder `target/web/web-modules/main/webjars/lib`. So you can easily check the file structure that has been downloaded by the webjar.

### CoffeeScript

The corresponding plugin needs to be active in file `project/plugins.sbt`.

The specific code for each subproject will be added within its corresponding folder `modules/[subproject]/app/assets/javascripts/[subproject]/`. You could add the files directly in the folder `modules/[subproject]/app/assets/javascripts/` but you should be careful with namespace collisions.

To access to the compiled file you simply have to reference to its JS equivalent:

    <script src="@routes.Assets.versioned("javascripts/web/main.js")"></script>

For more information, go to the documentation page about [CoffeeScript](http://www.playframework.com/documentation/2.3.x/AssetsCoffeeScript).

### LESS

The corresponding plugin needs to be active in file `project/plugins.sbt`. And the next configuration has been added to every subproject to be able to work with partial LESS source files (in `project/Common.scala`):

    includeFilter in (Assets, LessKeys.less) := "*.less"
    excludeFilter in (Assets, LessKeys.less) := "_*.less"

With that, every LESS file not prepended by an underscore (`_`) will be compiled, and they could import the code from the LESS files prepended by an underscore.

The common LESS files are in the subproject `common`, within the folder `modules/common/app/assets/stylesheets/common/`. And the specific code for each subproject will be added within its corresponding folder `modules/[subproject]/app/assets/stylesheets/[subproject]/`. You could add the files directly in the folder `modules/[subproject]/app/assets/stylesheets/` but you should be careful with namespace collisions.

To access to the compiled file you simply have to reference to its CSS equivalent:

    <link rel="stylesheet" media="screen" href="@routes.Assets.versioned("stylesheets/web/main.css")">

For more information, go to the documentation page about [LESS](http://www.playframework.com/documentation/2.3.x/AssetsLess).

### Public files

You can put the common public files in the subproject `common`, within the folder `modules/common/public/`. And the specific code for each subproject will be added within its corresponding folder `modules/[subproject]/public/`. Take care with possible namespace problems while running the whole project.

### Assets: RequireJS, Digest, Etag, Gzip, Fingerprint

To configure all of these features, for each service (`web` and `admin`) we have the following:

    pipelineStages := Seq(rjs, digest, gzip)
	RjsKeys.baseUrl := s"javascripts/$theName"
	RjsKeys.paths += ("common" -> ("../../../common/app/assets/javascripts/common" -> "empty:"))

The first line declares the asset pipeline. The second one establishes the corresponding folder to each service as the _RequireJS baseUrl_. Each one has its own subfolder within folder `javascripts` to avoid namespace collisions. Finally, the third one configures a common folder to share _RequireJS modules_ between subprojects using the `common` one.

Then you can put the common RequireJS modules in the subproject `common`, within the folder `modules/common/app/assets/javascripts/common/`. And the specific code for each subproject will be added within its corresponding folder `modules/[subproject]/app/assets/javascripts/[subproject]/`.

Now we just simply need to declare the RequireJS as:

    <script data-main="@routes.Assets.versioned("javascripts/web/main.js")" src="@routes.Assets.versioned("lib/requirejs/require.js")" type="text/javascript"></script>

For more information, go to the documentation page about [Assets](http://www.playframework.com/documentation/2.3.x/Assets), the tutorial `play-2.3-highlights` in Activator UI, or the website of [RequireJS](http://requirejs.org).

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

If you want to run only one subproject separately, you have to get into the subproject and run using its specific configuration file:

    [play-multidomain-seed] $ project admin
	[play-multidomain-seed-admin] $ run -Dconfig.resource=admin.conf


### Test

Each subproject has its own test files within the folder `modules/[subproject]/test`.

To run the tests for every subproject at once, simply execute:

    [play-multidomain-seed] $ test

And for a unique subproject, get into it and test it:

    [play-multidomain-seed] $ project admin
	[play-multidomain-seed-admin] $ test

In that case is not neccesary to specify the __conf__ file because it is done with the configuration (within `Common.scala`):

    javaOptions in Test += s"-Dconfig.resource=${module}.conf"

### Production

Simply execute:

    $ activator dist

or

    [play-multidomain-seed] $ dist

Now you have a zip file for each module.

    /play-multidomain-seed/modules/web/target/universal/play-multidomain-seed-web-1.0-SNAPSHOT.zip
    /play-multidomain-seed/modules/admin/target/universal/play-multidomain-seed-admin-1.0-SNAPSHOT.zip

So you can extract wherever you want and execute them separately. For example with:

    ./play-multidomain-seed-admin-1.0-SNAPSHOT/bin/play-multidomain-seed-admin -Dconfig.resource=admin.conf -Dhttp.port=9001 -Dapplication.secret=abcdefghijk &

Note I have added the `&` at the end to run the app in the background. The PID will be stored in `RUNNING_PID` file, so when you want to stop the app, just execute:

    kill $(cat path/to/RUNNING_PID)

Please, check the documentation about [Production Configuration](http://www.playframework.com/documentation/2.3.x/ProductionConfiguration) for more parameters. And also check about [Application Secret](http://www.playframework.com/documentation/2.3.x/ApplicationSecret).

### Thanks to

http://www.playframework.com/documentation/2.3.x/SBTSubProjects

http://eng.kifi.com/multi-project-deployment-in-play-framework/ -> https://github.com/kifi/multiproject

http://parleys.com/play/527f7a92e4b084eb60ac7732/chapter17/about