package com.assistne.kotlintodoapp.tasks.domain.filter

import com.assistne.kotlintodoapp.tasks.domain.model.Task

/**
 * Created by assistne on 17/2/17.
 */
class ActiveTaskFilter : TaskFilter {
    override fun filter(tasks: List<Task>) = tasks.filter(Task::isActive)
}