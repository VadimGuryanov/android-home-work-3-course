package kpfu.itis.homework

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentsClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var googlePaymentsClient: PaymentsClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        googlePaymentsClient = GooglePaymentUtils.createGoogleApiClientForPay(this)
        checkIsReadyGooglePay()
        initGooglePayButton()
    }

    private fun checkIsReadyGooglePay() {
        GooglePaymentUtils.checkIsReadyGooglePay(googlePaymentsClient) {
            rl_google_pay.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun initGooglePayButton() {
        rl_google_pay.setOnClickListener {
            val request = GooglePaymentUtils.createPaymentDataRequest(getString(R.string.price))
            AutoResolveHelper.resolveTask<PaymentData>(googlePaymentsClient.loadPaymentData(request),
                this,
                LOAD_PAYMENT_DATA_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE ->
                when (resultCode) {
                    Activity.RESULT_OK -> data?.apply {
                            val paymentData = PaymentData.getFromIntent(data)
                            val tokenJSON = paymentData?.paymentMethodToken?.token
                            Toast.makeText(this@MainActivity, tokenJSON, Toast.LENGTH_LONG).show()
                            Log.e("token", tokenJSON ?: "")
                        }
                    Activity.RESULT_CANCELED -> {}
                    AutoResolveHelper.RESULT_ERROR -> {
                        data?.let {
                            val status = AutoResolveHelper.getStatusFromIntent(data)
                            Toast.makeText(this@MainActivity, status.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }

    companion object {
        private val LOAD_PAYMENT_DATA_REQUEST_CODE = 1
    }

}