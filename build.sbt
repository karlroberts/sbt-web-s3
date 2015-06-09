ScriptedPlugin.scriptedSettings

scriptedLaunchOpts <++= version apply { version =>
  Seq("-Xmx1024M", "-XX:MaxPermSize=256M", "-Dplugin.version=" + version)
}

scriptedBufferLog := false

//needed for scalaz streamz in specs two extra matchers.... I'm not useing them so why?
//resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"


resourceGenerators in Compile += Def.task {
  val readme = (baseDirectory in Compile).value / "README.md"
  val file = (resourceManaged in Compile).value / "about" / "README.md"
//  val contents = "name=%s\nversion=%s".format(name.value,version.value)
  IO.copyFile(readme, file, false)
  Seq(file)
}.taskValue

scalacOptions in (Compile, doc) ++= Seq("-doc-root-content", baseDirectory.value+"/root-doc.txt")

scalacOptions in (Compile, doc) ++= Seq("-doc-title", "sbt-web-s3")

