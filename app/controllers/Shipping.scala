package controllers

import actions.CommonActions._
import com.gu.i18n.CountryGroup
import com.gu.memsub.Digipack
import com.gu.subscriptions.{PhysicalProducts, ProductPlan}
import model.Subscriptions._
import play.api.mvc._
import services.TouchpointBackend
import utils.TestUsers.PreSigninTestCookie
import scalaz.syntax.std.boolean._

object Shipping extends Controller {

  // no need to support test users here really as plans seldom change
  val catalog = TouchpointBackend.Normal.catalogService.paperCatalog

  def index(subscriptionCollection: SubscriptionProduct) = {
    Ok(views.html.shipping.shipping(subscriptionCollection))
  }

  def planToOptions(in: ProductPlan[PhysicalProducts]): SubscriptionOption =
    SubscriptionOption(in.slug,
      in.name, in.priceGBP.amount * 12 / 52, in.saving.map(_.toString + "%"), in.priceGBP.amount, in.description,
      routes.Checkout.renderCheckout(CountryGroup.UK, None, in.slug).url
    )

  def viewCollectionPaperDigital() = CachedAction {
    val plans = List.empty //catalog.voucher.productPlans.filter(_.products.others.contains(Digipack)).map(planToOptions)
    index(CollectionSubscriptionProduct(
      title = "Paper + digital voucher subscription",
      description = "Save money on your newspapers and digital content. Plus start using the daily edition and premium live news app immediately.",
      packageType = "paper-digital",
      options = plans.nonEmpty.option(plans).getOrElse(
        Seq(
        SubscriptionOption("everyday",
          "Everyday+", 10.99f, Some("41%"), 47.62f, "Guardian and Observer papers, plus tablet editions and Premium mobile access",
          "https://www.guardiansubscriptions.co.uk/Voucher?prom=faa13&pkgcode=ukx01&title=gv7&skip=1"
        ),
        SubscriptionOption("sixday",
          "Sixday+", 9.99f, Some("36%"), 43.29f, "Guardian papers, plus tablet editions and Premium mobile access",
          "https://www.guardiansubscriptions.co.uk/Voucher?prom=faa13&pkgcode=ukx01&title=gv6&skip=1"
        ),
        SubscriptionOption("weekend",
          "Weekend+", 6.79f, Some("22%"), 29.42f, "Saturday Guardian and Observer papers, plus tablet editions and Premium mobile access",
          "https://www.guardiansubscriptions.co.uk/Voucher?prom=faa13&pkgcode=ukx01&title=gv2&skip=1"
        ),
        SubscriptionOption("sunday",
          "Sunday+", 5.09f, Some("12%"), 22.06f, "Observer paper, plus tablet editions and Premium mobile access",
          "https://www.guardiansubscriptions.co.uk/Voucher?prom=faa13&pkgcode=ukx01&title=ov1&skip=1"
        )
      )
    )))
  }

  def viewCollectionPaper() = CachedAction {
    val plans = List.empty //catalog.voucher.productPlans.filter(_.products.others.isEmpty).map(planToOptions)
    index(CollectionSubscriptionProduct(
      title = "Paper voucher subscription",
      description = "Save money on your newspapers.",
      packageType = "paper",
      options = plans.nonEmpty.option(plans).getOrElse(Seq(
        SubscriptionOption("everyday",
          "Everyday", 9.99f, Some("37%"), 43.29f, "Guardian and Observer papers",
          "https://www.guardiansubscriptions.co.uk/Voucher?prom=faa13&pkgcode=ukx00&title=gv7&skip=1"
        ),
        SubscriptionOption("sixday",
          "Sixday", 8.49f, Some("34%"), 36.79f, "Guardian papers",
          "https://www.guardiansubscriptions.co.uk/Voucher?prom=faa13&pkgcode=ukx00&title=gv6&skip=1"
        ),
        SubscriptionOption("weekend",
          "Weekend", 4.79f, Some("19%"), 20.76f, "Saturday Guardian and Observer papers",
          "https://www.guardiansubscriptions.co.uk/Voucher?prom=faa13&pkgcode=ukx00&title=gv2&skip=1"
        )
      )
    )))
  }

  def viewDeliveryPaperDigital() = CachedAction {
    val plans = List.empty //catalog.delivery.productPlans.filter(_.products.others.contains(Digipack)).map(planToOptions)
    index(DeliverySubscriptionProduct(
      title = "Paper + digital home delivery subscription",
      description = """|If you live within the M25 you can have your papers delivered by 7am Monday - Saturday and 8.30am on Sunday.
        |Plus you can start using the daily edition and premium live news app immediately.""".stripMargin,
      packageType = "paper-digital",
      options = plans.nonEmpty.option(plans).getOrElse(Seq(
        SubscriptionOption("everyday",
          "Everyday+", 14.49f, None, 62.79f, "Guardian and Observer papers, plus tablet editions and Premium mobile access",
          "https://www.guardiandirectsubs.co.uk/Delivery/details.aspx?package=EVERYDAY%2B"
        ),
        SubscriptionOption("sixday",
          "Sixday+", 12.99f, None, 56.29f, "Guardian papers, plus tablet editions and Premium mobile access",
          "https://www.guardiandirectsubs.co.uk/Delivery/details.aspx?package=SIXDAY%2B"
        ),
        SubscriptionOption("weekend",
          "Weekend+", 7.79f, None, 33.76f, "Saturday Guardian and Observer papers, plus tablet editions and Premium mobile access",
          "https://www.guardiandirectsubs.co.uk/Delivery/details.aspx?package=WEEKEND%2B"
        ),
        SubscriptionOption("sunday",
          "Sunday+", 6.09f, None, 26.39f, "Observer paper, plus tablet editions and Premium mobile access",
          "https://www.guardiandirectsubs.co.uk/Delivery/details.aspx?package=SUNDAY%2B"
        )
      )
    )))
  }

  def viewDeliveryPaper() = CachedAction {
    val plans = List.empty //catalog.delivery.productPlans.filter(_.products.others.isEmpty).map(planToOptions)
    index(DeliverySubscriptionProduct(
      title = "Paper home delivery subscription",
      description = "If you live within the M25 you can have your papers delivered by 7am Monday - Saturday and 8.30 on Sunday.",
      packageType = "paper",
      options = plans.nonEmpty.option(plans).getOrElse(Seq(
        SubscriptionOption("everyday",
          "Everyday", 13.49f, None, 58.46f, "Guardian and Observer papers",
          "https://www.guardiandirectsubs.co.uk/Delivery/details.aspx?package=EVERYDAY"
        ),
        SubscriptionOption("sixday",
          "Sixday", 11.49f, None, 49.79f, "Guardian papers",
          "https://www.guardiandirectsubs.co.uk/Delivery/details.aspx?package=SIXDAY"
        ),
        SubscriptionOption("weekend",
          "Weekend", 5.79f, None, 25.09f, "Saturday Guardian and Observer papers",
          "https://www.guardiandirectsubs.co.uk/Delivery/details.aspx?package=WEEKEND"
        )
      )
    )))
  }

}
