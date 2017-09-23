package com.giampaolo.trapasso.ydl.downloader

import com.typesafe.scalalogging.LazyLogging
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.DeploymentOptions
import io.vertx.scala.core.eventbus.Message

import scala.concurrent.Future

class DownloadVerticle extends ScalaVerticle with LazyLogging {

  override def startFuture(): Future[_] = {
    val handler: Handler[Message[String]] = x => {
      logger.debug("Received " + x.body())

      val w = classOf[WorkerVerticle]

      val config = new JsonObject(x.body).put("rootPath","/Users/Giampaolo").put("downloadPath","MyVideos")

      val deploymentOptions = DeploymentOptions().setConfig(config)
      val s = w.getName

      vertx.deployVerticle(s"scala:$s", deploymentOptions)
      x.reply("Done")

    }

    vertx.eventBus().consumer[String]("urlRequest").handler(handler).completionFuture()
  }
}
