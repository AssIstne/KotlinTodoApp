package com.assistne.kotlintodoapp.tasks.domain.filter

import com.assistne.kotlintodoapp.tasks.domain.model.Task

/**
 * Created by assistne on 17/2/17.
 */
class CompleteTaskFilter : TaskFilter {
    override fun filter(tasks: List<Task>): List<Task> = tasks.filter(Task::completed)
}