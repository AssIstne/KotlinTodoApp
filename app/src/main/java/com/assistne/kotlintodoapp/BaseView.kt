package com.assistne.kotlintodoapp

/**
 * Created by assistne on 17/2/17.
 */
interface BaseView<in T : BasePresenter> {
    fun setPresenter(presenter : T)
}