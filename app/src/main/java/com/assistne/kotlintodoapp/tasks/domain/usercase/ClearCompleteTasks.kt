package com.assistne.kotlintodoapp.tasks.domain.usercase

import com.assistne.kotlintodoapp.UseCase
import com.assistne.kotlintodoapp.data.source.TasksRepository

/**
 * Created by assistne on 17/2/20.
 */
class ClearCompleteTasks(private val tasksRepository: TasksRepository) : UseCase<ClearCompleteTasks.RequestValues, ClearCompleteTasks.ResponseValue>() {
    override fun executeUseCase(requestValues: RequestValues?) {
        tasksRepository.clearCompletedTasks()
        userCaseCallback?.onSuccess(ResponseValue())
    }

    class RequestValues : UseCase.RequestValues

    class ResponseValue : UseCase.ResponseValue
}