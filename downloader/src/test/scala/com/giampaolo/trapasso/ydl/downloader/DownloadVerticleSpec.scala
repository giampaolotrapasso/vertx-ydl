package com.giampaolo.trapasso.ydl.downloader

import io.vertx.lang.scala.json.{Json, JsonObject}
import org.scalatest._
import org.scalatest.concurrent.{PatienceConfiguration, ScalaFutures}
import io.vertx.lang.scala.ScalaVerticle.nameForVerticle

class DownloadVerticleSpec extends VerticleTesting[DownloadVerticle] with Matchers with ScalaFutures with PatienceConfiguration {

  "DownloadVerticle" should "reply to a message" in {
    val future = vertx.eventBus().sendFuture[String]("urlRequest", """{"url":"http://youtube.com/watch?test"}""")

    whenReady(future) { res =>
      res.body() should equal("Done")
    }
  }

  override def config() = Json.emptyObj().put("workerVerticleName", nameForVerticle[DummyVerticle])
}
