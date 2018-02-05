## Multidomain Seed [Play 2.6 - Scala]

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
  * [Assets with RequireJS, Digest, Etag, Gzip, Fingerprint](http://www.playframework.com/documentation/2.5.x/Assets).
* It shoud explain:
  * How to share every common code to avoid duplications (models, controllers, views, CoffeeScript, LESS, ...).
  * How to use it for development, test and production.


And please, don't forget starring this project if you consider it has been useful for you.

Also check my other projects:

* [Play Multidomain Auth [Play 2.5 - Scala]](https://github.com/adrianhurt/play-multidomain-auth)
* [Play-Bootstrap - Play library for Bootstrap [Scala & Java]](https://adrianhurt.github.io/play-bootstrap)
* [Play Silhouette Credentials Seed [Play 2.5 - Scala]](https://github.com/adrianhurt/play-silhouette-credentials-seed)
* [Play API REST Template [Play 2.5 - Scala]](https://github.com/adrianhurt/play-api-rest-seed)

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
   └ RequestHandler.scala
   └ ErrorHandler.scala
 └ conf
   └ root-dev.conf
 └ project
   └ build.properties
   └ plugins.sbt
   └ Common.scala
 └ modules
   └ admin
     └ build.sbt
     └ app
       └ assets
         └ javascripts
           └ main-admin.coffee
           └ admin.coffee
           └ admin
             └ otherLib.coffee
         └ stylesheets
           └ main.less
       └ controllers
         └ Application.scala
         └ Assets.scala
       └ models
         └ Models.scala
       └ views
	       └ admin
             └ index.scala.html
             └ main.scala.html
       └ utils
         └ ErrorHandler.scala
     └ conf
       └ admin-dev.conf
       └ admin-prod.conf
       └ admin.routes
     └ public
       └ images
     └ test
   └ web
     └ ...
   └ common
     └ ...
```

Let's try to explain briefly how it is configured. For running the whole project we have the following configuration files:

* `build.sbt`: configures root project and declares every subproject.
* `conf/root-dev.conf` _(used when whole project is running)_: the default one. In the next section it is explained in detail.
* `conf/routes` _(used when whole project is running)_: routes file for the whole project. It simply imports the routes file of every subproject.
* `app/RequestHandler.scala` _(used when whole project is running)_: the RequestHandler object for the whole project. It determines the subdomain for each request (admin or web) and delegates its behaviour to the corresponding subproject.
* `app/ErrorHandler.scala` _(used when whole project is running)_: the ErrorHandler object for the whole project. It determines the subdomain for each request (admin or web) and delegates its behaviour to the corresponding subproject.

And for running each subproject independently:

* `modules/[subproject]/build.sbt`: configures the [subproject].
* `modules/[subproject]/conf/[subproject]-dev.conf` _(used when only this subproject is running)_: the default one, it declares the routes file for the subproject while running or testing only this subproject.
* `modules/[subproject]/conf/[subproject]-prod.conf` _(used when only this subproject is running)_: it declares the routes file for the subproject while distributing only this subproject.
* `modules/[subproject]/conf/[subproject].routes` _(used when only this subproject is running)_: routes file for this subproject.
* `modules/[subproject]/app/utils/ErrorHandler.scala` _(used when only this subproject is running)_: the ErrorHandler object for this subproject.

The common code for every  `build.sbt` file is defined at:

* `project/Common.scala`:  contains all the shared common variables and code for sbt files.

And the rest of relevant folders and files are:

* `modules/[subproject]/app/assets/javascripts/`: folder for CoffeeScript files of this subproject.
* `modules/[subproject]/app/assets/stylesheets/`: folder for LESS files of this subproject.
* `modules/[subproject]/app/controllers/`: folder for the controllers of this subproject.
* `modules/[subproject]/app/controllers/Assets.scala`: it is necessary to implement an `object Assets extends controllers.AssetsBuilder` for every subproject.
* `modules/[subproject]/app/views/[subproject]/`: folder for the views of this subproject.
* `modules/[subproject]/public/`: folder for every public file of this subproject.
* `modules/[subproject]/test/`: folder for every test file for this subproject.

Please check the _Splitting the route file_ section within the documentation page about [SBT Sub-projects](http://www.playframework.com/documentation/2.5.x/SBTSubProjects).

### Configuration files

As we want to run or test the whole project and also run, test or dist _admin_ and _web_ subprojects, we have several configuration files. Each one has its own particular purpose:

* `conf/root-dev.conf`: the configuration file that is called by default when the whole project is running. It simply includes the `shared.dev.conf` file.
* `conf/shared.dev.conf`: declares all the development configuration shared for the whole project and every subproject.
* `conf/shared.prod.conf`: includes the `shared.dev.conf` file and overrides every configuration that is specific for production and it is shared for the whole project and every subproject.

* `modules/[subproject]/conf/[subproject].conf`: declares the specific configuration for this subproject for development or production. It must declare the route file for this subproject.
* `modules/[subproject]/conf/[subproject]-dev.conf`: the configuration file that is called by default when the [subproject] is running. It simply includes the `shared.dev.conf` and `[subproject].conf` files.
* `modules/[subproject]/conf/[subproject]-prod.conf`: declares the specific configuration for this subproject for production. It includes the `shared.prod.conf` and `[subproject].conf` files.

As you can see, we have some shared configuration files: `shared.dev.conf` and `shared.prod.conf`. And we need them for every project (the root one and the subprojects).
Both files shoud be replicated within each `conf` directory for every subproject. But there is an easy way to avoid code replication and minimize errors, and it's defining a new [resourceGenerator](http://www.scala-sbt.org/release/docs/Howto-Generating-Files.html#Generate+resources) within `project/Common.scala`.
Then, each time the code is compiled, every `shared.*.conf` file will be replicated within the corresponding path. Note these files will __only__ be generated within the `target` file.

It has been added a key called `this.file` in many of the configuration files and it is shown in the index web page when you run it. Please, play with it to see how it is overridden by each configuration file depending the project and mode (dev or prod) you are running.

The corresponding configuration file is correctly taken for each case thanks to the settings lines in `Common.scala`:

    javaOptions += s"-Dconfig.resource=$module-dev.conf"

### Assets: RequireJS, Digest, Etag, Gzip, Fingerprint

To configure all of these features, for each service (`web` and `admin`) we have the following:

    pipelineStages := Seq(rjs, digest, gzip)
    RjsKeys.mainModule := s"main-$module"

The first line declares the asset pipeline. The second one establishes the corresponding _RequireJS_ main config file to each module.

Then you can put the common RequireJS modules in the subproject `common`, within the folder `modules/common/app/assets/javascripts/common/`. And the specific code for each subproject will be added within its corresponding folder `modules/[subproject]/app/assets/javascripts/`. Take care with possible namespace problems while running the whole project. In the example, the subproject _admin_ has other _RJS module_ within subfolder _admin_.

The common Assets are packaged as Webjars for the other subprojects that depend on it, so you must indicate the corresponding _RequireJS path_ to the common lib in the _RJS config file_ as:

    require.config
      paths:
        common: "../lib/common/javascripts"
        jquery: "../lib/jquery/jquery"
        ...

Now we just simply need to declare the RequireJS as:

    <script data-main="@routes.Assets.versioned("javascripts/main-web.js")" src="@routes.Assets.versioned("lib/requirejs/require.js")" type="text/javascript"></script>

For more information, go to the documentation page about [Assets](http://www.playframework.com/documentation/2.5.x/Assets), the tutorial `play-2.3-highlights` in Activator UI, or the website of [RequireJS](http://requirejs.org).

#### Custom AssetsBuilder

In order to avoid code like this:

    href="@routes.Assets.versioned("images/favicon.png")"
    href="@routes.Assets.versioned("stylesheets/main.css")">
    data-main="@routes.Assets.versioned("javascripts/main-web.js")"
    src="@routes.Assets.versioned("lib/requirejs/require.js")"
    src="@routes.Assets.versioned("lib/common/images/logo.png")"

because it can be very tedious to remember the specific path for every resource depending of its type or if it's from the common subproject or not, I prefer using this syntax:

    href="@routes.Assets.img("favicon.png")"
    href="@routes.Assets.css("main.css")">
    data-main="@routes.Assets.js("main-web.js")"
    src="@routes.Assets.lib("requirejs/require.js")"
    src="@routes.Assets.commonImg("logo.png")"

To get that we only need to define a custom `AssetsBuilder` class (you can see it in `modules/common/app/controllers/Assets.scala`).

    package controllers.common
    class Assets(errorHandler: DefaultHttpErrorHandler) extends AssetsBuilder(errorHandler) {
      def public (path: String, file: Asset) = versioned(path, file)
      def lib (path: String, file: Asset) = versioned(path, file)
      def css (path: String, file: Asset) = versioned(path, file)
      def commonCss (path: String, file: Asset) = versioned(path, file)
      def js (path: String, file: Asset) = versioned(path, file)
      def commonJs (path: String, file: Asset) = versioned(path, file)
      def img (path: String, file: Asset) = versioned(path, file)
      def commonImg (path: String, file: Asset) = versioned(path, file)
    }

Add a simple `Assets` class within the `controllers` folder of each subproject:

    package controllers.web
    class Assets @Inject() (val errorHandler: web.ErrorHandler) extends controllers.common.Assets(errorHandler)

And add the following in the routes files:

    GET     /public/*file        controllers.web.Assets.public(path="/public", file: Asset)
    GET     /lib/*file           controllers.web.Assets.lib(path="/public/lib", file: Asset)
    GET     /css/*file           controllers.web.Assets.css(path="/public/stylesheets", file: Asset)
    GET     /js/*file            controllers.web.Assets.js(path="/public/javascripts", file: Asset)
    GET     /img/*file           controllers.web.Assets.img(path="/public/images", file: Asset)
    GET     /common/css/*file    controllers.web.Assets.commonCss(path="/public/lib/common/stylesheets", file: Asset)
    GET     /common/js/*file     controllers.web.Assets.commonJs(path="/public/lib/common/javascripts", file: Asset)
    GET     /common/img/*file    controllers.web.Assets.commonImg(path="/public/lib/common/images", file: Asset)

#### Public files

You can put the common public files in the subproject `common`, within the folder `modules/common/public/`. The common Assets are packaged as Webjars for the other subprojects that depend on it, so you must access to them through their correspoding lib folder:

    <img src="@routes.Assets.commonImg("play.svg")"></img>

And the specific code for each subproject will be added within its corresponding folder `modules/[subproject]/public/`.

#### Shared resources

If you have shared resources between your subprojects, like for example uploaded images from your users, you need to render or download them from a shared folder. Note you can't consider a shared resource like a asset.

The process is very similar than the custom `AssetsBuilder`:

    package controllers.common
    abstract class SharedResources(errorHandler: DefaultHttpErrorHandler, conf: Configuration) extends Controller with utils.ConfigSupport {
      private lazy val path = confRequiredString("rsc.folder")
      def rsc(filename: String) = Action.async { implicit request =>  ... render the file ... }
    }

Add a simple `SharedResources` class within the `controllers` folder of each subproject:

    package controllers.web
    class SharedResources @Inject() (val errorHandler: web.ErrorHandler, val conf: Configuration) extends controllers.common.SharedResources(errorHandler, conf)

And add the following in the routes files:

    GET     /rsc/*file         controllers.web.SharedResources.rsc(file: String)

_Note:_ remember to set the absolute path to common resources folder with `rsc.folder` at the configuration file. Specially for production.

### RequestHandler

We need a global `RequestHandler` to run the whole project and get to things:

* Determine the subdomain for each request (`admin` or `web`) and delegate its behaviour to the corresponding subproject.
* Rewrite the urls for the `public`, `css`, `js` and `img` assets for the corresponding subproject. This is because for the root project these resources are located at `public/lib/[subproject]/`.

These things are done overriding the `routeRequest` method of the `RequestHandler`.

### ErrorHandler

As we did with `RequestHandler`, we also need to do the same for `ErrorHandler`. In this case, we have a global `ErrorHandler` and  specific `admin.ErrorHandler` and `web.ErrorHandler`.
When running the whole project, the global one will determine the subdomain for each request and delegate its behaviour to the corresponding subproject. Remember that is necessary to declare each specific `ErrorHandler` withing the corresponding configuration file.

### Webjars

The common [Webjars](http://www.webjars.org) are included within the field `Common.commonDependencies` in the file `project/Common.scala`. In our case:

    val commonDependencies = Seq(
      ...
      "org.webjars" % "jquery" % "3.1.0",
      "org.webjars" % "bootstrap" % "3.3.7-1" exclude("org.webjars", "jquery"),
      "org.webjars" % "requirejs" % "2.3.1",
      ...
    )

And the specific webjars for a subproject are declared in the file `modules/[subproject]/build.sbt`. For example, for the `web` subproject:

    libraryDependencies ++= Common.commonDependencies ++: Seq(
      "org.webjars" % "bootswatch-cerulean" % "3.3.5+4"
    )

Then, to access to their resources simply remember they are inside `lib` folder. For the previous examples:

    <link rel="stylesheet" media="screen" href="@routes.Assets.lib("bootswatch-cerulean/css/bootstrap.min.css")">
    <script src="@routes.Assets.lib("jquery/jquery.min.js")"></script>
    <script src="@routes.Assets.lib("bootstrap/js/bootstrap.min.js")"></script>

If you have doubts about the specific route of any webjar resource, remember it is directly downloaded within the relative folder `target/web/web-modules/main/webjars/lib`. So you can easily check the file structure that has been downloaded by the webjar.

### CoffeeScript

The corresponding plugin needs to be active in file `project/plugins.sbt`.

The common CoffeeScript files are in the subproject `common`, within the folder `modules/common/app/assets/javascripts`. And the specific code for each subproject will be added within its corresponding folder `modules/[subproject]/app/assets/javascripts/`.

To access to the compiled file you simply have to reference to its JS equivalent:

    <script src="@routes.Assets.js("main.js")"></script>

For more information, go to the documentation page about [CoffeeScript](http://www.playframework.com/documentation/2.5.x/AssetsCoffeeScript).

### LESS

The corresponding plugin needs to be active in file `project/plugins.sbt`. And the next configuration has been added to every subproject to be able to work with partial LESS source files (in `project/Common.scala`):

    includeFilter in (Assets, LessKeys.less) := "*.less"
    excludeFilter in (Assets, LessKeys.less) := "_*.less"

With that, every LESS file not prepended by an underscore (`_`) will be compiled, and they could import the code from the LESS files prepended by an underscore.

The common LESS files are in the subproject `common`, within the folder `modules/common/app/assets/stylesheets/`. And the specific code for each subproject will be added within its corresponding folder `modules/[subproject]/app/assets/stylesheets/`.

To import a common LESS file, import it directly as (you can check an example in `modules/admin/app/assets/stylesheets/_variables.less`):

    @import "../../../../../common/app/assets/stylesheets/_common.less";

To access to the compiled file you simply have to reference to its CSS equivalent:

    <link rel="stylesheet" media="screen" href="@routes.Assets.css("main.css")">

For more information, go to the documentation page about [LESS](http://www.playframework.com/documentation/2.5.x/AssetsLess).

### Internationalization: how to split messages files

Well... it's a tricky one. We need the corresponding messages files within the conf directory of each subproject.  But we have 2 problems:

* What about sharing some message definitions from common subproject?
* We also need the corresponding messages files for root project with all the message definitions.

In fact, the main purpose of this project is to show you how to share and reduce your code, so let's go.

To resolve that, we need to take advantage of `sbt`. So a new [`resourceGenerator`](http://www.scala-sbt.org/release/docs/Howto-Generating-Files.html#Generate+resources) has been defined in `project/Common.scala` that is executed each time the project is compiled. It works in the following way:

* Put your shared messages files within `modules/common/conf/messages/`.
* Put your specific messages files for each services within `modules/[subproject]/conf/messages/`.
* Use the `messagesFilesFrom` argument of `appSettings` and `serviceSettings` methods of `Common.scala` to specify the list and priority of the corresponding subprojects messages files used for each one. In the example, for `web` subproject `messagesFilesFrom = Seq("common", "web")` and for root project `messagesFilesFrom = Seq("common", "admin", "web")`.
* Each time the code is compiled, every needed messages file will be generated appending the corresponding previous ones. Note these files will __only__ be generated within the `target` file.

__*Assumption*__: if there are 2 coincidences within the same file, the last one will be taken. So it is ordered from lower to higher priority.

### Development

First of all, to get access to `admin` subdomain you will need modify your `/etc/hosts` files (or the equivalent in your S.O.) to map the next URLs to `localhost` or (`127.0.0.1`). For example, add the following lines:

    127.0.0.1	myweb.com
    127.0.0.1	www.myweb.com
    127.0.0.1	admin.myweb.com

Then, simply execute:

    $ activator run

or

    [play-multidomain-seed] $ run

And that's all! The whole project will run using the `conf/root-dev.conf` file enabling all the services at once. You can go with your browser and check the URLs:

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

_Note:_ remember to set the absolute path to common resources folder with `rsc.folder` at the configuration file.

Simply execute:

    $ activator dist

or

    [play-multidomain-seed] $ dist

Now you have a zip file for each module.

    /play-multidomain-seed/modules/web/target/universal/web-1.0-SNAPSHOT.zip
    /play-multidomain-seed/modules/admin/target/universal/admin-1.0-SNAPSHOT.zip

So you can extract wherever you want and execute them separately. For example with:

    ./admin-1.0-SNAPSHOT/bin/admin -Dconfig.resource=admin-prod.conf -Dhttp.port=9001 -Dapplication.secret=abcdefghijk &

Note it is added the `&` at the end to run the app in the background. The PID will be stored in `RUNNING_PID` file, so when you want to stop the app, just execute:

    kill $(cat path/to/RUNNING_PID)

If you would like to test the whole project in production mode, you should be able to execute the start command as:

    [play-multidomain-seed] $ start

Please, check the documentation about [Production Configuration](http://www.playframework.com/documentation/2.5.x/ProductionConfiguration) for more parameters. And also check about [Application Secret](http://www.playframework.com/documentation/2.5.x/ApplicationSecret).

### Thanks to

http://www.playframework.com/documentation/2.5.x/SBTSubProjects

http://eng.kifi.com/multi-project-deployment-in-play-framework/ -> https://github.com/kifi/multiproject

http://parleys.com/play/527f7a92e4b084eb60ac7732/chapter17/about