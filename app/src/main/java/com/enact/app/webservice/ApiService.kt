package com.enact.app.webservice
import com.enact.app.model.CommonObject
import com.enact.app.model.register.RegisterResponse
import retrofit2.Response
import retrofit2.http.*


/**
 * Created by Enact.
 */
interface ApiService {
    @POST("login")
    @FormUrlEncoded
    suspend fun login(@FieldMap map: HashMap<String,String?>):Response<CommonObject<RegisterResponse>>

    @POST("register")
    @FormUrlEncoded
    suspend fun register(@FieldMap map: HashMap<String,String?>):Response<CommonObject<RegisterResponse>>



    @POST("forget_password")
    @FormUrlEncoded
    suspend fun forgotPassword(@FieldMap map: HashMap<String,String?>):Response<CommonObject<*>>


}



