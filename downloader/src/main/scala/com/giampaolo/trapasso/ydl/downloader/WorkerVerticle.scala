package com.giampaolo.trapasso.ydl.downloader

import java.io.File
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
      val rootPath = config.getString("rootPath")
      val downloadPath = config.getString("downloadPath")
      val directory = s"""-o $downloadPath/%(title)s.%(ext)s"""

      new ProcessExecutor()
        .directory(new File(rootPath))
        .command("youtube-dl", directory, url)
        .redirectOutput(new LogOutputStream() {
          @Override
          def processLine(line: String) {
            logger.debug("Line " + line)
            DownloadStatus.fromString(line).foreach { download =>
              logger.debug(s"Download $download")
            }
          }
        })
        .start()
    }
}
