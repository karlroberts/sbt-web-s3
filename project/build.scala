import sbt.Keys._
import sbt._

import ohnosequences.sbt.SbtS3Resolver.{s3 => ss33, _}
import com.amazonaws.services.s3.model.Region
import com.ambiata.promulgate.project.ProjectPlugin._

object ProjectSettings {
  val organisation = "au.com.ecetera.sbt"
  val id = "sbt-web-s3"
  val isSnapshot = true
  private[this] val versionNum = "0.2.1"
  val version = if(isSnapshot) versionNum else versionNum + "-SNAPSHOT"

}

object V {
  val sprayV ="1.3.2"
  val awsV = "1.3.29"
//  val awsV = "1.7.13"
  val specs2V = "3.6"
  val scalacheckV = "1.10.0"
}

object SbtWebS3Build extends Build {

  type Sett = sbt.Def.Setting[_]

  val coredeps = Seq(
      "com.amazonaws" % "aws-java-sdk" % V.awsV
    , "io.spray"      %% "spray-http"    % V.sprayV
    , "org.specs2" %% "specs2-core" % V.specs2V % "test" //withSources() withJavadoc()
    , "org.specs2" %% "specs2-scalacheck" % V.specs2V % "test"
//    , "org.scalacheck" %% "scalacheck" % scalacheckV
  )

  lazy val standardSettings = Defaults.defaultSettings ++
    Seq[Sett](
      organization := ProjectSettings.organisation
      , sbtPlugin := true
      , scalaVersion := "2.10.4"
      , credentials +=   Credentials(Path.userHome / ".s3Credentials")
      , scalacOptions := Seq(
        "-deprecation"
        , "-unchecked"
        , "-optimise"
        , "-Ywarn-all"
        , "-Xlint"
        , "-Xfatal-warnings"
        , "-feature"
        , "-language:_"
      )
    ) ++ S3Resolver.defaults ++ Seq(
      publishMavenStyle           := false
      , publishArtifact in Test     := false
      , pomIncludeRepository        := { _ => false }
      , publishTo                   <<= (s3credentials).apply((creds) =>
        Some(S3Resolver(creds, false, Region.AP_Sydney)("ecetera-oss-publish", ss33("ecetera-repo-oss")).withIvyPatterns))
    )

  //standardSettings ++

  lazy val root: Project = Project(
    id = "sbt-web-s3"
    , base = file(".")
    , settings = standardSettings ++ Seq(
        name := ProjectSettings.id
      , version in ThisBuild := ProjectSettings.version
      , resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases" //for streams for specs2
      , libraryDependencies ++= coredeps
    )
//        ++ VersionPlugin.uniqueVersionSettings
        ++ promulgate.library("au.com.ecetera.sbt", "ecetera-repo-oss")
  )
}
