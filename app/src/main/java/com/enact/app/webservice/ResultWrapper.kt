package com.enact.app.webservice

/**
 * Created by Enact.
 */
sealed class ResultWrapper<out T> {
    /*  data class Success<out T>(val value: T): ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null): ResultWrapper<Nothing>()
    object SocketTimeOutError: ResultWrapper<Nothing>()
    object NetworkError: ResultWrapper<Nothing>()
}
data class ErrorResponse(
    val error_des : String?,
    val causes: Map<String, String> = emptyMap()
)*/

    class Loading<T> : ResultWrapper<T>()

    data class Success<T>(val value: T) : ResultWrapper<T>()

    data class Error<T>(val code: Int, val message: String) : ResultWrapper<T>()

    val isLoading: Boolean get() = this is Loading

    val isSuccess: Boolean get() = this is Success

    val isError: Boolean get() = this is Error


    companion object {

        fun <T> loading() = Loading<T>()

        fun <T> success(value: T) = Success(value)

        fun <T> error(code: Int, message: String) = Error<T>(code, message)
    }
}