package com.example.dwi.jatmico.view.isues

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.models.Isues
import kotlinx.android.synthetic.main.activity_issues.*
import android.view.Menu
import android.view.MenuItem
import com.example.dwi.jatmico.view.create.CreateIssueActivity
import com.example.dwi.jatmico.view.detail_isues.DetailIssueActivity
import com.example.dwi.jatmico.view.search.SearchActivity


class IssuesActivity : AppCompatActivity(), IssuesView {
    private lateinit var adapter: IssuesAdapter
    private lateinit var presenter: IssuesPresenter

    private var project_id = 0
    private var page = 1
    private var per_page = 20

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loading.visibility = View.GONE
    }

    override fun showErrorAlert(it: Throwable) {
        Log.e("IssuesActivity", it.localizedMessage)
        Toast.makeText(this@IssuesActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun showData(isues: MutableList<Isues>) {
        Log.d("data_size", isues.size.toString())
        adapter.setData(isues)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issues)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initPresenter()
        initRecylerView()
        project_id = intent.getIntExtra("project_id", project_id)

        getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->
            presenter.getIsues(
                project_id, page, per_page,
                sp.getString(getString(R.string.access_token), "")!!
            )

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
        presenter = IssuesPresenterImp()
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
                    getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->
                        presenter.getIsues(
                            project_id, page, per_page,
                            sp.getString(getString(R.string.access_token), "")!!
                        )

                    }
                }
            }

        }
    }
}


