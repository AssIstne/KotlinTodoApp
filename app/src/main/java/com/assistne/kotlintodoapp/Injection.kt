package com.assistne.kotlintodoapp

import android.content.Context
import com.assistne.kotlintodoapp.addedittask.domain.usecase.DeleteTask
import com.assistne.kotlintodoapp.addedittask.domain.usecase.GetTask
import com.assistne.kotlintodoapp.addedittask.domain.usecase.SaveTask
import com.assistne.kotlintodoapp.data.source.FakeTasksRemoteDataSource
import com.assistne.kotlintodoapp.data.source.TasksRepository
import com.assistne.kotlintodoapp.data.source.local.TasksLocalDataSource
import com.assistne.kotlintodoapp.statistics.domain.usecase.GetStatistics
import com.assistne.kotlintodoapp.tasks.domain.filter.FilterFactory
import com.assistne.kotlintodoapp.tasks.domain.usercase.ActivateTask
import com.assistne.kotlintodoapp.tasks.domain.usercase.ClearCompleteTasks
import com.assistne.kotlintodoapp.tasks.domain.usercase.CompleteTask
import com.assistne.kotlintodoapp.tasks.domain.usercase.GetTasks

/**
 * Created by assistne on 17/2/20.
 */
class Injection {
    companion object {
        fun provideTasksRepository(context: Context): TasksRepository = TasksRepository.getInstance(FakeTasksRemoteDataSource.getInstance(),
                    TasksLocalDataSource.getInstance(context))

        fun provideGetTasks(context: Context): GetTasks = GetTasks(provideTasksRepository(context), FilterFactory())

        fun provideUseCaseHandler(): UseCaseHandler = UseCaseHandler.getInstance()

        fun provideCompleteTasks(context: Context): CompleteTask = CompleteTask(provideTasksRepository(context))

        fun provideActivateTask(context: Context): ActivateTask = ActivateTask(provideTasksRepository(context))

        fun provideClearCompleteTasks(context: Context): ClearCompleteTasks = ClearCompleteTasks(provideTasksRepository(context))

        fun provideGetTask(context: Context): GetTask = GetTask(Injection.provideTasksRepository(context))

        fun provideSaveTask(context: Context): SaveTask = SaveTask(Injection.provideTasksRepository(context))

        fun provideDeleteTask(context: Context): DeleteTask = DeleteTask(Injection.provideTasksRepository(context))

        fun provideGetStatistics(context: Context): GetStatistics = GetStatistics(Injection.provideTasksRepository(context))
    }
}