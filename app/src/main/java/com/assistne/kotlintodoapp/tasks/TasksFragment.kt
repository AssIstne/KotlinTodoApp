package com.assistne.kotlintodoapp.tasks

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import android.widget.BaseAdapter
import android.widget.PopupMenu
import com.assistne.kotlintodoapp.R
import com.assistne.kotlintodoapp.addedittask.AddEditTaskActivity
import com.assistne.kotlintodoapp.tasks.domain.model.Task
import kotlinx.android.synthetic.main.task_item.view.*
import kotlinx.android.synthetic.main.tasks_frag.*
import java.util.*

/**
 * Created by assistne on 17/2/17.
 */
class TasksFragment : Fragment(), TasksContract.View {
    private var presenter: TasksContract.Presenter? = null
    private var listAdapter: TasksAdapter? = null

    internal var itemListener: TaskItemListener = object : TaskItemListener {
        override fun onTaskClick(clickedTask: Task) {
            presenter?.openTaskDetails(clickedTask)
        }

        override fun onCompleteTaskClick(completedTask: Task) {
            presenter?.completeTask(completedTask)
        }

        override fun onActivateTaskClick(activatedTask: Task) {
            presenter?.activateTask(activatedTask)
        }
    }
    companion object {
        fun newInstance() = TasksFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listAdapter = TasksAdapter(ArrayList<Task>(0), itemListener)
    }

    override fun onResume() {
        super.onResume()
        presenter?.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter?.result(requestCode, resultCode)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.tasks_frag, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        tasks_list.adapter = listAdapter

        noTasksAdd.setOnClickListener { showAddTask() }

        val fab = activity.findViewById(R.id.fab_add_task) as FloatingActionButton
        fab.setImageResource(R.drawable.ic_add)
        fab.setOnClickListener { presenter?.addNewTask() }

        val refreshLayout: ScrollChildSwipeRefreshLayout = view?.findViewById(R.id.refresh_layout) as ScrollChildSwipeRefreshLayout
        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(activity, R.color.colorPrimary),
                ContextCompat.getColor(activity, R.color.colorAccent),
                ContextCompat.getColor(activity, R.color.colorPrimaryDark)
        )
        refreshLayout.setScrollUpChild(tasks_list)
        refreshLayout.setOnRefreshListener { presenter?.loadTasks(false) }

        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_clear -> presenter?.clearCompletedTasks()
            R.id.menu_filter -> showFilteringPopUpMenu()
            R.id.menu_refresh -> presenter?.loadTasks(true)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.tasks_fragment_menu, menu)
    }

    override fun setLoadingIndicator(active: Boolean) {
        val root = view
        if (root != null) {
            val srl: SwipeRefreshLayout = root.findViewById(R.id.refresh_layout) as SwipeRefreshLayout
            srl.post { srl.isRefreshing = active }
        }
    }

    override fun showTasks(tasks: List<Task>) {
        listAdapter?.replaceData(tasks)
        tasksLL.visibility = View.VISIBLE
        noTasks.visibility = View.GONE
    }

    override fun showAddTask() {
        startActivityForResult(Intent(context, AddEditTaskActivity::class.java), AddEditTaskActivity.REQUEST_ADD_TASK)
    }

    override fun showTaskDetailsUi(taskId: String) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTaskMarkedComplete() {
        showMessage(getString(R.string.task_marked_complete))
    }

    override fun showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active))
    }

    override fun showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared))
    }

    override fun showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error))
    }

    override fun showNoTasks() {
        showNoTasksViews(resources.getString(R.string.no_tasks_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false)
    }

    override fun showActiveFilterLabel() {
        filteringLabel.text = resources.getText(R.string.label_active)
    }

    override fun showCompletedFilterLabel() {
        filteringLabel.text = resources.getText(R.string.label_completed)
    }

    override fun showAllFilterLabel() {
        filteringLabel.text = resources.getText(R.string.label_all)
    }

    override fun showNoActiveTasks() {
        showNoTasksViews(
                resources.getString(R.string.no_tasks_active),
                R.drawable.ic_check_circle_24dp,
                false
        )
    }

    private fun showNoTasksViews(mainText: String?, iconRes: Int, showAddView: Boolean) {
        tasksLL.visibility = View.GONE
        noTasks.visibility = View.VISIBLE

        noTasksMain.text = mainText
        noTasksIcon.setImageDrawable(resources.getDrawable(iconRes))
        noTasksAdd.visibility = if (showAddView) View.VISIBLE else View.GONE
    }

    override fun showNoCompletedTasks() {
        showNoTasksViews(
                resources.getString(R.string.no_tasks_completed),
                R.drawable.ic_verified_user_24dp,
                false
        )
    }

    override fun showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message))
    }

    private fun showMessage(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
    }

    override fun isActive(): Boolean = isAdded

    override fun showFilteringPopUpMenu() {
        val popup = PopupMenu(context, activity.findViewById(R.id.menu_filter))
        popup.menuInflater.inflate(R.menu.filter_tasks, popup.menu)

        popup.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.active -> presenter?.setFiltering(TasksFilterType.ACTIVE_TASKS)
                R.id.completed -> presenter?.setFiltering(TasksFilterType.COMPLETED_TASKS)
                else -> presenter?.setFiltering(TasksFilterType.ALL_TASKS)
            }
            presenter?.loadTasks(false)
            true
        }

        popup.show()
    }

    override fun setPresenter(presenter: TasksContract.Presenter) {
        this.presenter = presenter
    }

    private class TasksAdapter(var tasks: List<Task>, var itemListener: TaskItemListener) : BaseAdapter() {

        fun replaceData(tasks: List<Task>) {
            setList(tasks)
            notifyDataSetChanged()
        }

        private fun setList(tasks: List<Task>) {
            this.tasks = tasks
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowView: View
            if (convertView == null) {
                rowView = LayoutInflater.from(parent?.context).inflate(R.layout.task_item, parent, false)
            } else {
                rowView = convertView
            }

            val task = getItem(position)
            rowView.title?.text = task.getTitleForList()
            if (task.isCompleted) {
                rowView.setBackgroundDrawable(
                        parent?.context?.resources?.getDrawable(
                                R.drawable.list_completed_touch_feedback))
            } else {
                rowView.setBackgroundDrawable(
                        parent?.context?.resources?.getDrawable(
                                R.drawable.touchFeedback))
            }

            rowView.complete?.isChecked = task.isCompleted
            rowView.complete?.setOnClickListener {
                if (!task.isCompleted) {
                    itemListener.onCompleteTaskClick(task)
                } else {
                    itemListener.onActivateTaskClick(task)
                }
            }

            rowView.setOnClickListener {
                itemListener.onTaskClick(task)
            }

            return rowView
        }

        override fun getItem(position: Int): Task = tasks[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getCount(): Int = tasks.size

    }

    interface TaskItemListener {
        fun onTaskClick(clickedTask: Task)
        fun onCompleteTaskClick(completedTask: Task)
        fun onActivateTaskClick(activatedTask: Task)
    }
}