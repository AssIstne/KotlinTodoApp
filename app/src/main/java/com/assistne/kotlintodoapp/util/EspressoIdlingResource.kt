package com.assistne.kotlintodoapp.util

import android.support.test.espresso.IdlingResource

/**
 * Created by assistne on 17/2/17.
 */
class EspressoIdlingResource {
    companion object {
        const val RESOURCE = "GLOBAL"
        val DEFAULT_INSTANCE: SimpleCountingIdlingResource by lazy { SimpleCountingIdlingResource(RESOURCE) }
        fun increment() = DEFAULT_INSTANCE.increment()

        fun decrement() = DEFAULT_INSTANCE.decrement()

        fun getIdlingResource() : IdlingResource = DEFAULT_INSTANCE
    }
}