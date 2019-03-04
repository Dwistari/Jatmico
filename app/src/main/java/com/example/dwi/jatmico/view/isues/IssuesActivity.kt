package com.example.dwi.jatmico.view.isues

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.dwi.jatmico.data.models.Issues
import kotlinx.android.synthetic.main.activity_issues.*
import android.view.Menu
import android.view.MenuItem
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.models.Project
import com.example.dwi.jatmico.data.models.Severity
import com.example.dwi.jatmico.utils.OnScrollListener
import com.example.dwi.jatmico.view.BaseActivity
import com.example.dwi.jatmico.view.create.CreateIssueActivity
import com.example.dwi.jatmico.view.detail_isues.DetailIssueActivity
import com.example.dwi.jatmico.view.search.SearchActivity
import java.util.*
import kotlin.collections.ArrayList


class IssuesActivity : BaseActivity(), IssuesView {
    private lateinit var adapter: IssuesAdapter
    private lateinit var presenter: IssuesPresenter

    private var project_id = 0
    private var project_name = ""
    private var page = 1
    private var per_page = 10
    private var isDataEnd = false
    private var isLoading = false
    private var mContext: Context? = null
    private var layoutManager: LinearLayoutManager? = null
    private var severityId: Int? = null
    private var severities: MutableList<Severity>? = ArrayList()
    private var severitiesNames: MutableList<String>? = ArrayList()
    private var issuesData: MutableList<Issues>? = null
    private var sort = arrayOf("New", "OLD", "Most Severe", "Less Severe")

    override fun getLayout(): Int {
        return R.layout.activity_issues
    }

    override fun onViewReady() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        project_id = intent.getIntExtra("project_id", project_id)
        project_name = intent.getStringExtra("project_name")
        supportActionBar?.title = project_name
        initPresenter()
        initRecylerView()
        presenter.getSeverity()
        presenter.getProjects(page, per_page)
        swipe_refresh?.setOnRefreshListener {
            refreshItem()
        }

        fab.setOnClickListener {
            val intent = Intent(this@IssuesActivity, CreateIssueActivity::class.java)
            startActivityForResult(intent, 1)

        }
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loading.visibility = View.GONE
    }

    override fun showingProject(projects: MutableList<Project>) {
        adapter.setProject(projects)
        presenter.getIsues(project_id, page, per_page)
    }

    override fun showErrorAlert(it: Throwable) {
        isLoading = false
        swipe_refresh?.isRefreshing = false
        Log.e("IssuesActivity", it.localizedMessage)
        Toast.makeText(this@IssuesActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun showData(issues: MutableList<Issues>) {
        Log.d("data_size", issues.size.toString())
        issuesData = issues
        adapter.setData(filterSeverity(issues))
        swipe_refresh?.isRefreshing = false

    }

    override fun addData(issues: MutableList<Issues>) {
        adapter.hideLoading()
        isLoading = false
        adapter.addData(filterSeverity(issues))
    }

    override fun showEmptyAlert() {
        Toast.makeText(this@IssuesActivity, "No issues found", Toast.LENGTH_SHORT).show()
    }

    override fun dataEnd() {
        isDataEnd = true
    }

    //show severities data
    override fun showSeverity(severities: MutableList<Severity>) {
        this.severities?.addAll(severities)
        adapter.setSeverity(severities)

        severities.forEachIndexed { i, item ->
            if (i < 3) {
                severitiesNames?.add(item.name)
            }
        }

    }

    private fun filterSeverity(severity: MutableList<Issues>): MutableList<Issues> {
        Log.d("filterSeverity", severity.toString())

        val filterSeverity: MutableList<Issues> = ArrayList()
        if (severityId != null) {

            severity.forEach {
                Log.d("filter", "sev id: ${it.id}")

                if (severityId == it.severity?.id) {

                    filterSeverity.add(it)

                    Log.d("tes Severity", filterSeverity.size.toString())

                }

            }
            return filterSeverity
        } else {
            return severity

        }
    }

    private fun refreshItem() {
        page = 1
        isLoading = false
        isDataEnd = false
        presenter.getIsues(project_id, page, per_page)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item?.getItemId()

        if (id == R.id.action_search) {

            val intent = Intent(this@IssuesActivity, SearchActivity::class.java)
            intent.putExtra("project_id", project_id)
            startActivity(intent)
            return true
        }
        if (id == R.id.filter) {
            Log.d("SEVERITY", severitiesNames.toString())
            val dialog: AlertDialog?
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Severity")
            builder.setSingleChoiceItems(severitiesNames?.toTypedArray(), -1, null)


            builder.setPositiveButton("OK") { dialog, _ ->
                severityId = severities?.get((dialog as AlertDialog).listView.checkedItemPosition)?.id

                issuesData?.let { adapter.setData(filterSeverity(it)) }

            }

            builder.setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }

            dialog = builder.create()
            dialog?.show()

        }

        if (id == R.id.sort_by) {
            val builder = AlertDialog.Builder(this)
            builder.setItems(sort) { dialog: DialogInterface, position: Int ->
                if (position == 0) {
                    //DESC DATE

                    Collections.sort(issuesData, object : Comparator<Issues?> {
                        override fun compare(o1: Issues?, o2: Issues?): Int {
                            return o2?.createdAt?.compareTo(o1?.createdAt!!)!!
                        }

                    })
                    issuesData?.let {
                        adapter.setData(it)
                    }

                } else if (position == 1) {
                    //ASC DATE

                    Collections.sort(issuesData, object : Comparator<Issues?> {
                        override fun compare(o1: Issues?, o2: Issues?): Int {
                            return o1?.createdAt?.compareTo(o2?.createdAt!!)!!
                        }

                    })
                    issuesData?.let { adapter.setData(it) }

                } else if (position == 2) {
                    //ASC Severity Id

                    for (project in issuesData!!) {
                        Log.d("unsorteddata", project.severity?.id.toString())
                    }

                    Collections.sort(issuesData, object : Comparator<Issues?> {
                        override fun compare(o1: Issues?, o2: Issues?): Int {
                            return o1?.severity?.id!!.compareTo(o2?.severity!!.id)
                        }

                    })

                    Log.d("sorteddata", issuesData.toString())
                    issuesData?.let { adapter.setData(it) }

                } else {
                    //DESC Severity Id

                    for (project in issuesData!!) {
                        Log.d("unsorteddata", project.severity?.id.toString())
                    }
                    Collections.sort(issuesData, object : Comparator<Issues?> {
                        override fun compare(o1: Issues?, o2: Issues?): Int {
                            return o2?.severity?.id!!.compareTo(o1?.severity!!.id)
                        }

                    })

                    Log.d("sorteddata", issuesData.toString())

                    issuesData?.let {
                        adapter.setData(it)
                    }
                }


            }
            val dialog: AlertDialog? = builder.create()
            dialog?.show()
        }

        return super.onOptionsItemSelected(item)
    }


    private fun initRecylerView() {
        adapter = IssuesAdapter()
        layoutManager = LinearLayoutManager(mContext)
        card_recycler_view.layoutManager = layoutManager
        (card_recycler_view.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        card_recycler_view.addOnScrollListener(object : OnScrollListener(layoutManager) {
            override fun loadMoreItem() {
                isLoading = true
                adapter.setLoading()
                Handler().postDelayed({
                    page += 1
                    presenter.getIsues(project_id, page, per_page)
                }, 500)
            }

            override fun isDataEnd(): Boolean {
                return isDataEnd
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })

        card_recycler_view.adapter = adapter
        adapter.listener = object : IssuesAdapter.Listener {

            override fun onClickItem(issues: Issues, position: Int) {
                val intent = Intent(this@IssuesActivity, DetailIssueActivity::class.java)
                intent.putExtra("issue_id", issues.id)
                intent.putExtra("position", position)
                startActivityForResult(intent, 2)
            }
        }
    }

    private fun initPresenter() {
        presenter = IssuesPresenterImp(getSharedPreferences("Jatmico", MODE_PRIVATE))
        presenter.initView(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check which request we're responding to
        if (requestCode == 2) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                val position = data?.getIntExtra("position", 0)
                Log.d("position_received", position.toString())
                position?.let { adapter.deleteItem(it) }
            }

            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    presenter.getIsues(
                        project_id, page, per_page
                    )
                }
            }

        }
    }
}
