version := "0.1.0-SNAPSHOT"

sbtPlugin := true

organization := "au.com.ecetera.sbt"

name := "sbt-web-s3"

ScriptedPlugin.scriptedSettings

scriptedLaunchOpts <++= version apply { version =>
  Seq("-Xmx1024M", "-XX:MaxPermSize=256M", "-Dplugin.version=" + version)
}

scriptedBufferLog := false

libraryDependencies ++= {
  val sprayV ="1.3.2"
  val awsV = "1.3.29"
  Seq(
    "com.amazonaws" % "aws-java-sdk" % awsV,
    "io.spray"      %% "spray-http"    % sprayV
  )
}

resourceGenerators in Compile += Def.task {
  val readme = (baseDirectory in Compile).value / "README.md"
  val file = (resourceManaged in Compile).value / "about" / "README.md"
//  val contents = "name=%s\nversion=%s".format(name.value,version.value)
  IO.copyFile(readme, file, false)
  Seq(file)
}.taskValue



