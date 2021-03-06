//resolver for sbt-s3-resolver
resolvers += "Era7 maven releases" at "http://releases.era7.com.s3.amazonaws.com"

resolvers += Resolver.url("ambiata-oss", new URL("https://ambiata-oss.s3.amazonaws.com"))(Resolver.ivyStylePatterns)

libraryDependencies <+= (sbtVersion) { sv =>
  "org.scala-sbt" % "scripted-plugin" % sv
}

addSbtPlugin("ohnosequences" % "sbt-s3-resolver" % "0.12.0")

addSbtPlugin("com.ambiata" % "promulgate" % "0.11.0-20141014013725-80c129f")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")

sbtPlugin := true