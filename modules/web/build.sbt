import scalariform.formatter.preferences._

Common.serviceSettings("web", messagesFilesFrom = Seq("common", "web"))

// Add here the specific settings for this module


libraryDependencies ++= Common.commonDependencies ++: Seq(
	"org.webjars" % "bootswatch-cerulean" % "3.3.5+4"
	// Add here the specific dependencies for this module:
	// jdbc,
	// anorm
)

scalariformPreferences := scalariformPreferences.value
	.setPreference(AlignSingleLineCaseStatements, true)
	.setPreference(DoubleIndentConstructorArguments, true)
	.setPreference(DanglingCloseParenthesis, Preserve)