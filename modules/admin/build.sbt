Common.serviceSettings("admin")

// Add here the specific settings for this module


libraryDependencies ++= Common.commonDependencies ++: Seq(
	"org.webjars" % "bootswatch-superhero" % "3.2.0-1"
	// Add here the specific dependencies for this module:
	// jdbc,
	// anorm
)
