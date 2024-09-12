import android.annotation.SuppressLint
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.steve_md.smartmkulima.data.remote.DarajaApiClient
import com.steve_md.smartmkulima.data.room.AppDatabase
import com.steve_md.smartmkulima.data.room.TransactionDao
import com.steve_md.smartmkulima.model.Transaction
import com.steve_md.smartmkulima.payment.mpesa.dto.StkPushRequest
import com.steve_md.smartmkulima.payment.mpesa.dto.StkPushSuccessResponse
import com.steve_md.smartmkulima.utils.Constants
import com.steve_md.smartmkulima.utils.Constants.BUSINESS_SHORT_CODE
import com.steve_md.smartmkulima.utils.Constants.CALLBACKURL
import com.steve_md.smartmkulima.utils.Constants.PARTYB
import com.steve_md.smartmkulima.utils.Constants.PASSKEY
import com.steve_md.smartmkulima.utils.Constants.SANDBOX_BASE_URL
import com.steve_md.smartmkulima.utils.DateFormat
import com.steve_md.smartmkulima.utils.RegEx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

/**
 * Repository for initiating M-PESA API ( Server Requests )
 */
class PaymentRepository @Inject constructor(
    private val apiClient: DarajaApiClient,
    database: AppDatabase
) {
    private val consumerKey = "NgGUJ2LGJlVvjdLu8P7yDGIs6v4RmMF1114mYRUVTVOjsCii"
    private val consumerSecret = "L5Ur0sgEuGfBAB7u8ynlH0bzerD1VABt7ABASfBAozvATLKETNHGcpTieX1vzLyv"

    private val transactionDao = database.transactionDao()


    // Initiating Daraja API Client
    fun initiateDarajaApiClient() : DarajaApiClient {

        apiClient.setIsDebug(true)

        return DarajaApiClient(
            consumerKey = consumerKey,
            consumerSecret = consumerSecret,
            environment = SANDBOX_BASE_URL
        )
    }


    // Get Access Token
    suspend fun getAccessToken(): String {

        apiClient.setGetAccessToken(false)

        val response = apiClient.mpesaService().getAccessToken().execute()
        if (response.isSuccessful) {
            return response.body()?.accessToken ?: throw Exception("Access token not available")
        } else {
            throw Exception("Failed to fetch access token")
        }
    }

    suspend fun performSTKPush(phoneNumber: String, amount: String): StkPushSuccessResponse {
        val timestamp = RegEx.getTimestamp()

        val stkPushRequest = StkPushRequest(
            businessShortCode = BUSINESS_SHORT_CODE,
            password = RegEx.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp!!)!!,
            timestamp = timestamp,
            transactionType = Constants.TransactionType.CustomerPayBillOnline,
            amount = amount,
            partyA = RegEx.sanitizePhoneNumber(phoneNumber),
            partyB = PARTYB,
            phoneNumber = RegEx.sanitizePhoneNumber(phoneNumber),
            callBackURL = CALLBACKURL,
            accountReference = "LIPA NA MPESA",
            transactionDesc = "LIPA NA MPESA C2B"
        )



        val response = apiClient.mpesaService().sendPush(stkPushRequest).execute()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("STK Push response is empty")
        } else {
            throw HttpException(response)
        }
    }

    // Save this Transaction to Room DB
    @SuppressLint("SimpleDateFormat")
    suspend fun savePaymentTransaction(amount: String) {

        val yourmilliseconds = System.currentTimeMillis()
        val sdf = SimpleDateFormat("MMM dd,yyyy HH:mm")
        val resultdate = Date(yourmilliseconds)


        CoroutineScope(Dispatchers.IO).launch {
            val transaction =
                Transaction(id = 0, amount.toDouble(), sdf.format(resultdate))

            transactionDao.saveTransaction(transaction)
        }
    }
}
