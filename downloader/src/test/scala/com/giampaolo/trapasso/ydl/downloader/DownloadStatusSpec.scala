package com.giampaolo.trapasso.ydl.downloader

import org.scalatest.Matchers._
import org.scalatest.{WordSpec, _}

class DownloadStatusSpec extends WordSpec {

  "DownloadStatus.fromString" when {

    """receives '[youtube] v4pi1LxuDHc: Downloading webpage'""" should {

      "return None" in {
        DownloadStatus.fromString("[youtube] v4pi1LxuDHc: Downloading webpage") shouldBe None
      }
    }
    """receives [download]   7.2% of 42.96MiB at 11.26KiB/s ETA 01:00:30""" should {

      "return valid download status" in {

        DownloadStatus.fromString("[download]   7.2% of 42.96MiB at 11.26KiB/s ETA 01:00:30") shouldBe Some(
          DownloadStatus("7.2", "42.96MiB", "11.26KiB/s"))

      }

    }

    """receives [download]   100.0% of 42.96MiB at 11.26KiB/s ETA 01:00:30""" should {

      "return valid download status" in {

        DownloadStatus.fromString("[download]   100.0% of 42.96MiB at 11.26KiB/s ETA 01:00:30") shouldBe Some(
          DownloadStatus("100.0", "42.96MiB", "11.26KiB/s"))

      }

    }

  }
}
