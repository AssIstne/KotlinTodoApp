package com.assistne.kotlintodoapp.taskdetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.*
import com.assistne.kotlintodoapp.R
import com.assistne.kotlintodoapp.addedittask.AddEditTaskActivity
import com.assistne.kotlintodoapp.addedittask.AddEditTaskFragment
import kotlinx.android.synthetic.main.taskdetail_frag.*

/**
 * Created by assistne on 17/2/21.
 */
class TaskDetailFragment : Fragment(), TaskDetailContract.View {
    private var presenter: TaskDetailContract.Presenter? = null
    companion object {
        val ARGUMENT_TASK_ID = "TASK_ID"
        val REQUEST_EDIT_TASK = 1

        fun newInstance(taskId: String?) : TaskDetailFragment {
            val arguments = Bundle()
            arguments.putString(ARGUMENT_TASK_ID, taskId)
            val fragment = TaskDetailFragment()
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        presenter?.start()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.taskdetail_frag, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        activity.findViewById(R.id.fab_edit_task).setOnClickListener { presenter?.editTask() }
    }

    override fun setPresenter(presenter: TaskDetailContract.Presenter) {
        this.presenter = presenter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_delete -> {
                presenter?.deleteTask()
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.taskdetail_fragment_menu, menu)
    }

    override fun setLoadingIndicator(active: Boolean) {
        if (active) {
            task_detail_title.text = ""
            task_detail_description.text = getString(R.string.loading)
        }
    }

    override fun hideDescription() {
        task_detail_description.visibility = View.GONE
    }

    override fun hideTitle() {
        task_detail_title.visibility = View.GONE
    }

    override fun showDescription(description: String?) {
        task_detail_description.visibility = View.VISIBLE
        task_detail_description.text = description
    }

    override fun showCompletionStatus(complete: Boolean) {
        task_detail_complete.isChecked = complete
        task_detail_complete.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                presenter?.completeTask()
            } else {
                presenter?.activateTask()
            }
        }
    }

    override fun showEditTask(taskId: String) {
        val intent = Intent(context, AddEditTaskActivity::class.java)
        intent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId)
        startActivityForResult(intent, REQUEST_EDIT_TASK)
    }

    override fun showTaskDeleted() {
        activity.finish()
    }

    override fun showTaskMarkedComplete() {
        Snackbar.make(view as View, R.string.task_marked_complete, Snackbar.LENGTH_LONG).show()
    }

    override fun showTaskMarkedActive() {
        Snackbar.make(view as View, R.string.task_marked_active, Snackbar.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_EDIT_TASK && resultCode == Activity.RESULT_OK) {
            activity.finish()
        }
    }

    override fun showTitle(title: String?) {
        task_detail_title.visibility = View.VISIBLE
        task_detail_title.text = title
    }

    override fun showMissingTask() {
        task_detail_title.text = ""
        task_detail_description.text = getString(R.string.no_data)
    }

    override fun isActive(): Boolean = isAdded
}