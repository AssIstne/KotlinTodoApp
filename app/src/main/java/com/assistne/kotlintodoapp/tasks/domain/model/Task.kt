package com.assistne.kotlintodoapp.tasks.domain.model

import java.util.*

/**
 * Created by assistne on 17/2/17.
 */
data class Task(val title: String?, val description: String?,
                val id: String = UUID.randomUUID().toString(), val isCompleted: Boolean = false) {
    fun getTitleForList() : String? {
        if (!title.isNullOrEmpty()) {
            return title
        } else {
            return description
        }
    }

    fun isActive() = !isCompleted

    fun isEmpty() = (title.isNullOrEmpty() && description.isNullOrEmpty())
}