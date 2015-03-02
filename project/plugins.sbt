libraryDependencies <+= (sbtVersion) { sv =>
  "org.scala-sbt" % "scripted-plugin" % sv
}

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")