package com.assistne.kotlintodoapp

import com.assistne.kotlintodoapp.util.EspressoIdlingResource
import java.lang.ref.WeakReference

/**
 * Created by assistne on 17/2/17.
 */
class UseCaseHandler private constructor(val userCaseScheduler: UseCaseScheduler) {
    private object Holder {
        val INSTANCE = UseCaseHandler(UseCaseThreadPoolScheduler())
    }
    companion object {
        fun getInstance() : UseCaseHandler = Holder.INSTANCE
    }

    fun <T : UseCase.RequestValues, R : UseCase.ResponseValue> execute(
            userCase: UseCase<T, R>, values: T, callback : UseCase.UseCaseCallback<R>) {
        userCase.requestValues = values
        userCase.userCaseCallback = UiCallbackWrapper(callback, this)
        val weakUseCase = WeakReference<UseCase<T, R>>(userCase)

        EspressoIdlingResource.increment()
        userCaseScheduler.execute(Runnable {
            val userCaseToExecute = weakUseCase.get()
            userCaseToExecute?.run()

            if (EspressoIdlingResource.getIdlingResource().isIdleNow) {
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun <V : UseCase.ResponseValue> notifyResponse(response: V, useCaseCallback: UseCase.UseCaseCallback<V>) = userCaseScheduler.notifyResponse(response, useCaseCallback)

    fun <V : UseCase.ResponseValue> notifyError(userCaseCallback: UseCase.UseCaseCallback<V>) = userCaseScheduler.onError(userCaseCallback)

    class UiCallbackWrapper<V : UseCase.ResponseValue>(
            var callback: UseCase.UseCaseCallback<V>,
            var useCaseHandler: UseCaseHandler) : UseCase.UseCaseCallback<V> {
        override fun onSuccess(response: V) = useCaseHandler.notifyResponse(response, callback)

        override fun onError() = useCaseHandler.notifyError(callback)

    }
}