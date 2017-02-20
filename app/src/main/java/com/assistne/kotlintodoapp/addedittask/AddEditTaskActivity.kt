package com.assistne.kotlintodoapp.addedittask

import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v7.app.AppCompatActivity
import com.assistne.kotlintodoapp.Injection
import com.assistne.kotlintodoapp.R
import com.assistne.kotlintodoapp.util.ActivityUtils
import com.assistne.kotlintodoapp.util.EspressoIdlingResource
import kotlinx.android.synthetic.main.addtask_act.*

class AddEditTaskActivity : AppCompatActivity() {
    companion object {
        val REQUEST_ADD_TASK = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addtask_act)

        setSupportActionBar(toolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var addEditTaskFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
        val taskId = intent.getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)

        if (addEditTaskFragment == null) {
            addEditTaskFragment = AddEditTaskFragment.newInstance()

            if (intent.hasExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)) {
                toolbar.setTitle(R.string.edit_task)
                val bundle = Bundle()
                bundle.putString(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId)
                addEditTaskFragment.arguments = bundle
            } else {
                toolbar.setTitle(R.string.add_task)
            }

            ActivityUtils.addFragmentToActivity(supportFragmentManager, addEditTaskFragment, R.id.contentFrame)
        }

        AddEdtiTaskPresenter(Injection.provideUseCaseHandler(),
                taskId,
                addEditTaskFragment as AddEditTaskContract.View,
                Injection.provideGetTask(applicationContext),
                Injection.provideSaveTask(applicationContext))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @VisibleForTesting
    fun getCountingIdlingResource() = EspressoIdlingResource.getIdlingResource()
}
