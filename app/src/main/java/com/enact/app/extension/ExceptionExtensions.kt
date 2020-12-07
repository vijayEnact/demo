package com.enact.app.extension

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.enact.app.webservice.ApiResponseCode
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException

import java.net.ConnectException
import java.net.UnknownHostException

fun Exception.getStatusCode(): Int {
    return when (this) {
        is ConnectException -> ApiResponseCode.SERVER_CONNECTION_ERROR
        is UnknownHostException -> ApiResponseCode.NETWORK_NOT_AVAILABLE
        is JsonSyntaxException -> ApiResponseCode.SYNTAX_ERROR
        is HttpException -> ApiResponseCode.UN_AUTHORIZE
        else -> ApiResponseCode.UNKNOWN_ERROR
    }



}

fun Context.vibrate(milliseconds:Long = 200){
    val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    // Check whether device/hardware has a vibrator
    val canVibrate:Boolean = vibrator.hasVibrator()

    if(canVibrate){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // void vibrate (VibrationEffect vibe)
            vibrator.vibrate(
                    VibrationEffect.createOneShot(
                            milliseconds,
                            // The default vibration strength of the device.
                            VibrationEffect.DEFAULT_AMPLITUDE
                    )
            )
        }else{
            // This method was deprecated in API level 26
            vibrator.vibrate(milliseconds)
        }
    }
}