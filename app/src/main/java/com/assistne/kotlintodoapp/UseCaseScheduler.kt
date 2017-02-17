package com.assistne.kotlintodoapp

/**
 * Created by assistne on 17/2/17.
 */
interface UseCaseScheduler {
    fun execute(runnable: Runnable)
    fun <V : UseCase.ResponseValue> notifyResponse(response: V, userCaseCallback: UseCase.UseCaseCallback<V>)
    fun <V : UseCase.ResponseValue> onError(useCaseCallback: UseCase.UseCaseCallback<V>)
}