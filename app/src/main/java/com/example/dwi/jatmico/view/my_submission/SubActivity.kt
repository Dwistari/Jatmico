package com.example.dwi.jatmico.view.my_submission

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
import com.example.dwi.jatmico.R.string.access_token
import com.example.dwi.jatmico.data.models.Isues
import com.example.dwi.jatmico.data.models.Project
import com.example.dwi.jatmico.presenter.SubPresenter
import com.example.dwi.jatmico.presenter.SubPresenterImp
import com.example.dwi.jatmico.view.detail_isues.DetailActivity
import com.example.dwi.jatmico.view.search.SearchActivity
import kotlinx.android.synthetic.main.activity_submission.*

class SubActivity : AppCompatActivity(), SubView {

    var ProjectNames: MutableList<String>? = null

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
        adapter.setData(submission)
    }

    //--SPINNER SHOW DATA Project
    override fun showsData(projects: MutableList<Project>) {
        Log.d("Show_Project", projects.size.toString())

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


//                if (projects[position].id == 1 ) {
//                    showData()
//                }else
//                    if(projects[position].id == 2){
//                        showsData()
//                    }

                spinnerArrayAdapter.notifyDataSetChanged()

                select_project.getItemAtPosition(position).toString()
//                presenter.getIsues(projects[position].id, page, per_page,access_token)

            }

        }
    }

//    private fun shortById() {
//        if (project_id)
//
//    }


    private lateinit var adapter: SubAdapter
    private lateinit var presenter: SubPresenter

    var access_token = ""
    private var sort_by_id = 0
    private var project_id = 0
    private var position: Int? = null
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
        presenter.getSub(page, per_page, access_token)

        presenter.getProjects(page, per_page, access_token)
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
            Toast.makeText(this, "Item Two Clicked", Toast.LENGTH_LONG).show()
            return true
        }
        if (id == R.id.sort_by) {
            Toast.makeText(this, "Item Three Clicked", Toast.LENGTH_LONG).show()
            return true
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
