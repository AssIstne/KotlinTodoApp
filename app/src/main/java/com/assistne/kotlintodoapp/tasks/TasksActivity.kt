package com.assistne.kotlintodoapp.tasks

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.assistne.kotlintodoapp.Injection
import com.assistne.kotlintodoapp.R
import com.assistne.kotlintodoapp.statistics.StatisticsActivity
import com.assistne.kotlintodoapp.util.ActivityUtils
import kotlinx.android.synthetic.main.tasks_act.*

class TasksActivity : AppCompatActivity() {
    val CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY"
    var tasksPresenter: TasksPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_act)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawer_layout.setStatusBarBackground(R.color.colorPrimaryDark)
        setupDrawerContent(nav_view)

        var tasksFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
        if(tasksFragment == null) {
            tasksFragment = TasksFragment.newInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, tasksFragment, R.id.contentFrame)
        }

        tasksPresenter = TasksPresenter(
                Injection.provideUseCaseHandler(),
                tasksFragment as TasksContract.View,
                Injection.provideGetTasks(applicationContext),
                Injection.provideCompleteTasks(applicationContext),
                Injection.provideActivateTask(applicationContext),
                Injection.provideClearCompleteTasks(applicationContext)
        )
    }

    fun setupDrawerContent(navigationView: NavigationView?) {
        navigationView?.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.statistics_navigation_menu_item -> {
                    val intent = Intent(this@TasksActivity, StatisticsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }
            it.isChecked = true
            drawer_layout.closeDrawers()
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putSerializable(CURRENT_FILTERING_KEY, tasksPresenter?.currentFiltering)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
