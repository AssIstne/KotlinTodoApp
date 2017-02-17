package com.assistne.kotlintodoapp.tasks.domain.filter

import com.assistne.kotlintodoapp.tasks.domain.model.Task
import java.util.*

/**
 * Created by assistne on 17/2/17.
 */
class FilterAllTaskFilter : TaskFilter {
    override fun filter(tasks: List<Task> ) = ArrayList(tasks)
}