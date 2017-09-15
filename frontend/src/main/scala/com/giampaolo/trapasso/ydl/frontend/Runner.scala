package com.giampaolo.trapasso.ydl.frontend

import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import io.vertx.scala.core.{Vertx, VertxOptions}

object Runner {
  def main(args: Array[String]): Unit = {
    val vo = VertxOptions().setFileResolverCachingEnabled(true)

    val vertx = Vertx.vertx(vo)
    vertx.deployVerticle(nameForVerticle[HttpVerticle])
  }
}
