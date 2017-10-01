package com.giampaolotrapasso.ydl.datastore

import com.typesafe.scalalogging.Logger
import io.vertx.core.json.JsonObject
import io.vertx.core.{AsyncResult, Handler}
import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import io.vertx.scala.core.{DeploymentOptions, Vertx, VertxOptions}
import org.slf4j.LoggerFactory

object Runner {
  def main(args: Array[String]): Unit = {

    lazy val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))

    val vo = VertxOptions().setFileResolverCachingEnabled(true)

    val handler: Handler[AsyncResult[Vertx]] = (res) => {
      if (res.succeeded) {
        val vertx = res.result
        val config = new JsonObject().put("url", "jdbc:hsqldb:file:db/downloada").put("driver_class", "org.hsqldb.jdbcDriver")
        val deploymentOptions = DeploymentOptions().setConfig(config)

        vertx.deployVerticle(nameForVerticle[DatastoreVerticle], deploymentOptions)
      } else {
        logger.debug("Failed" + res.cause())
      }
    }

    Vertx.clusteredVertx(vo, handler)
  }

}
