package com.example.dwi.jatmico.view.my_submission

import android.content.DialogInterface
import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.models.Isues
import com.example.dwi.jatmico.data.models.Project
import com.example.dwi.jatmico.data.models.Severitys
import com.example.dwi.jatmico.view.detail_isues.DetailIssueActivity
import com.example.dwi.jatmico.view.search.SearchActivity
import kotlinx.android.synthetic.main.activity_my_submission.*
import kotlin.collections.ArrayList


class MySubmissionActivity : AppCompatActivity(), MySubmissionView {

    private var sortId: Int? = null
    private  var severityId: Int? = null
    private var projectNames: MutableList<String>? = null
    private var severities: MutableList<Severitys>? = ArrayList()
    private var severitiesNames: MutableList<String>? = ArrayList()
    private var submissionData: MutableList<Isues>? = null
    private var sort = arrayOf("New", "OLD", "Most", "Less")


    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loading.visibility = View.GONE
    }

    override fun showErrorAlert(it: Throwable) {
        Log.e("MySubmissionActivity", it?.localizedMessage)
        Toast.makeText(this@MySubmissionActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun showData(submission: MutableList<Isues>) {
        Log.d("data_size", submission.size.toString())
        submissionData = submission
        adapter.setData(filterSubmission(submission))
        adapter.setData(filterSeverity(submission))
    }

    //show severities data
    override fun showSeverity(severities: MutableList<Severitys>) {
//        val dialog: AlertDialog?
//        severityId = severities?.get((dialog as AlertDialog).listView.checkedItemPosition)?.id
        submissionData?.let { adapter.setData(filterSeverity(it)) }
        this.severities?.addAll(severities)
        adapter.setSeverity(severities)

        severities.forEachIndexed { i, item ->
            if (i < 3) {
                severitiesNames?.add(item.name)
            }
        }

//        severitiesNames = ArrayList()
//        for (severity in severities) {
//            severitiesNames?.add(severity.name)
//        }

    }

    override fun showsProject(projects: MutableList<Project>) {
        sortId = projects.get(0).id
        submissionData?.let { adapter.setData(filterSubmission(it)) }

        adapter.setProject(projects)

        projectNames = ArrayList()
        for (project in projects) {
            projectNames?.add(project.name)

//         Picasso.with(projectNames?.).load(project.image).project.image.url)
//            .into(R.layout.spinner_item,R.id.icon_project)
//            Picasso.with(this).load(project.image?.url).into(icon_project)
        }

        // Initializing an ArrayAdapter
        val spinnerArrayAdapter = object : ArrayAdapter<String>(
            this, R.layout.spinner_item, R.id.item_spiner, projectNames
        ) {

            override fun getDropDownView(
                position: Int, convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                return view
            }
        }

//--SPINNER--
        select_project.adapter = spinnerArrayAdapter

        select_project?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                select_project.getItemAtPosition(position).toString()

                sortId = projects.get(position).id
                submissionData?.let { adapter.setData(filterSubmission(it)) }

                spinnerArrayAdapter.notifyDataSetChanged()

            }

        }
    }

    private fun filterSubmission(submission: MutableList<Isues>): MutableList<Isues> {
        Log.d("filterSubmission", submission.size.toString())
        val filteredSubmission: MutableList<Isues> = ArrayList()
        if (sortId != null) {

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

    private fun filterSeverity(severity: MutableList<Isues>): MutableList<Isues> {
        Log.d("filterSeverity", severity.toString())

        //NOT SHOW
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

    private lateinit var adapter: MySubmissionAdapter
    private lateinit var presenter: MySubmissionPresenter

    var access_token = ""
    private var position: Int? = null
    private var project_id = 0
    private var page = 1
    private var per_page = 20


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_submission)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initPresenter()
        initRecylerView()
        position = intent.getIntExtra("position", 0)

        getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->
            access_token = sp.getString(getString(R.string.access_token), "")

        }
//        presenter.getSub(sortBy,sortSeverity, page, per_page, access_token)

        presenter.getSub(page, per_page, access_token)
        presenter.getProjects(page, per_page, access_token)
        presenter.getSeverity(access_token)
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
            intent.putExtra("sub_id", project_id)
            startActivity(intent)
            return true
        }
        if (id == R.id.filter) {
            Log.d("SEVERITY", severitiesNames.toString())
            val dialog: AlertDialog?
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Severity")
            builder.setSingleChoiceItems(severitiesNames?.toTypedArray(), -1, null)


            builder.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    severityId = severities?.get((dialog as AlertDialog).listView.checkedItemPosition)?.id

                    submissionData?.let { adapter.setData(filterSeverity(it)) }

//                    Toast.makeText(this@MySubmissionActivity, severityId.toString(), Toast.LENGTH_SHORT).show()
                }

            })

            builder.setNegativeButton("CANCEL") { dialog, which ->
                dialog.dismiss()
            }

            dialog = builder.create()
            dialog?.show()

        }

        if (id == R.id.sort_by) {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Severity")
            builder.setSingleChoiceItems(sort, -1, null)


            builder.setPositiveButton("OK") { dialog, id ->

                Toast.makeText(this, "Its toast!", Toast.LENGTH_SHORT).show()

            }

            builder.setNegativeButton("CANCEL") { dialog, which ->
                dialog.dismiss()
            }


            val dialog: AlertDialog? = builder.create()
            dialog?.show()

        }

        return super.onOptionsItemSelected(item)

    }


    private fun initRecylerView() {
        card_recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = MySubmissionAdapter()
        adapter.listener = object : MySubmissionAdapter.Listener {

            override fun onClickItem(submission: Isues, position: Int) {
                val intent = Intent(this@MySubmissionActivity, DetailIssueActivity::class.java)
                intent.putExtra("issue_id", submission.id)
                intent.putExtra("position", position)
                startActivityForResult(intent, 1)
//                startActivity(intent)

            }
        }
        card_recycler_view.adapter = adapter
    }

    private fun initPresenter() {
        presenter = MySubmissionPresenterImp()
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

