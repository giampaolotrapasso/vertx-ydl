package com.giampaolo.trapasso.ydl.downloader

import com.giampaolotrapasso.ydl.datastore.DatastoreVerticle
import io.vertx.lang.scala.json.{Json, JsonObject}
import org.scalatest._
import org.scalatest.concurrent.{PatienceConfiguration, ScalaFutures}
import io.vertx.lang.scala.ScalaVerticle.nameForVerticle

class DatastoreVerticleSpec extends VerticleTesting[DatastoreVerticle] with Matchers with ScalaFutures with PatienceConfiguration {

  "DataStoreVerticle" should "reply to a message" in {
    val future = vertx.eventBus().sendFuture[String]("urlRequest", """{"url":"http://youtube.com/watch?test"}""")

    whenReady(future) { res =>
      res.body() should equal("Done")
    }
  }

  override def config() = Json.emptyObj().put("workerVerticleName", nameForVerticle[DummyVerticle])
}
