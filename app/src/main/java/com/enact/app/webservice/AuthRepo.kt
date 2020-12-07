package com.enact.app.webservice
import com.enact.app.model.CommonObject
import com.app.demo.sharedpref.SharedPreferenceHelper
import com.enact.app.extension.getStatusCode
import com.enact.app.model.register.RegisterResponse
import com.enact.app.utils.LocalDb
import org.json.JSONObject
import retrofit2.Response

/**
 * Created by skycap.
 */
class AuthRepo(private val apiService: ApiService
               ,private val sharedPreferenceHelper: SharedPreferenceHelper){


    suspend fun login(body:HashMap<String,String?>): ResultWrapper<CommonObject<RegisterResponse>?> {
        return try {
            val response: Response<CommonObject<RegisterResponse>> = apiService.login(body)
            val apiResponse = response.body()
            if (response.code() == ApiResponseCode.SUCCESS_CODE) {
                if (apiResponse?.status==ApiResponseCode.SUCCESS_CODE){
                    ResultWrapper.success(apiResponse)
                }
                else{
                    ResultWrapper.Error<CommonObject<RegisterResponse>>(ApiResponseCode.errorCode,
                            apiResponse?.message
                            ?: "")
                }
            } else {
                val jObjError = JSONObject(response.errorBody()?.string())
                ResultWrapper.Error<CommonObject<RegisterResponse>>(jObjError.getInt("code"), "")
            }
        } catch (e: Exception) {
            ResultWrapper.Error<CommonObject<RegisterResponse>>(e.getStatusCode(), "")
        }
    }

    suspend fun register(body:HashMap<String,String?>): ResultWrapper<CommonObject<RegisterResponse>?> {
        return try {
            val response: Response<CommonObject<RegisterResponse>> = apiService.register(body)
            val apiResponse = response.body()
            if (response.code() == ApiResponseCode.SUCCESS_CODE) {
                if (apiResponse?.status==ApiResponseCode.SUCCESS_CODE) {
                    ResultWrapper.success(apiResponse)
                }
                else{
                    ResultWrapper.Error<CommonObject<RegisterResponse>>(ApiResponseCode.errorCode, apiResponse?.message
                            ?: "")
                }
            } else {
                val jObjError = JSONObject(response.errorBody()?.string())
                ResultWrapper.Error<CommonObject<RegisterResponse>>(jObjError.getInt("code"), "")
            }
        } catch (e: Exception) {
            ResultWrapper.Error<CommonObject<RegisterResponse>>(e.getStatusCode(), "")
        }
    }


    suspend fun forgotPassword(body:HashMap<String,String?>): ResultWrapper<CommonObject<*>?> {
        return try {
            val response: Response<CommonObject<*>> = apiService.forgotPassword(body)
            val apiResponse = response.body()
            if (response.code() == ApiResponseCode.SUCCESS_CODE) {
                if (apiResponse?.status==ApiResponseCode.SUCCESS_CODE) {
                    ResultWrapper.success(apiResponse)
                }
                else{
                    ResultWrapper.Error<CommonObject<*>>(ApiResponseCode.errorCode, apiResponse?.message
                            ?: "")
                }
            } else {
                val jObjError = JSONObject(response.errorBody()?.string())
                ResultWrapper.Error<CommonObject<*>>(jObjError.getInt("code"), "")
            }
        } catch (e: Exception) {
            ResultWrapper.Error<CommonObject<*>>(e.getStatusCode(), "")
        }
    }

    suspend fun saveUserToLocalDb(user:RegisterResponse){
        LocalDb.userLogined=user
    }

    fun getUserFromLocalDb():Boolean{
        //&& LocalDb?.userLogined?.user_id!=0
        if(LocalDb.userLogined!=null )
        {
            return true
        }
        return false
    }
}
