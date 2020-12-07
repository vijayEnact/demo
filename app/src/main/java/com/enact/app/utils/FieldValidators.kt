package com.enact.app.utils

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.enact.app.R
import com.enact.app.dependencyinjection.ResourceProvider
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


/**
 * Created by Enact.
 */
object FieldValidators: KoinComponent {
    val resourceProvider: ResourceProvider by inject()

     fun setEmailError(text: String): LiveData<String>? {
        val liveData = MutableLiveData<String>()
        if(!TextUtils.isEmpty(text) && android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches().not())
            liveData.value=  resourceProvider.getStringResource(R.string.invalid_email)
        else if (text.isEmpty()){
            liveData.value=resourceProvider.getStringResource(R.string.require)
        }
        else liveData.value=null
        return liveData
    }

      fun setNotEmptyError(string: String): LiveData<String> {
        val liveData = MutableLiveData<String>()
        if(string.isEmpty()) liveData.value=resourceProvider.getStringResource(R.string.require)
        else liveData.value=null
        return liveData
    }
    fun setPhoneError(string: String): LiveData<String> {
        val liveData = MutableLiveData<String>()
        if(string.length<10||string.length>10) liveData.value=resourceProvider.getStringResource(R.string.phone_number_length)
        else if (string.isEmpty()){
            liveData.value=resourceProvider.getStringResource(R.string.require)
        }
        else liveData.value=null
        return liveData
    }
    fun setPassError(string: String): MutableLiveData<String> {
        val liveData = MutableLiveData<String>()
        if(string.length<8||string.isEmpty()) liveData.value= resourceProvider.getStringResource(R.string.password_length)
        else liveData.value=null
        return liveData


    }
}
