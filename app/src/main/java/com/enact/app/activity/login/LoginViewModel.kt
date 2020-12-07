package com.enact.app.activity.login

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.enact.app.R
import com.enact.app.dependencyinjection.SealedClass
import com.enact.app.utils.ApiParameter
import com.enact.app.utils.Constants
import com.enact.app.utils.FieldValidators
import com.enact.app.webservice.ApiResponseCode
import com.enact.app.webservice.AuthRepo
import com.enact.app.webservice.ResultWrapper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import skycap.android.core.livedata.SingleEventLiveData

class LoginViewModel(val authRepo: AuthRepo) : ViewModel() {

    var rememberMe=MutableLiveData<Boolean>().apply {
        postValue(false)
    }
    var email = MutableLiveData<String>()
    var emailError: LiveData<String> = Transformations.switchMap(email, FieldValidators::setEmailError)

    var password = MutableLiveData<String>()
    var passwordError: LiveData<String> = Transformations.switchMap(password, FieldValidators::setPassError)

    var singleEventUiState = SingleEventLiveData<SealedClass>()


    fun loginOnClick() {
        if (isLoginData()) {
            val map = HashMap<String, String?>().apply {
                put(ApiParameter.email, email.value)
                put(ApiParameter.password, password.value)
                put(ApiParameter.account_type,Constants.account_type)
            }
            singleEventUiState.postValue(SealedClass.Loading)
            GlobalScope.launch {
                when (val response = authRepo.login(map)) {
                    is ResultWrapper.Success -> {
                        if(rememberMe.value!!)
                        {
                            authRepo.saveUserToLocalDb(response.value!!.data)
                        }
                        singleEventUiState.postValue(SealedClass.DismissLoading)
                        singleEventUiState.postValue(SealedClass.StartNextScreen(Constants.homeScreen))
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
            if (password.value ?: "".isNotEmpty() == false) {
                password.postValue("")
            }
        }
    }

    private fun isLoginData(): Boolean {
        return emailError.value == null && passwordError.value == null && email.value?.isNotEmpty() == true
                && password.value?.isNotEmpty() == true
    }
    fun errorViewModel(){

    }

    fun onClickForgotPassword(){
        singleEventUiState.postValue(SealedClass.StartNextScreen(Constants.forgotScreen))
    }

     fun getRememberMe():Boolean
    {
        return rememberMe.value!!
    }

    fun setRememberMe()
    {
        if(rememberMe.value!!)
        {
            rememberMe.postValue(false)
        }
        else
        {
            rememberMe.postValue(true)
        }
    }

    fun checkUserAlreadyLogin(){
        if(authRepo.getUserFromLocalDb())
        {
            singleEventUiState.postValue(SealedClass.StartNextScreen(Constants.homeScreen))
        }
    }

}