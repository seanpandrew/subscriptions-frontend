package services

import com.gu.identity.play.IdMinimalUser
import com.gu.membership.salesforce.MemberId
import com.gu.membership.zuora.soap.Zuora.SubscribeResult
import TouchpointBackend.{Normal => touchpointBackend}
import touchpoint.{ProductPlan, BillingFrequency}
import touchpointBackend.ratePlans
import com.typesafe.scalalogging.LazyLogging
import configuration.Config
import model.SubscriptionData

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class CheckoutService(identityService: IdentityService, salesforceService: SalesforceService) extends LazyLogging {
  import CheckoutService._

  lazy val paymentDelay = Some(Config.Zuora.paymentDelay)

  def processSubscription(subscriptionData: SubscriptionData,
                          idUserOpt: Option[IdMinimalUser]): Future[CheckoutResult] = {

    val userOrElseRegisterGuest: Future[UserIdData] =
      idUserOpt.map(user => Future {
        MinimalIdUser(user)
      }).getOrElse {
        logger.info(s"User does not have an Identity account. Creating a guest account")
        identityService.registerGuest(subscriptionData.personalData)
      }

    val digitialMonthlyRatePlan = ratePlans.find( r => r.frequency == BillingFrequency.Monthly && r.product == ProductPlan.Digital)

    //TODO when implementing test-users this requires updating to supply data to correct location
    for {
      userData <- userOrElseRegisterGuest
      memberId <- salesforceService.createOrUpdateUser(subscriptionData.personalData, userData.id)
      subscribeResult <- touchpointBackend.zuoraService.createSubscription(memberId, subscriptionData,
        digitialMonthlyRatePlan.get.ratePlanId, paymentDelay)
    } yield CheckoutResult(memberId, userData, subscribeResult)
  }
}

object CheckoutService extends CheckoutService(IdentityService, SalesforceService) {
  case class CheckoutResult(salesforceMember: MemberId, userIdData: UserIdData, zuoraResult: SubscribeResult)
}
