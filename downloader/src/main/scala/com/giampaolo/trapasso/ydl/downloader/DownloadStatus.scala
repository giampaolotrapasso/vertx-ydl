package com.giampaolo.trapasso.ydl.downloader

import play.api.libs.json.Json

case class DownloadStatus(url: String, progress: String, size: String, speed: String)

object DownloadStatus {

  def fromString(url: String, s: String): Option[DownloadStatus] = {

    val regexp = new scala.util.matching.Regex(
      """([0-9]{1,3}.[0-9]{1,2})%[ ]of[ ]([0-9]{1,4}.[0-9]{1,2}.iB)[ ]at[ ]([0-9]{1,4}.[0-9]{1,2}.iB\/s)[ ]ETA[ ].*""",
      "progress",
      "size",
      "speed")

    regexp.findFirstIn(s) match {
      case Some(regexp(progress, size, speed)) => Some(DownloadStatus(url, progress, size, speed))
      case None => None
    }
  }

  implicit val DownloadStatusWrite = Json.writes[DownloadStatus]

}
