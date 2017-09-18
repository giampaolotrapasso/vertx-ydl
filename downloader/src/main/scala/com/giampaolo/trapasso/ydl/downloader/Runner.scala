package com.giampaolo.trapasso.ydl.downloader

import com.typesafe.scalalogging.Logger
import io.vertx.core.{AsyncResult, Handler}
import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import io.vertx.scala.core.{Vertx, VertxOptions}
import org.slf4j.LoggerFactory

object Runner {
  def main(args: Array[String]): Unit = {

    lazy val logger: Logger =
      Logger(LoggerFactory.getLogger(getClass.getName))

    val vo = VertxOptions().setFileResolverCachingEnabled(true)

    val handler: Handler[AsyncResult[Vertx]] = (res) => {
      if (res.succeeded) {
        val vertx = res.result
        vertx.deployVerticle(nameForVerticle[DownloadVerticle])
      } else {
        logger.debug("Failed" + res.cause())
      }
    }

    Vertx.clusteredVertx(vo, handler)
  }
}
