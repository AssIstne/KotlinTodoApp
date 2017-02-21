package com.assistne.kotlintodoapp.statistics

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.assistne.kotlintodoapp.R
import kotlinx.android.synthetic.main.statistics_frag.*

/**
 * Created by assistne on 17/2/21.
 */
class StatisticsFragment : Fragment(), StatisticsContract.View {
    private var presenter: StatisticsContract.Presenter? = null
    companion object {
        fun newInstance() = StatisticsFragment()
    }

    override fun setPresenter(presenter: StatisticsContract.Presenter) {
        this.presenter = presenter
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.statistics_frag, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter?.start()
    }

    override fun setProgressIndicator(active: Boolean) {
        if (active) {
            statistics.text = getString(R.string.loading)
        } else {
            statistics.text = ""
        }
    }

    override fun showStatistics(numberOfIncompleteTasks: Int, numberOfCompletedTasks: Int) {
        if (numberOfCompletedTasks == 0 && numberOfIncompleteTasks == 0) {
            statistics.text = getString(R.string.statistics_no_tasks)
        } else {
            val displayString = resources.getString(R.string.statistics_active_tasks, numberOfIncompleteTasks, numberOfCompletedTasks)
            statistics.text = displayString
        }
    }

    override fun showLoadingStatisticsError() {
        statistics.text = getString(R.string.statistics_error)
    }

    override fun isActive(): Boolean = isAdded
}
