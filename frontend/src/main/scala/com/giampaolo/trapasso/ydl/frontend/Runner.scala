package com.giampaolo.trapasso.ydl.frontend

import io.vertx.core.{AsyncResult, Handler}
import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import io.vertx.scala.core.{Vertx, VertxOptions}

object Runner {
  def main(args: Array[String]): Unit = {
    val vo = VertxOptions().setFileResolverCachingEnabled(true)

    val handler: Handler[AsyncResult[Vertx]] = (res) => {
      if (res.succeeded) {
        val vertx = res.result
        vertx.deployVerticle(nameForVerticle[HttpVerticle])
      } else {
        println("Failed" + res.cause())
      }
    }

    Vertx.clusteredVertx(vo, handler)
  }
}
