import sbt._
import Keys._
import play.PlayImport._
import play.sbt.routes.RoutesKeys.routesGenerator
import play.routes.compiler.InjectedRoutesGenerator
import com.typesafe.sbt.web.SbtWeb.autoImport.{Assets, pipelineStages}
import com.typesafe.sbt.less.Import.LessKeys
import com.typesafe.sbt.rjs.Import.{rjs, RjsKeys}
import com.typesafe.sbt.digest.Import.digest
import com.typesafe.sbt.gzip.Import.gzip

object Common {
	def appName = "play-multidomain-seed"
	
	// Common settings for every project
	def settings (theName: String) = Seq(
		name := theName,
		organization := "com.myweb",
		version := "1.0-SNAPSHOT",
		scalaVersion := "2.11.6",
		routesGenerator := InjectedRoutesGenerator,
		doc in Compile <<= target.map(_ / "none"),
		scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked", "-language:reflectiveCalls"),
		resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
	)
	// Settings for the app, i.e. the root project
	val appSettings = settings(appName) ++: Seq(
		javaOptions += s"-Dconfig.resource=root-dev.conf"
	)
	// Settings for every module, i.e. for every subproject
	def moduleSettings (module: String) = settings(module) ++: Seq(
		javaOptions += s"-Dconfig.resource=$module-dev.conf"
	)
	// Settings for every service, i.e. for admin and web subprojects
	def serviceSettings (module: String) = moduleSettings(module) ++: Seq(
		includeFilter in (Assets, LessKeys.less) := "*.less",
		excludeFilter in (Assets, LessKeys.less) := "_*.less",
		pipelineStages := Seq(rjs, digest, gzip),
		RjsKeys.mainModule := s"main-$module"
	)
	
	val commonDependencies = Seq(
		cache,
		ws,
		specs2 % Test,
		"org.webjars" % "jquery" % "2.1.4",
		"org.webjars" % "bootstrap" % "3.3.5" exclude("org.webjars", "jquery"),
		"org.webjars" % "requirejs" % "2.1.19"
		// Add here more common dependencies:
		// jdbc,
		// anorm,
		// ...
	)
}