package com.giampaolo.trapasso.ydl.frontend

case class VideoRequest(url: String)

object VideoRequest {

  import play.api.libs.json._

  implicit val videoRequestWrite = Json.writes[VideoRequest]
}
