package com.assistne.kotlintodoapp

import android.os.Handler
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by assistne on 17/2/17.
 */
class UseCaseThreadPoolScheduler : UseCaseScheduler {
    val POOL_SIZE = 2
    val MAX_POOL_SIZE = 4
    val TIMEOUT = 30

    val handler = Handler()
    val threadPoolExecutor : ThreadPoolExecutor

    init {
        threadPoolExecutor = ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT.toLong(),
                TimeUnit.SECONDS, ArrayBlockingQueue<Runnable>(POOL_SIZE))
    }

    override fun execute(runnable: Runnable) = threadPoolExecutor.execute(runnable)

    override fun <V : UseCase.ResponseValue> notifyResponse(response: V, userCaseCallback: UseCase.UseCaseCallback<V>) {
        handler.post { userCaseCallback.onSuccess(response) }
    }
    override fun <V : UseCase.ResponseValue> onError(useCaseCallback: UseCase.UseCaseCallback<V>) {
        handler.post { useCaseCallback.onError() }
    }
}