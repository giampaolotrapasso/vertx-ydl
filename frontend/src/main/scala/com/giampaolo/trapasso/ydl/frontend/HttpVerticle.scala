package com.giampaolo.trapasso.ydl.frontend

import com.typesafe.scalalogging.LazyLogging
import io.vertx.core.{AsyncResult, Handler}
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.{BodyHandler, StaticHandler}
import play.api.libs.json.Json
import com.giampaolo.trapasso.ydl.frontend.VideoRequest._

import scala.concurrent.Future

class HttpVerticle extends ScalaVerticle with LazyLogging {

  override def startFuture(): Future[_] = {
    logger.debug("Starting HTTP Verticle")

    val router = Router.router(vertx)

    router.route.handler(BodyHandler.create)

    val staticHandler = StaticHandler.create("webroot")
    staticHandler.setIndexPage("/index.html")
    router.route("/").handler(staticHandler)

    val eventBus = vertx.eventBus()

    val handler: Handler[AsyncResult[Message[String]]] = (event: AsyncResult[Message[String]]) => {
      if (event.succeeded())
        logger.debug(s"Success ${event.result()}")
      else
        logger.debug(s"Failed! ${event.cause()}")
    }

    router
      .post("/urls")
      .handler(r => {
        for {
          request <- r.getBodyAsString()
          map = Json.parse(request).as[List[Map[String, String]]].flatten.toMap
          videoRequest = VideoRequest(map("value"))
          _ = eventBus.send("urlRequest", Json.toJson(videoRequest).toString(), handler)
        } yield videoRequest
        r.response().setStatusCode(201).end("{}")
      })

    router
      .route()
      .last()
      .handler(context => {
        logger.error(s"Url not found ${context.request().path()} ${context.request().formAttributes().names().toList}")
        context.response().setStatusCode(404).end("Not found")
      })

    vertx.createHttpServer().requestHandler(router.accept _).listenFuture(8666, "0.0.0.0")
  }
}
