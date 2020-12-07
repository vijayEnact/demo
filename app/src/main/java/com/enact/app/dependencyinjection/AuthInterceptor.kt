package com.example.ks.di




import com.app.demo.sharedpref.SharedPreferenceHelper
import com.enact.app.utils.Constants
import com.google.gson.Gson
import okhttp3.*

/**
 * Created by Enact.
 */
class AuthInterceptor(private val  sharedPreferenceHelper: SharedPreferenceHelper) : Interceptor {
    override  fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        // DONT INCLUDE API KEYS IN YOUR SOURCE CODE
        sharedPreferenceHelper.getAccessToken()?.let {
            req=req.newBuilder().apply {
                addHeader("Authorization","Bearer $it")
                addHeader( "Accept", "application/json")
            }.build()
        }

        var response= chain.proceed(req)
        if(response.code()==401){
            val refreshTokenBody = FormBody.Builder()
                .add("refresh_token", sharedPreferenceHelper.getRefreshToken()?:"")

                .build()
            val refreshTokenRequest = Request.Builder()
                .addHeader("Accept", "application/json")
                .url(Constants.BASE_URL + "refresh-token")
                .post(refreshTokenBody)
                .build()
            val refreshTokenResponse = chain.proceed(refreshTokenRequest)
            if (refreshTokenResponse.code() == 200) {
                val fromJson = Gson().fromJson<ResponseBody>(
                    Gson().toJson(refreshTokenResponse.body()),
                    ResponseBody::class.java
                )
             /*   sharedPreferenceHelper.saveRefreshToken(fromJson.data?.accessToken)
                sharedPreferenceHelper.saveRefreshToken(fromJson.data?.refreshToken)*/
                response=chain.proceed(buildRequest(chain,"Token Value"))
            }
        }
        return response
    }
    private fun buildRequest(chain: Interceptor.Chain, token: String?,): Request {

        val original = chain.request()


            original.newBuilder()
                .method(original.method(), original.body())
                .addHeader("Accept", "application/json")
                .header("Authorization", "Bearer $token")
//                .addHeader("device-token", deviceId)
                .build()

        return original
    }

}