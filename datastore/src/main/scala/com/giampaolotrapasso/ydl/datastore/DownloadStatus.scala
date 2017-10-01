package com.giampaolotrapasso.ydl.datastore

import play.api.libs.json.Json

case class DownloadStatus(url: String, progress: String, size: String, speed: String)

object DownloadStatus {

  implicit val downloadStatusFormat = Json.format[DownloadStatus]
}
