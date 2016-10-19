package configuration

import utils.StringUtils._

case class Links(href: String, title: String) {
  val slug = slugify(title)
}

object Links {
  val digipackTerms = Links(
    "https://www.theguardian.com/info/2014/aug/06/guardian-observer-digital-subscriptions-terms-conditions",
    "Terms and Conditions"
  )
  val paperTerms = Links(
    "https://www.theguardian.com/subscriber-direct/subscription-terms-and-conditions",
    "Terms and Conditions"
  )
  val privacyPolicy = Links(
    "https://www.theguardian.com/help/privacy-policy",
    "Privacy Policy"
  )

  val whyYourDataMattersToUs = Links(
    "https://www.theguardian.com/info/video/2014/nov/03/why-your-data-matters-to-us-video",
    "Why your data matter to us"
  )

  val guardianSubscriptionFaqs = Links(
    "https://www.theguardian.com/subscriber-direct/subscription-frequently-asked-questions",
    "Frequently Asked Questions"
  )
}

object ProfileLinks {
  private val identityUrl = Config.Identity.webAppUrl

  val signIn =  Links(s"$identityUrl/signin?returnUrl=${Config.subscriptionsUrl}", "Your account")

  val commentActivity = Links(s"$identityUrl/user/id/", "Comment activity")
  val editProfile = Links(s"$identityUrl/public/edit", "Edit profile")
  val emailPreferences = Links(s"$identityUrl/email-prefs", "Email preferences")
  val changePassword = Links(s"$identityUrl/password/change", "Change password")
  val signOut = Links(s"$identityUrl/signout", "Sign out")

  val popupLinks = Seq(commentActivity, editProfile, emailPreferences, changePassword, signOut)
}
