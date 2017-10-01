package com.giampaolotrapasso.ydl.datastore

import com.typesafe.scalalogging.LazyLogging
import io.vertx.core.{AsyncResult, Handler, Vertx => JVertx}
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.sql.SQLClient
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message
import play.api.libs.json.{JsResult, Json}

import scala.concurrent.Future

class DatastoreVerticle extends ScalaVerticle with LazyLogging {

  private var sqlClient: SQLClient = _

  val handler: Handler[AsyncResult[Void]] = (event: AsyncResult[Void]) => {
    if (event.succeeded())
      logger.debug(s"Success ${event.result()}")
    else
      logger.debug(s"Failed! ${event.cause()}")
  }

  override def startFuture(): Future[_] = {

    sqlClient = JDBCClient.createShared(vertx.asJava.asInstanceOf[JVertx], config)

    createDownloadTableIfNotExists

    val handleDownloadUpdate: Handler[Message[String]] = x => {

      logger.debug("Received " + x.body())

      val value: JsResult[DownloadStatus] = Json.fromJson[DownloadStatus](Json.parse(x.body))

      value.map { downloadStatus =>
        sqlClient.getConnection(res => {
          if (res.succeeded()) {
            val table = "downloads"

            val statement =
              s"""
                |MERGE INTO $table
                |  USING
                |  (VALUES
                |     '${downloadStatus.url}',
                |     '${downloadStatus.progress}',
                |     '${downloadStatus.size}')
                |  vals (
                |      url,
                |      progress,
                |      size)
                |  ON ($table.url=vals.url)
                |  WHEN MATCHED THEN UPDATE SET $table.progress = vals.progress, $table.size = vals.size
                |  WHEN NOT MATCHED THEN INSERT (url, progress, size) VALUES (vals.url, vals.progress, vals.size)
              """.stripMargin

            logger.debug(statement)
            val connection = res.result()
            connection.execute(statement, handler)
            connection.close()
          } else {
            // Failed to get connection - deal with it
          }
        })
      }
    }

    vertx.eventBus().consumer[String]("downloadUpdate").handler(handleDownloadUpdate).completionFuture()
  }

  private def createDownloadTableIfNotExists =
    sqlClient.getConnection(res => {
      if (res.succeeded()) {
        val connection = res.result()
        connection.execute("CREATE TABLE IF NOT EXISTS downloads (url varchar(255), progress varchar(255), size varchar(255));", handler)
      } else {
        logger.error("Connection not created " + res.cause())
      }
    })
}
