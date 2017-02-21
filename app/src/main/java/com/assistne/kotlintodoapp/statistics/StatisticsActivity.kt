package com.assistne.kotlintodoapp.statistics

import android.content.Intent
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.design.widget.NavigationView
import android.support.test.espresso.IdlingResource
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.assistne.kotlintodoapp.Injection
import com.assistne.kotlintodoapp.R
import com.assistne.kotlintodoapp.tasks.TasksActivity
import com.assistne.kotlintodoapp.util.ActivityUtils
import com.assistne.kotlintodoapp.util.EspressoIdlingResource
import kotlinx.android.synthetic.main.statistics_act.*

class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics_act)

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.statistics_title)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawer_layout.setStatusBarBackground(R.color.colorPrimaryDark)
        setupDrawerContent(nav_view)

        var statisticsFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as StatisticsFragment?
        if (statisticsFragment == null) {
            statisticsFragment = StatisticsFragment.newInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, statisticsFragment, R.id.contentFrame)
        }

        StatisticsPresenter(Injection.provideUseCaseHandler(),
                statisticsFragment,
                Injection.provideGetStatistics(applicationContext))
    }

    private fun setupDrawerContent(navigationView: NavigationView?) {
        navigationView?.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.list_navigation_menu_item -> {
                    val intent = Intent(this@StatisticsActivity, TasksActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }
            it.isChecked = true
            drawer_layout.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> drawer_layout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    @VisibleForTesting
    fun getCountingIdlingResource(): IdlingResource {
        return EspressoIdlingResource.getIdlingResource()
    }
}
