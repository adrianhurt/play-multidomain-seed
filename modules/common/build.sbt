import scalariform.formatter.preferences._

Common.moduleSettings("common")

// Add here the specific settings for this module


libraryDependencies ++= Common.commonDependencies

scalariformPreferences := scalariformPreferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(DanglingCloseParenthesis, Preserve)