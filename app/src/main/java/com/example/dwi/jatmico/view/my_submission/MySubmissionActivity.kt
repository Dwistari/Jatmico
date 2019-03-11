package com.example.dwi.jatmico.view.my_submission

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.models.Issues
import com.example.dwi.jatmico.data.models.Project
import com.example.dwi.jatmico.data.models.Severity
import com.example.dwi.jatmico.utils.OnScrollListener
import com.example.dwi.jatmico.view.BaseActivity
import com.example.dwi.jatmico.view.detail_isues.DetailIssueActivity
import com.example.dwi.jatmico.view.search.SearchActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_my_submission.*
import kotlinx.android.synthetic.main.spinner_item.view.*
import java.util.*
import kotlin.collections.ArrayList


class MySubmissionActivity : BaseActivity(), MySubmissionView {

    private lateinit var adapter: MySubmissionAdapter
    private lateinit var presenter: MySubmissionPresenter

    var access_token = ""
    private var position: Int? = null
    private var sub_id = 0
    private var page = 1
    private var per_page = 10
    private var isDataEnd = false
    private var isLoading = false
    private var mContext: Context? = null
    private var layoutManager: LinearLayoutManager? = null
    private var sortId: Int? = null
    private var severityId: Int? = null
    private var projectNames: MutableList<String>? = null
    private var severities: MutableList<Severity>? = ArrayList()
    private var severitiesNames: MutableList<String>? = ArrayList()
    private var submissionData: MutableList<Issues>? = null
    private var sort = arrayOf("New", "OLD", "Most Severe", "Less Severe")


    override fun getLayout(): Int {
        return R.layout.activity_my_submission
    }

    override fun onViewReady() {
        setSupportActionBar(toolbar_search)
        supportActionBar?.title = "My Submission"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initPresenter()
        initRecylerView()
        position = intent.getIntExtra("position", 0)
        presenter.getSub(page, per_page)
        presenter.getProjects(page, per_page)
        presenter.getSeverity()
        swipe_refresh?.setOnRefreshListener {
            refreshItem()
        }

    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loading.visibility = View.GONE
    }

    override fun showErrorAlert(it: Throwable) {
        Log.e("MySubmissionActivity", it?.localizedMessage)
        swipe_refresh?.isRefreshing = false
        Toast.makeText(this@MySubmissionActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun showData(submission: MutableList<Issues>) {
        Log.d("data_size", submission.size.toString())
        submissionData = submission
        adapter.setData(filterSubmission(submission))
        adapter.setData(filterSeverity(submission))
        swipe_refresh?.isRefreshing = false
    }

    override fun addData(submission: MutableList<Issues>) {
        adapter.hideLoading()
        isLoading = false
        adapter.addData(filterSeverity(submission))
        adapter.addData(filterSubmission(submission))


    }

    override fun showEmptyAlert() {
        Toast.makeText(this@MySubmissionActivity, "No issues found", Toast.LENGTH_SHORT).show()
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

    override fun showsProject(projects: MutableList<Project>) {
//        sortId = projects.get(0).id
        adapter.setProject(projects)

        projectNames = ArrayList()
        for (project in projects) {
            projectNames?.add(project.name)

        }
        val spinnerArrayAdapter = ProjectListAdapter(this@MySubmissionActivity, projects)
        select_project.adapter = spinnerArrayAdapter

        select_project?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    if (Intent.ACTION_SEND != intent.action && intent.type == null) {

                        sortId = projects.get(position).id
                        submissionData?.let { adapter.setData(filterSubmission(it)) }

                        spinnerArrayAdapter.notifyDataSetChanged()
                    }

                }
            }
    }

    class ProjectListAdapter(private val ctx: Context, val projects: MutableList<Project>?) :
        ArrayAdapter<Project>(ctx, R.layout.spinner_item, R.id.item_spiner, projects) {

        override fun getDropDownView(
            position: Int, convertView: View?,
            parent: ViewGroup
        ): View {
            return getCustomView(position, parent)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            return getCustomView(position, parent)
        }

        @SuppressLint("SetTextI18n")
        fun getCustomView(position: Int, parent: ViewGroup): View {

            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val row = inflater.inflate(R.layout.spinner_item, parent, false)

            row.item_spiner.text = projects?.get(position)?.name

            if (projects?.get(position)?.image?.thumb?.url != null) {
                row.icon_project.visibility = View.VISIBLE
                Picasso
                    .with(row.context)
                    .load(projects?.get(position).image?.thumb?.url)
                    .into(row.icon_project)
            } else {
                row.icon_project.visibility = View.GONE
            }

            return row
        }
    }


    private fun filterSubmission(submission: MutableList<Issues>): MutableList<Issues> {
        Log.d("filterSubmission", submission.size.toString())
        val filteredSubmission: MutableList<Issues> = ArrayList()
        if (sortId != -1) {

            submission.forEach {
                Log.d("filter", "sub id: ${it.id}")

                if (sortId == it.projectId) {
                    filteredSubmission.add(it)

                    Log.d("tes submission", filteredSubmission.size.toString())
                }
            }
            return filteredSubmission
        } else {
            return submission
        }

    }

    private fun filterSeverity(severity: MutableList<Issues>): MutableList<Issues> {
        Log.d("filterSeverity", severity.size.toString())

        val filterSeverity: MutableList<Issues> = ArrayList()
        if (severityId != null) {

            severity.forEach {
                Log.d("filter", "sev id: ${it.id}")

                if (severityId == it.severity?.id && sortId == it.projectId) {
                    filterSeverity.add(it)
                    Log.d("tes Severity", filterSeverity.size.toString())

                } else if (filterSeverity.size == 0) {
                    tv_noViews.text = ("Data tidak tersedia")
                } else if (filterSeverity.size != 0) {
                    tv_noViews.visibility = View.INVISIBLE
                } else {
                    return filterSeverity
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
        presenter.getSub(page, per_page)
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

            val intent = Intent(this@MySubmissionActivity, SearchActivity::class.java)
            intent.putExtra("sub_id", sub_id)
            startActivity(intent)
            return true
        }
        if (id == R.id.filter) {
            Log.d("SEVERITY", severitiesNames.toString())
            var alertDialog: AlertDialog? = null
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Severity")
            builder.setSingleChoiceItems(severitiesNames?.toTypedArray(), -1)
            { dialog, which ->
                alertDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = true
            }


            builder.setPositiveButton("OK") { dialog, which ->
                severityId = severities?.get((dialog as AlertDialog).listView.checkedItemPosition)?.id

                submissionData?.let { adapter.setData(filterSeverity(it)) }

            }

            builder.setNegativeButton("CANCEL") { dialog, which ->
                dialog.dismiss()
            }

            alertDialog = builder.create()
            alertDialog?.show()
            alertDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.let { it.isEnabled = false } ?: Log.d("DIALOG", "btn is null")

        }

        if (id == R.id.sort_by) {
            val builder = AlertDialog.Builder(this)
            builder.setItems(sort) { dialog: DialogInterface, position: Int ->
                if (position == 0) {
                    //DESC DATE

                    Collections.sort(submissionData, object : Comparator<Issues> {
                        override fun compare(o1: Issues?, o2: Issues?): Int {
                            return o2?.createdAt?.compareTo(o1?.createdAt!!)!!
                        }

                    })
                    submissionData?.let { adapter.setData(it) }

                } else if (position == 1) {
                    //ASC DATE

                    Collections.sort(submissionData, object : Comparator<Issues> {
                        override fun compare(o1: Issues?, o2: Issues?): Int {
                            return o1?.createdAt?.compareTo(o2?.createdAt!!)!!
                        }

                    })
                    submissionData?.let { adapter.setData(it) }

                } else if (position == 2) {
                    //ASC Severity Id

                    for (project in submissionData!!) {
                        Log.d("unsorteddata", project.severity?.id.toString())
                    }

                    Collections.sort(submissionData, object : Comparator<Issues> {
                        override fun compare(o1: Issues?, o2: Issues?): Int {
                            return o1?.severity?.id!!.compareTo(o2?.severity!!.id)
                        }

                    })

                    Log.d("sorteddata", submissionData.toString())
                    submissionData?.let { adapter.setData(it) }

                } else {
                    //DESC Severity Id

                    for (project in submissionData!!) {
                        Log.d("unsorteddata", project.severity?.id.toString())
                    }
                    Collections.sort(submissionData, object : Comparator<Issues> {
                        override fun compare(o1: Issues?, o2: Issues?): Int {
                            return o2?.severity?.id!!.compareTo(o1?.severity!!.id)
                        }

                    })

                    Log.d("sorteddata", submissionData.toString())

                    submissionData?.let { adapter.setData(it) }
                }


            }
            val dialog: AlertDialog? = builder.create()
            dialog?.show()
        }

        return super.onOptionsItemSelected(item)
    }


    private fun initRecylerView() {
        adapter = MySubmissionAdapter()
        layoutManager = LinearLayoutManager(mContext)
        card_recycler_view.layoutManager = layoutManager
        (card_recycler_view.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        card_recycler_view.addOnScrollListener(object : OnScrollListener(layoutManager) {
            override fun loadMoreItem() {
                isLoading = true
                adapter.setLoading()
                Handler().postDelayed({
                    page += 1
                    presenter.getSub(page, per_page)
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
        adapter.listener = object : MySubmissionAdapter.Listener {
            override fun onClickItem(submission: Issues, position: Int) {
                val intent = Intent(this@MySubmissionActivity, DetailIssueActivity::class.java)
                intent.putExtra("issue_id", submission.id)
                intent.putExtra("position", position)
                startActivityForResult(intent, 1)
            }
        }
    }

    private fun initPresenter() {
        presenter = MySubmissionPresenterImp(getSharedPreferences("Jatmico", MODE_PRIVATE))
        presenter.initView(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                val position = data?.getIntExtra("position", 0)
                Log.d("position_received", position.toString())
                position?.let { adapter.deleteItem(it) }
            }

            if (requestCode == 3) {
                if (resultCode == RESULT_OK) {
                    finish()
                }
            }

        }
    }
}

