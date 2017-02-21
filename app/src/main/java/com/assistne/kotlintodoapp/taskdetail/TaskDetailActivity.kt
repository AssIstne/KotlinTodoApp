package com.assistne.kotlintodoapp.taskdetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.assistne.kotlintodoapp.Injection
import com.assistne.kotlintodoapp.R
import com.assistne.kotlintodoapp.util.ActivityUtils
import kotlinx.android.synthetic.main.taskdetail_act.*

class TaskDetailActivity : AppCompatActivity() {
    companion object {
        val EXTRA_TASK_ID = "TASK_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.taskdetail_act)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val taskId = intent.getStringExtra(EXTRA_TASK_ID)
        var taskDetailFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)

        if (taskDetailFragment == null) {
            taskDetailFragment = TaskDetailFragment.newInstance(taskId)
            ActivityUtils.addFragmentToActivity(supportFragmentManager, taskDetailFragment, R.id.contentFrame)
        }

        TaskDetailPresenter(
                Injection.provideUseCaseHandler(),
                taskId,
                taskDetailFragment as TaskDetailFragment,
                Injection.provideGetTask(applicationContext),
                Injection.provideCompleteTasks(applicationContext),
                Injection.provideActivateTask(applicationContext),
                Injection.provideDeleteTask(applicationContext)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
