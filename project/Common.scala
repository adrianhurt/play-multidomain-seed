import sbt._
import Keys._
import play.sbt.PlayImport._
import play.sbt.routes.RoutesKeys.routesGenerator
import com.typesafe.sbt.web.SbtWeb.autoImport.{Assets, pipelineStages}
import com.typesafe.sbt.less.Import.LessKeys
import com.typesafe.sbt.rjs.Import.{rjs, RjsKeys}
import com.typesafe.sbt.digest.Import.digest
import com.typesafe.sbt.gzip.Import.gzip
import com.typesafe.config._

object Common {
  def appName = "play-multidomain-seed"
	
  // Common settings for every project
  def settings (theName: String) = Seq(
    name := theName,
    organization := "com.myweb",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.12.3",
    // suppress API doc generation
    sources in (Compile, doc) := Seq.empty,
    publishArtifact in (Compile, packageDoc) := false,
    scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked", "-language:reflectiveCalls", "-language:postfixOps", "-language:implicitConversions"),
    resolvers ++= Seq(
      "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases",
      "Atlassian Releases" at "https://maven.atlassian.com/public/",
      Resolver.jcenterRepo,
      Resolver.sonatypeRepo("snapshots")
    )
  )
  // Settings for the app, i.e. the root project
  def appSettings (messagesFilesFrom: Seq[String]) = settings(appName) ++: Seq(
    javaOptions += s"-Dconfig.resource=root-dev.conf",
    messagesGenerator in Compile := messagesGenerate(messagesFilesFrom, baseDirectory.value, resourceManaged.value, streams.value.log),
    resourceGenerators in Compile += (messagesGenerator in Compile)
  )
  // Settings for every module, i.e. for every subproject
  def moduleSettings (module: String) = settings(module) ++: Seq(
    javaOptions += s"-Dconfig.resource=$module-dev.conf",
    sharedConfFilesReplicator in Compile := sharedConfFilesReplicate(baseDirectory.value / ".." / "..", resourceManaged.value, streams.value.log),
    resourceGenerators in Compile += (sharedConfFilesReplicator in Compile)
  )
  // Settings for every service, i.e. for admin and web subprojects
  def serviceSettings (module: String, messagesFilesFrom: Seq[String]) = moduleSettings(module) ++: Seq(
    includeFilter in (Assets, LessKeys.less) := "*.less",
    excludeFilter in (Assets, LessKeys.less) := "_*.less",
    pipelineStages := Seq(rjs, digest, gzip),
    RjsKeys.mainModule := s"main-$module",
    messagesGenerator in Compile := messagesGenerate(messagesFilesFrom, baseDirectory.value / ".." / "..", resourceManaged.value, streams.value.log),
    resourceGenerators in Compile += (messagesGenerator in Compile)
  )
	
  val commonDependencies = Seq(
    guice,
    ehcache,
    ws,
    specs2 % Test,
    "org.webjars" % "jquery" % "3.1.0",
    "org.webjars" % "bootstrap" % "3.3.7-1" exclude("org.webjars", "jquery"),
    "org.webjars" % "requirejs" % "2.3.1",
    "com.iheart" %% "ficus" % "1.3.2"
    // Add here more common dependencies:
    // jdbc,
    // anorm,
    // ...
  )
	
	
	
  /*
  * Utilities to replicate shared.*.conf files
  */
	
  lazy val sharedConfFilesReplicator = taskKey[Seq[File]]("Replicate shared.*.conf files.")
	
  def sharedConfFilesReplicate (rootDir: File, managedDir: File, log: Logger): Seq[File] = {
    val files = ((rootDir / "conf") ** "shared.*.conf").get
    val destinationDir = managedDir / "conf"
    destinationDir.mkdirs()
    files.map { file =>
      val destinationFile = destinationDir / file.getName()
      IO.copyFile(file, destinationFile)
      file
    }
  }
	
  /*
  * Utilities to generate the messages files
  */
	
  val conf = ConfigFactory.parseFile(new File("conf/shared.dev.conf")).resolve()
  val langs = scala.collection.JavaConverters.asScalaBuffer(conf.getStringList("play.i18n.langs"))
	
  lazy val messagesGenerator = taskKey[Seq[File]]("Generate the messages resource files.")
	
  def messagesGenerate (messagesFilesFrom: Seq[String], rootDir: File, managedDir: File, log: Logger): Seq[File] = {
    val destinationDir = managedDir / "conf"
    destinationDir.mkdirs()
    val files = langs.map { lang =>
      val messagesFilename = s"messages.$lang"
      val originFiles = messagesFilesFrom.map(subproject => rootDir / "modules" / subproject / "conf" / "messages" / messagesFilename)
      val destinationFile = destinationDir / messagesFilename
      IO.write(destinationFile, "## GENERATED FILE ##\n\n", append = false)
      originFiles.map { file =>
        IO.writeLines(destinationFile, lines = IO.readLines(file), append = true)
      }
      destinationFile
    }
    log.info("Generated messages files")
    files
  }
	
}