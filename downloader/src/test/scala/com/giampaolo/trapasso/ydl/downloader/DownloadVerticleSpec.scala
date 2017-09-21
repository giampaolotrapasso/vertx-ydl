package com.giampaolo.trapasso.ydl.downloader

import org.scalatest._

class DownloadVerticleSpec extends VerticleTesting[DownloadVerticle] with Matchers {

  "DownloadVerticle" should "reply to a message" in {
    val future = vertx.eventBus().sendFuture[String]("urlRequest", "msg")

    future.map(res => res.body() should equal("Done"))
  }

}
