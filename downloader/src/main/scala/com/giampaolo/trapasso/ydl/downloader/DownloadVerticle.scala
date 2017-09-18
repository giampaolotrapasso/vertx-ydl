package com.giampaolo.trapasso.ydl.downloader

import com.typesafe.scalalogging.LazyLogging
import io.vertx.core.Handler
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message

import scala.concurrent.Future

class DownloadVerticle extends ScalaVerticle with LazyLogging {

  override def startFuture(): Future[_] = {
    val handler: Handler[Message[String]] = x => {
      logger.debug("Received " + x.body())
      x.reply("Done")
    }

    vertx.eventBus().consumer[String]("urlRequest").handler(handler).completionFuture()
  }
}
