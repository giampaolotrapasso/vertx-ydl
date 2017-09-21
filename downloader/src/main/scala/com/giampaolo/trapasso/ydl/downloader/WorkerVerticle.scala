package com.giampaolo.trapasso.ydl.downloader

import java.util.concurrent.{Future => JavaFuture}

import com.typesafe.scalalogging.LazyLogging
import io.vertx.lang.scala.ScalaVerticle
import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.stream.LogOutputStream

import scala.concurrent.Future

class WorkerVerticle extends ScalaVerticle with LazyLogging {

  override def startFuture(): Future[_] =
    Future {
      val url = config.getString("url")

      new ProcessExecutor()
        .command("youtube-dl", url)
        .redirectOutput(new LogOutputStream() {
          @Override
          def processLine(line: String) {
            DownloadStatus.fromString(line).foreach{ download =>
              logger.debug(s"Download $download")
            }
          }
        })
        .start()
    }
}
