package com.enact.app.activity.forgot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.enact.app.R
import com.enact.app.dependencyinjection.SealedClass
import com.enact.app.utils.ApiParameter
import com.enact.app.utils.FieldValidators
import com.enact.app.webservice.ApiResponseCode
import com.enact.app.webservice.AuthRepo
import com.enact.app.webservice.ResultWrapper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import skycap.android.core.livedata.SingleEventLiveData

class ForgotViewModel(val authRepo: AuthRepo) : ViewModel(){

    val email = MutableLiveData<String>()
    val emailError: LiveData<String> = Transformations.switchMap(email, FieldValidators::setEmailError)

    var singleEventUiState = SingleEventLiveData<SealedClass>()

    fun onClickForgotPassword(){
        if (isForgotData()) {
            val map = HashMap<String, String?>().apply {
                put(ApiParameter.email, email.value)
            }
            singleEventUiState.postValue(SealedClass.Loading)
            GlobalScope.launch {
                when (val response = authRepo.forgotPassword(map)) {
                    is ResultWrapper.Success -> {
                        singleEventUiState.postValue(SealedClass.DismissLoading)
                        singleEventUiState.postValue(SealedClass.HasData(response.value))

                    }
                    is ResultWrapper.Error -> {
                        singleEventUiState.postValue(SealedClass.DismissLoading)
                        when (response.code) {
                            ApiResponseCode.NETWORK_NOT_AVAILABLE -> {
                                singleEventUiState.postValue(
                                        SealedClass.ErrorClass(
                                                FieldValidators.resourceProvider.getStringResource(
                                                        R.string.TXT_MSG_NETWORK
                                                )
                                        )
                                )
                            }
                            ApiResponseCode.errorCode -> {
                                singleEventUiState.postValue(
                                        SealedClass.ErrorClass(response.message)
                                )
                            }
                            else -> {
                                singleEventUiState.postValue(
                                        SealedClass.ErrorClass(
                                                FieldValidators.resourceProvider.getStringResource(
                                                        R.string.TXT_MSG_COMMON_API_ERROR
                                                )
                                        )
                                )
                            }
                        }
                    }
                }
            }
        } else {
            if (email.value ?: "".isNotEmpty() == false) {
                email.postValue("")
            }
        }
    }
    private fun isForgotData(): Boolean {
        return emailError.value == null && email.value?.isNotEmpty() == true
    }


}