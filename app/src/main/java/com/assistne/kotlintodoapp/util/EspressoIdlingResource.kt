package com.assistne.kotlintodoapp.util

import android.support.test.espresso.IdlingResource

/**
 * Created by assistne on 17/2/17.
 */
class EspressoIdlingResource {
    companion object {
        const val RESOURCE = "GLOBAL"
        val DEFAULT_INSTANCE by lazy { SimpleCountingIdlingResource(RESOURCE) }
        fun increment() {

        }

        fun decrement() {

        }

        fun getIdlingResource() : IdlingResource = DEFAULT_INSTANCE
    }
}