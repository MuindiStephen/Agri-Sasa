package com.steve_md.smartmkulima.data.remote

import com.steve_md.smartmkulima.model.ubibot_iot.UbiBotAllResponse
import com.steve_md.smartmkulima.model.ubibot_iot.UbiBotResponse
import retrofit2.http.GET

interface UbiBotIoTWebService {

    // https://script.google.com/macros/s/AKfycbwqMY1ru0BdRR1fHgFG06J9bNjzV4JXhMSpACwAb03C34_rOusJNVY2bOp06cpTvFZT/exec

    @GET("macros/s/AKfycbwqMY1ru0BdRR1fHgFG06J9bNjzV4JXhMSpACwAb03C34_rOusJNVY2bOp06cpTvFZT/exec")
    suspend fun fetchLatestEntryUbiBotData(): UbiBotResponse

    @GET("macros/echo?user_content_key=O_1T39g7a7JgpWMYTawsjhQIF3BAyK1W8lVbue0gnhq64mtxv3yPPs-UBE2ITKWFlV29bYJ3RBZbQlDZh_zMH-YTNbLVngBjm5_BxDlH2jW0nuo2oDemN9CCS2h10ox_1xSncGQajx_ryfhECjZEnO7sVaYUUEXvTEw2RWBjKDuP8ARs8ruhxZkWn222-mgec6FbcWkx3TczwrKdXp_9tE0Sj8V9ZNMFmGp-TJjK8aNnRBK1fWZWKQ&lib=MQStUg-N7V5JwG09gd0GvfsmoKdKy5H4y")
    suspend fun fetchAllUbiBotData(): List<UbiBotAllResponse>

}