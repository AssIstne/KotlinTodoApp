package com.assistne.kotlintodoapp

/**
 * Created by assistne on 17/2/17.
 */
abstract class UseCase<Q : UseCase.RequestValues, P : UseCase.ResponseValue> {
    var requestValues : Q? = null
    var userCaseCallback : UseCaseCallback<P>? = null
    interface RequestValues

    interface ResponseValue

    interface UseCaseCallback<in R> {
        fun onSuccess(response: R)
        fun onError()
    }

    fun run() {
        executeUseCase(requestValues)
    }

    abstract fun executeUseCase(requestValues: Q?)
}