package com.enact.app.dependencyinjection

sealed class SealedClass{


    object Loading : SealedClass()
    object DismissLoading : SealedClass()
    /**
     * data was successfully loaded for the screen.  "happy path"
     */
    data class HasData<T>(val content:T?) : SealedClass()

    object NoData : SealedClass()

    data class ErrorClass(val errorMessage: String): SealedClass()
    data class SuccessMessage(val successMessage: String): SealedClass()

    data class StartNextScreen(val targetScreen:String): SealedClass()

    data class UpdateUi(val status:Int): SealedClass()


}