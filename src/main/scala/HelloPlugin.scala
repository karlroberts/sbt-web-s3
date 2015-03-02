package au.com.ecetera.sbt

import sbt._
import Keys._

import scala.io.Source

object HelloPlugin extends sbt.AutoPlugin {

    lazy val aboutSbtWebS3 = taskKey[Unit]("Prints hello world")

    override def projectSettings = Seq (
      aboutSbtWebS3  := {
        val url = getClass.getResource("/about/README.md")
        if(url != null)
        {
          println("blah blah " + url.getPath)
          val source = Source.fromURL( url )
          source.getLines().foreach { streams.value.log.info(_) }
        } else
        {
          streams.value.log.error("WTF! could not find resource at /about/README.md")
        }

      }
  )




}
