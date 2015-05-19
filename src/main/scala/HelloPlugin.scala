package au.com.ecetera.sbt

import sbt._
import Keys._

import scala.io.Source

object HelloPlugin extends sbt.AutoPlugin {

    lazy val s3wsReadme = taskKey[Unit]("Prints sbt-web-s3 usage information")

    override def projectSettings = Seq (
      s3wsReadme  := {
        val url = getClass.getResource("/about/README.md")
        if(url != null)
        {
          println("blah blah " + url.getPath)
          val source = Source.fromURL( url )
          source.getLines().foreach { streams.value.log.info(_) }
        } else
        {
          streams.value.log.error("Could not find resource at /about/README.md")
        }

      }
  )




}
