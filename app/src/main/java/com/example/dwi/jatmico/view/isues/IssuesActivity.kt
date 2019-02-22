package com.example.dwi.jatmico.view.isues

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.dwi.jatmico.data.models.Isues
import kotlinx.android.synthetic.main.activity_issues.*
import android.view.Menu
import android.view.MenuItem
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.models.Severity
import com.example.dwi.jatmico.view.create.CreateIssueActivity
import com.example.dwi.jatmico.view.detail_isues.DetailIssueActivity
import com.example.dwi.jatmico.view.search.SearchActivity
import java.util.*


class IssuesActivity : AppCompatActivity(), IssuesView {
    private lateinit var adapter: IssuesAdapter
    private lateinit var presenter: IssuesPresenter

    var access_token = ""
    private var project_id = 0
    private var page = 1
    private var per_page = 20
    private var isDataEnd = false
    private var isLoading = false

    private var severityId: Int? = null
    private var severities: MutableList<Severity>? = ArrayList()
    private var severitiesNames: MutableList<String>? = ArrayList()
    private var issuesData: MutableList<Isues>? = null
    private var sort = arrayOf("New", "OLD", "Most Severe", "Less Severe")


    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loading.visibility = View.GONE
    }

    override fun showErrorAlert(it: Throwable) {
        isLoading = false
        swipe_refresh?.isRefreshing = false
        Log.e("IssuesActivity", it.localizedMessage)
        Toast.makeText(this@IssuesActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun showData(isues: MutableList<Isues>) {
        Log.d("data_size", isues.size.toString())
        issuesData = isues
        adapter.setData(filterSeverity(isues))
        swipe_refresh?.isRefreshing = false
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
    private fun filterSeverity(severity: MutableList<Isues>): MutableList<Isues> {
        Log.d("filterSeverity", severity.toString())

        val filterSeverity: MutableList<Isues> = ArrayList()
        if (severityId != null) {

            severity.forEach {
                Log.d("filter", "sev id: ${it.id}")

                if (severityId == it.severity.id) {

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
        presenter.getIsues(project_id,page, per_page)
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issues)
//        supportActionBar?.title = resources.getText(project_id)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initPresenter()
        initRecylerView()
        project_id = intent.getIntExtra("project_id", project_id)
        presenter.getIsues(project_id,page, per_page)
        presenter.getSeverity(access_token)
        swipe_refresh?.setOnRefreshListener {
                refreshItem()
            }



        fab.setOnClickListener { view ->
            val intent = Intent(this@IssuesActivity, CreateIssueActivity::class.java)
            startActivityForResult(intent, 1)

        }
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


            builder.setPositiveButton("OK") { dialog, which ->
                severityId = severities?.get((dialog as AlertDialog).listView.checkedItemPosition)?.id

                issuesData?.let { adapter.setData(filterSeverity(it)) }

            }

            builder.setNegativeButton("CANCEL") { dialog, which ->
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

                    Collections.sort(issuesData, object : Comparator<Isues> {
                        override fun compare(o1: Isues?, o2: Isues?): Int {
                            return o2?.createdAt?.compareTo(o1?.createdAt!!)!!
                        }

                    })
                    issuesData?.let { adapter.setData(it) }

                } else if (position == 1) {
                    //ASC DATE

                    Collections.sort(issuesData, object : Comparator<Isues> {
                        override fun compare(o1: Isues?, o2: Isues?): Int {
                            return o1?.createdAt?.compareTo(o2?.createdAt!!)!!
                        }

                    })
                    issuesData?.let { adapter.setData(it) }

                } else if (position == 2) {
                    //ASC Severity Id

                    for (project in issuesData!!) {
                        Log.d("unsorteddata", project.severity.id.toString())
                    }

                    Collections.sort(issuesData, object : Comparator<Isues> {
                        override fun compare(o1: Isues?, o2: Isues?): Int {
                            return o1?.severity?.id!!.compareTo(o2?.severity!!.id)
                        }

                    })

                    Log.d("sorteddata", issuesData.toString())
                    issuesData?.let { adapter.setData(it) }

                } else {
                    //DESC Severity Id

                    for (project in issuesData!!) {
                        Log.d("unsorteddata", project.severity.id.toString())
                    }
                    Collections.sort(issuesData, object : Comparator<Isues> {
                        override fun compare(o1: Isues?, o2: Isues?): Int {
                            return o2?.severity?.id!!.compareTo(o1?.severity!!.id)
                        }

                    })

                    Log.d("sorteddata", issuesData.toString())

                    issuesData ?.let { adapter.setData(it) }
                }


            }
            val dialog: AlertDialog? = builder.create()
            dialog?.show()
        }

        return super.onOptionsItemSelected(item)
    }



    private fun initRecylerView() {
        card_recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = IssuesAdapter()
        adapter.listener = object : IssuesAdapter.Listener {

            override fun onClickItem(isues: Isues, position : Int) {
                val intent = Intent(this@IssuesActivity, DetailIssueActivity::class.java)
                intent.putExtra("issue_id", isues.id)
                intent.putExtra("position", position)
                startActivityForResult(intent, 2)
//                startActivity(intent)

            }
        }
        card_recycler_view.adapter = adapter
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
//                                    position?.let { adapter.addItem(it) }
                    )
                }
            }

        }
    }
}

