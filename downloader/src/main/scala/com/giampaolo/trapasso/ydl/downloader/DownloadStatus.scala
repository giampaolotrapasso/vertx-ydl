package com.giampaolo.trapasso.ydl.downloader

case class DownloadStatus(progress: String, size: String, speed: String)

object DownloadStatus {

  def fromString(s: String): Option[DownloadStatus] = {

    val regexp = new scala.util.matching.Regex(
      """([0-9]{1,2}.[0-9]{1,2})%[ ]of[ ]([0-9]{1,4}.[0-9]{1,2}.iB)[ ]at[ ]([0-9]{1,4}.[0-9]{1,2}.iB\/s)[ ]ETA[ ].*""",
      "progress",
      "size",
      "speed")

    regexp.findFirstIn(s) match {
      case Some(regexp(progress, size, speed)) => Some(DownloadStatus(progress, size, speed))
      case None => None
    }
  }

}
