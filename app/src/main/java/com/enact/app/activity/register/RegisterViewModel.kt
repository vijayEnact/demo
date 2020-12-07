package com.enact.app.activity.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.enact.app.R
import com.enact.app.dependencyinjection.SealedClass
import com.enact.app.utils.ApiParameter
import com.enact.app.utils.Constants
import com.enact.app.utils.FieldValidators
import com.enact.app.utils.FieldValidators.resourceProvider
import com.enact.app.webservice.ApiResponseCode
import com.enact.app.webservice.AuthRepo
import com.enact.app.webservice.ResultWrapper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import skycap.android.core.livedata.SingleEventLiveData
import kotlin.math.sin

class RegisterViewModel(val authRepo: AuthRepo) :ViewModel(){

    var fullName = MutableLiveData<String>()
    val fullNameError: LiveData<String> = Transformations.switchMap(fullName, FieldValidators::setNotEmptyError)

    var phoneNumber = MutableLiveData<String>()
    val phoneNumberError: LiveData<String> = Transformations.switchMap(phoneNumber, FieldValidators::setPhoneError)


    var email = MutableLiveData<String>()
    val emailError: LiveData<String> = Transformations.switchMap(email, FieldValidators::setEmailError)

    var password = MutableLiveData<String>()
    val passwordError: LiveData<String> = Transformations.switchMap(password, FieldValidators::setPassError)

    var singleEventUiState = SingleEventLiveData<SealedClass>()

    fun onRegisterClick() {
        if (isRegisterData()) {
            val map = HashMap<String, String?>().apply {
                put(ApiParameter.name, fullName.value)
                put(ApiParameter.phone, phoneNumber.value)
                put(ApiParameter.email, email.value)
                put(ApiParameter.password, password.value)
                put(ApiParameter.account_type,Constants.account_type)
                put(ApiParameter.type,Constants.type)
            }
            singleEventUiState.postValue(SealedClass.Loading)
            GlobalScope.launch {
                when (val response = authRepo.register(map)) {
                    is ResultWrapper.Success -> {
                        singleEventUiState.postValue(SealedClass.DismissLoading)
                        singleEventUiState.postValue(SealedClass.StartNextScreen(Constants.login))

                    }
                    is ResultWrapper.Error -> {
                        singleEventUiState.postValue(SealedClass.DismissLoading)
                        when (response.code) {
                            ApiResponseCode.NETWORK_NOT_AVAILABLE -> {
                                singleEventUiState.postValue(
                                        SealedClass.ErrorClass(
                                                resourceProvider.getStringResource(
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
                                                resourceProvider.getStringResource(
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
            if (password.value ?: "".isNotEmpty() == false) {
                password.postValue("")
            }

            if (fullName.value ?: "".isNotEmpty() == false) {
                fullName.postValue("")
            }

            if (phoneNumber.value ?: "".isNotEmpty() == false) {
                phoneNumber.postValue("")
            }
        }
    }


    private fun isRegisterData(): Boolean {
        return emailError.value == null && passwordError.value == null && phoneNumberError.value == null
                && fullNameError.value == null && email.value?.isNotEmpty() == true
                && password.value?.isNotEmpty() == true &&   fullName.value?.isNotEmpty() == true
                &&   phoneNumber.value?.isNotEmpty() == true
    }




}