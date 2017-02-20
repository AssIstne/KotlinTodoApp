package com.assistne.kotlintodoapp.addedittask

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.assistne.kotlintodoapp.R
import kotlinx.android.synthetic.main.addtask_frag.*

/**
 * Created by assistne on 17/2/20.
 */
class AddEditTaskFragment : Fragment(), AddEditTaskContract.View {

    private var presenter: AddEditTaskContract.Presenter? = null

    companion object {
        val ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID"
        fun newInstance() = AddEditTaskFragment()
    }

    override fun onResume() {
        super.onResume()
        presenter?.start()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater?.inflate(R.layout.addtask_frag, container, false)
        setHasOptionsMenu(true)
        retainInstance = true
        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val fab = activity.findViewById(R.id.fab_edit_task_done) as FloatingActionButton
        fab.setImageResource(R.drawable.ic_done)
        fab.setOnClickListener {
            presenter?.saveTask(add_task_title.text.toString(), add_task_description.text.toString())
        }
    }

    override fun showEmptyTaskError() {
        Snackbar.make(add_task_title, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show()
    }

    override fun showTasksList() {
        activity.setResult(Activity.RESULT_OK)
        activity.finish()
    }

    override fun setTitle(title: String?) {
        add_task_title.setText(title)
    }

    override fun setDescription(description: String?) {
        add_task_description.setText(description)
    }

    override fun isActive() = isAdded

    override fun setPresenter(presenter: AddEditTaskContract.Presenter) {
        this.presenter = presenter
    }
}