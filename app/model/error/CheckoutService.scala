package model.error

import com.gu.identity.play.IdMinimalUser
import com.gu.memsub.promo.PromoCode
import com.gu.salesforce.ContactId
import com.gu.zuora.soap.models.Results.SubscribeResult
import com.gu.zuora.soap.models.errors.PaymentGatewayError
import model.PurchaserIdentifiers
import services.UserIdData

object CheckoutService {

  sealed trait CheckoutResult

  case class CheckoutSuccess(
      salesforceMember: ContactId,
      userIdData: UserIdData,
      subscribeResult: SubscribeResult,
      validPromoCode: Option[PromoCode]) extends CheckoutResult

  case class CheckoutIdentityFailure(
      msg: String,
      requestData: String,
      errorResponse: Option[String]) extends CheckoutResult with SubsError {
    override val message = msg
    override val request = requestData
    override val response = errorResponse
  }

  case class CheckoutGenericFailure(
      purchaserIds: PurchaserIdentifiers,
      msg: String,
      requestData: String,
      errorResponse: Option[String]) extends CheckoutResult with SubsError {
    override val message = msg
    override val request = requestData
    override val response = errorResponse
  }

  case class CheckoutStripeError(
      purchaserIds: PurchaserIdentifiers,
      paymentError: Throwable,
      msg: String,
      requestData: String,
      errorResponse: Option[String]) extends CheckoutResult with SubsError {
    override val message = msg
    override val request = requestData
    override val response = errorResponse
  }

  case class CheckoutZuoraPaymentGatewayError(
      purchaserIds: PurchaserIdentifiers,
      paymentError: PaymentGatewayError,
      msg: String,
      requestData: String,
      errorResponse: Option[String]) extends CheckoutResult with SubsError {
    override val message = msg
    override val request = requestData
    override val response = errorResponse
  }

  case class CheckoutSalesforceFailure(
      identityUser: Option[IdMinimalUser],
      msg: String,
      requestData: String,
      errorResponse: Option[String]) extends CheckoutResult with SubsError {
    override val message = msg
    override val request = requestData
    override val response = errorResponse
  }

  case class CheckoutExactTargetFailure(
      purchaserIds: PurchaserIdentifiers,
      msg: String,
      requestData: String,
      errorResponse: Option[String]) extends CheckoutResult with SubsError {
    override val message = msg
    override val request = requestData
    override val response = errorResponse
  }

  case class CheckoutPaymentTypeFailure(
      purchaserIds: PurchaserIdentifiers,
      msg: String,
      requestData: String,
      errorResponse: Option[String]) extends CheckoutResult with SubsError {
    override val message = msg
    override val request = requestData
    override val response = errorResponse
  }

}


