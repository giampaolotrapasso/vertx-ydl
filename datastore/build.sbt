import sbt.Package._
import sbt._
import Docker.autoImport.exposedPorts

scalaVersion := "2.12.3"

enablePlugins(DockerPlugin)
exposedPorts := Seq(8667)

libraryDependencies ++= Vector(
  Library.vertx_lang_scala,
  Library.vertx_web,
  Library.scalaTest % "test",
  "com.typesafe.play" %% "play-json" % "2.6.3",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "org.zeroturnaround" % "zt-exec" % "1.10",
  "io.vertx" % "vertx-jdbc-client" % "3.4.2",
  "org.hsqldb" % "hsqldb" % "2.3.2",
  // Uncomment for clustering
  Library.vertx_hazelcast,
  //required to get rid of some warnings emitted by the scala-compile
  Library.vertx_codegen
)

packageOptions += ManifestAttributes(("Main-Verticle", "scala:com.giampaolo.trapasso.ydl.datastore.HttpVerticle"))
