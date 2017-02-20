package com.assistne.kotlintodoapp.addedittask.domain.usecase

import com.assistne.kotlintodoapp.UseCase
import com.assistne.kotlintodoapp.data.source.TasksRepository
import com.assistne.kotlintodoapp.tasks.domain.model.Task

/**
 * Created by assistne on 17/2/20.
 */
class SaveTask(val tasksRepository: TasksRepository) : UseCase<SaveTask.RequestValues, SaveTask.ResponseValue>() {
    override fun executeUseCase(requestValues: RequestValues?) {
        val task = requestValues?.task
        if (task != null) {
            tasksRepository.saveTask(task)
            userCaseCallback?.onSuccess(ResponseValue(task))
        }
    }

    class RequestValues(val task: Task) : UseCase.RequestValues

    class ResponseValue(val task: Task) : UseCase.ResponseValue
}