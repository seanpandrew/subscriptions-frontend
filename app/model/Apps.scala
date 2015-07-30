package model

import controllers.CachedAssets.hashedPathFor

object Apps {

  case class AppBadge(title: String, path: String) {
    val src = hashedPathFor(path)
  }
  case class AppLink(href: String, badge: AppBadge)

  val appleAppStoreBadge = AppBadge(
    "Download from the App Store",
    "images/logos/apple-app-store.svg"
  )
  val googlePlayStoreBadge = AppBadge(
    "Get it on Google Play",
    "images/logos/google-play-store.svg"
  )
  val amazonAppStoreBadge = AppBadge(
    "Available at Amazon",
    "images/logos/amazon-app-store.svg"
  )

  object DailyEdition {
    val appleAppStore = AppLink(
      "https://itunes.apple.com/gb/app/guardian-observer-daily-edition/id452707806?mt=8",
      appleAppStoreBadge
    )
    val googlePlayStore = AppLink(
      "https://play.google.com/store/apps/details?id=com.guardian.android.tabletedition.google",
      googlePlayStoreBadge
    )
    val amazonAppStore = AppLink(
      "http://www.amazon.co.uk/dp/B00H9I8MBK",
      amazonAppStoreBadge
    )
  }

  object LiveNews {
    val appleAppStore = AppLink(
      "https://itunes.apple.com/gb/app/the-guardian/id409128287?mt=8",
      appleAppStoreBadge
    )
    val googlePlayStore = AppLink(
      "https://play.google.com/store/apps/details?id=com.guardian&hl=en_GB",
      googlePlayStoreBadge
    )
  }

}
