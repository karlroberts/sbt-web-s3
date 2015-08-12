import sbt.Keys._
import sbt._

import ohnosequences.sbt.SbtS3Resolver.{s3 => ss33, _}
import com.amazonaws.services.s3.model.Region
import com.ambiata.promulgate.project.ProjectPlugin._

object ProjectSettings {
  val organisation = "com.owtelse.sbt"
  val id = "sbt-web-s3"
  val isSnapshot = true
  private[this] val versionNum = "0.2.2"
  val version = if(isSnapshot) versionNum else versionNum + "-SNAPSHOT"

}

object V {
  val Version = """(\d)+\.(\d)+\.(\d)+""".r

  def sprayV(scalaVersion: String): ModuleID = CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, major)) if (major == 9)  => { println("----> seen version 2.9"); "io.spray"  % "spray-http"  % "1.3.1" }
    case Some((2, major)) if (major == 10) => { println("----> seen version 2.10"); "io.spray"  %% "spray-http"  % "1.3.2" }
    case _ => { println("-------> seen default"); "io.spray"  %% "spray-http"  % "1.3.2" }
  }

  val awsV = "1.3.29"
//  val awsV = "1.7.13"
  def specs2V(scalaVersion: String): Seq[ModuleID] = CrossVersion.partialVersion(scalaVersion) match {

    case Some((2, major)) if (major == 9) => Seq("org.specs2" %% "specs2" % "1.12.4.1" % "test" //withSources() withJavadoc()
    )

    case Some((2, major)) if (major == 10) => Seq("org.specs2" %% "specs2-core" % "3.6" % "test" //withSources() withJavadoc()
      , "org.specs2" %% "specs2-scalacheck" % "3.6" % "test"
    )

    case _ => Seq("org.specs2" %% "specs2-core" % "3.6" % "test" //withSources() withJavadoc()
      , "org.specs2" %% "specs2-scalacheck" % "3.6" % "test"
    )
  }
  val scalacheckV = "1.10.0"


  def optionsVersion(scalaVersion:String): Seq[String] = {
    val extras = CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, major)) if (major == 9) => Seq[String]()
      case Some((2, major)) if (major == 10) => Seq[String]("-Ywarn-all", "-feature", "-language:_" )
      case Some((2, major)) if (major == 11) => Seq[String](
        "-feature"
        , "-language:_"
        , "-Ywarn-adapted-args"
        , "-Ywarn-dead-code"
        , "-Ywarn-inaccessible"
        , "-Ywarn-infer-any"
        , "-Ywarn-nullary-override"
        , "-Ywarn-nullary-unit"
        , "-Ywarn-numeric-widen"
        , "-Ywarn-unused"
        , "-Ywarn-unused-import"
        , "-Ywarn-value-discard"
      )
      case _ => Seq("-feature", "-language:_")
    }

    Seq(
      "-deprecation"
      , "-unchecked"
      , "-optimise"
      , "-Xlint"
      , "-Xfatal-warnings"
      , "-Xlog-reflective-calls"
    ) ++ extras
  }
}

object SbtWebS3Build extends Build {

  type Sett = sbt.Def.Setting[_]

  def coredeps(scalaVersion: String) = Seq(
      "com.amazonaws" % "aws-java-sdk" % V.awsV
    , V.sprayV(scalaVersion)
  ) ++ V.specs2V(scalaVersion)

  def scalacOptionVersion(scalaVersion:String): Seq[String] =  V.optionsVersion(scalaVersion)

  lazy val standardSettings = Defaults.defaultSettings ++
    Seq[Sett](
      organization := ProjectSettings.organisation
      , sbtPlugin := true
      , scalaVersion := "2.10.4"
//      , crossScalaVersions := Seq("2.10.4", "2.11.7")
      , credentials +=   Credentials(Path.userHome / ".s3Credentials")
      , scalacOptions := scalacOptionVersion(scalaVersion.value)
    ) ++ S3Resolver.defaults ++ Seq(
      publishMavenStyle           := false
      , publishArtifact in Test     := false
      , pomIncludeRepository        := { _ => false }
      , publishTo                   <<= (s3credentials).apply((creds) =>
        Some(S3Resolver(creds, false, Region.AP_Sydney)("owtelse-oss-publish", ss33("owtelse-repo-oss")).withIvyPatterns))
    )

  //standardSettings ++

  lazy val root: Project = Project(
    id = "sbt-web-s3"
    , base = file(".")
    , settings = standardSettings ++ Seq(
        name := ProjectSettings.id
      , version in ThisBuild := ProjectSettings.version
      , resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases" //for streams for specs2
      , libraryDependencies <++= scalaVersion ( sv => coredeps(sv) )
    )
//        ++ VersionPlugin.uniqueVersionSettings
        ++ promulgate.library("com.owtelse.sbt", "owtelse-repo-oss")
  )
}
