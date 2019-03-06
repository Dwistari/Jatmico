package com.example.dwi.jatmico.view.search

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.dwi.jatmico.R
import kotlinx.android.synthetic.main.activity_search.*
import android.widget.SearchView.OnQueryTextListener
import com.example.dwi.jatmico.data.models.Issues

class SearchActivity : AppCompatActivity() , SearchView {

    private lateinit var adapter: SearchAdapter
    private lateinit var presenter: SearchPresenter

    private var project_id = 0
    private var sub_id = 0
    private var page = 1
    private var per_page = 10
    private var isDataEnd = false
    private var isLoading = false

    override fun showLoading() {
        loadings.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loadings.visibility = View.GONE
    }

    override fun showErrorAlert(it: Throwable) {
        Log.e("SearchActivity", it?.localizedMessage)
        Toast.makeText(this@SearchActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun showData(search: MutableList<Issues>) {
        Log.d("data_size", search.size.toString())

        if (search.size == 0) {
            tv_noView.text = ("Pencarian tidak ditemukan")
//            card_recycler_search?.visibility = View.INVISIBLE

        } else {
            adapter.setData(search)
            tv_noView?.visibility = View.INVISIBLE
//            refresh?.isRefreshing = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        initPresenter()
        initRecylerView()
        project_id = intent.getIntExtra("project_id", project_id)
        sub_id = intent.getIntExtra("sub_id", sub_id)

        search_isues.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                    getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->
                        presenter.getSearch(
                            query!!, project_id, page, per_page,
                            sp.getString(getString(R.string.access_token), "")!!)

                        presenter.getSearchSub(
                            query, page, per_page,
                            sp.getString(getString(R.string.access_token), "")!!)

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }


    private fun refreshItem() {
        page = 1
        isLoading = false
        isDataEnd = false
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun initRecylerView() {
       card_recycler_search.layoutManager = LinearLayoutManager(this)
        adapter = SearchAdapter()
        card_recycler_search.adapter = adapter
    }

    private fun initPresenter() {
        presenter = SearchPresenterImp()
        presenter.initView(this)
    }
}

