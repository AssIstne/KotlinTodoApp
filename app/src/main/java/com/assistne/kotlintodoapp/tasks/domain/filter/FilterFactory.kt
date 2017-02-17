package com.assistne.kotlintodoapp.tasks.domain.filter

import com.assistne.kotlintodoapp.tasks.TasksFilterType

/**
 * Created by assistne on 17/2/17.
 */
class FilterFactory {
    val filters: Map<TasksFilterType, TaskFilter> = hashMapOf(
        TasksFilterType.ALL_TASKS to FilterAllTaskFilter(),
        TasksFilterType.ACTIVE_TASKS to ActiveTaskFilter(),
        TasksFilterType.COMPLETED_TASKS to CompleteTaskFilter()
    )
    fun create(filterType: TasksFilterType) = filters[filterType]
}