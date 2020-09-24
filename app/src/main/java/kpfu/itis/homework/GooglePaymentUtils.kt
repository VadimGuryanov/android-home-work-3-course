package kpfu.itis.homework

import android.app.Activity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
object GooglePaymentUtils {

    private const val CURRENCY_CODE = "RUB"
//    private const val PAYMENT_GATEWAY_TOKENIZATION_NAME = "sberbank"
    private const val PAYMENT_GATEWAY_TOKENIZATION_NAME = "yandexcheckout"
    private const val MERCHANT_NAME = "Example Merchant"
    private val SUPPORTED_NETWORKS = arrayListOf(
        WalletConstants.CARD_NETWORK_VISA,
        WalletConstants.CARD_NETWORK_MASTERCARD
    )

    fun createGoogleApiClientForPay(activity: Activity): PaymentsClient =
        Wallet.getPaymentsClient(activity,
            Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .setTheme(WalletConstants.THEME_LIGHT)
                .build())

    fun checkIsReadyGooglePay(paymentsClient: PaymentsClient, callback: (res: Boolean) -> Unit) {
        val isReadyRequest = IsReadyToPayRequest.newBuilder()
            .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
            .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
            .addAllowedPaymentMethods(SUPPORTED_NETWORKS)
            .build()
        val task = paymentsClient.isReadyToPay(isReadyRequest)
        task.addOnCompleteListener {
            try {
                if (it.getResult(ApiException::class.java) == true) {
                    callback.invoke(true)
                } else {
                    callback.invoke(false)
                }
            } catch (e: ApiException) {
                callback.invoke(false)
            }
        }
    }

    fun createPaymentDataRequest(price: String): PaymentDataRequest {
        val transaction = createTransaction(price)
        return generatePaymentRequest(transaction)
    }

    private fun createTransaction(price: String): TransactionInfo =
        TransactionInfo.newBuilder()
            .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
            .setTotalPrice(price)
            .setCurrencyCode(CURRENCY_CODE)
            .build()

    private fun generatePaymentRequest(transactionInfo: TransactionInfo): PaymentDataRequest {
        val tokenParams = PaymentMethodTokenizationParameters
            .newBuilder()
            .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
            .addParameter("gateway", PAYMENT_GATEWAY_TOKENIZATION_NAME)
            .addParameter("gatewayMerchantId", MERCHANT_NAME)
            .build()

        return PaymentDataRequest.newBuilder()
            .setTransactionInfo(transactionInfo)
            .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
            .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
            .setCardRequirements(CardRequirements.newBuilder()
                .addAllowedCardNetworks(SUPPORTED_NETWORKS)
                .build())
            .setPaymentMethodTokenizationParameters(tokenParams)
            .setUiRequired(true)
            .build()
    }
}