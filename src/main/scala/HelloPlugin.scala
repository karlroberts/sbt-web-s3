package com.owtelse.sbt
import sbt._
import Keys._
import scala.io.Source
object HelloPlugin extends sbt.AutoPlugin {
    lazy val s3wsReadme = taskKey[Unit]("Prints out the Plugin's README")
    override def projectSettings = Seq (
      s3wsReadme  := {
        val url = getClass.getResource("/about/README.md")
        if(url != null)
        {
          println("---> " + url.getPath)
          val source = Source.fromURL( url )
          source.getLines().foreach { streams.value.log.info(_) }
        } else
        {
          streams.value.log.error("Could not find resource at /about/README.md")
        }
      }
  )
}
