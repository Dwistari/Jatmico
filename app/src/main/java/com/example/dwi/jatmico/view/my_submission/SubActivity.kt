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
import com.example.dwi.jatmico.presenter.SubPresenter
import com.example.dwi.jatmico.presenter.SubPresenterImp
import com.example.dwi.jatmico.view.detail_isues.DetailActivity
import com.example.dwi.jatmico.view.search.SearchActivity
import kotlinx.android.synthetic.main.activity_submission.*

class SubActivity : AppCompatActivity(), SubView {

    var ProjectNames: MutableList<String>? = null
    var SeverityNames: MutableList<String>? = ArrayList()
    var sort_id: Int? = null
    var submissionData: MutableList<Isues>? = null
    var severityData: MutableList<Severitys>? = null
    var sort = arrayOf("New", "OLD", "Most", "Less")


    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loading.visibility = View.GONE
    }

    override fun showErrorAlert(it: Throwable) {
        Log.e("SubActivity", it?.localizedMessage)
        Toast.makeText(this@SubActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun showData(submission: MutableList<Isues>) {
        Log.d("data_size", submission.size.toString())
        submissionData = submission
        adapter.setData(filterSubmission(submission))
    }

    //show severityName data
    override fun showSeverity(severityName: MutableList<Severitys>) {
        severityData = severityName
        severityName.forEachIndexed { i, item ->
            if (i<3) {
                SeverityNames?.add(item.name)
            }
        }
        adapter.setSeverity(severityName)

    }

    //--SPINNER SHOW DATA Project
    override fun showsData(projects: MutableList<Project>) {
        Log.d("Show_Project", projects.size.toString())

        sort_id = projects.get(0).id
        submissionData?.let { adapter.setData(filterSubmission(it)) }
        adapter.setsData(projects)

        ProjectNames = ArrayList()
        for (project in projects) {
            ProjectNames?.add(project.name)

//         Picasso.with(ProjectNames?.getContext()).load(project[position].project.image.url)
//            .into(itemView?.profile_user)
        }
        // Initializing an ArrayAdapter
        val spinnerArrayAdapter = object : ArrayAdapter<String>(
            this, R.layout.spinner_item, R.id.item_spiner, ProjectNames
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
//                presenter.getIsues(projects[position].id, page, per_page,access_token)
                sort_id = projects.get(position).id
                submissionData?.let { adapter.setData(filterSubmission(it)) }

                spinnerArrayAdapter.notifyDataSetChanged()

            }

        }
    }

    private fun filterSubmission(submission: MutableList<Isues>): MutableList<Isues> {
        var filteredSubmission: MutableList<Isues> = ArrayList()
        if (sort_id != null) {

            submission.forEach {
                Log.d("filter", "sub id: ${it.id}")

                if (it.projectId == sort_id) {
                    filteredSubmission.add(it)
                }
            }
            return filteredSubmission
        } else {
            return submission
        }

    }

    private lateinit var adapter: SubAdapter
    private lateinit var presenter: SubPresenter

    var access_token = ""
    private var position: Int? = null
    private var sortBy = ""
    private var sortSeverity = 0
    private var page = 1
    private var per_page = 20


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submission)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initPresenter()
        initRecylerView()
        position = intent.getIntExtra("position", 0)

        getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->
            access_token = sp.getString(getString(R.string.access_token), "")

        }
        presenter.getSub(sortBy,sortSeverity, page, per_page, access_token)

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

            val intent = Intent(this@SubActivity, SearchActivity::class.java)
            startActivity(intent)
            return true
        }
        if (id == R.id.filter) {
            Log.d("SEVERITY",SeverityNames.toString())
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Severity")
            builder.setSingleChoiceItems(SeverityNames?.toTypedArray(), -1, null)

            DialogInterface.OnClickListener { dialogInterface: DialogInterface, which ->

//                 SeverityNames.[which]
            }



            builder.setPositiveButton("OK") { dialog, id ->

                Toast.makeText(this, "Its toast!", Toast.LENGTH_SHORT).show()

            }

            builder.setNegativeButton("CANCEL") { dialog, which ->
                dialog.dismiss()
            }


            val dialog: AlertDialog? = builder.create()
            dialog?.show()

        }
        if (id == R.id.sort_by) {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Severity")
            builder.setSingleChoiceItems(sort, -1, null)

            DialogInterface.OnClickListener { dialogInterface: DialogInterface, which ->

                // mSelectedItems.[which]
            }
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
        adapter = SubAdapter()
        adapter.listener = object : SubAdapter.Listener {

            override fun onClickItem(submission: Isues, position: Int) {
                val intent = Intent(this@SubActivity, DetailActivity::class.java)
                intent.putExtra("issue_id", submission.id)
                intent.putExtra("position", position)
                startActivityForResult(intent, 1)
//                startActivity(intent)

            }
        }
        card_recycler_view.adapter = adapter
    }

    private fun initPresenter() {
        presenter = SubPresenterImp()
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

