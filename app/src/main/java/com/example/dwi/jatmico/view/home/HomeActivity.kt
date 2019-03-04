package com.example.dwi.jatmico.view.home

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.models.Me
import com.example.dwi.jatmico.data.models.Project
import com.example.dwi.jatmico.view.my_submission.MySubmissionActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), HomeView {

    private lateinit var adapter: HomeAdapter
    private lateinit var presenter: HomePresenter
    private var page = 1
    private var perPage = 10

    override fun dismissLoading() {
        loading.visibility = View.GONE
    }

    override fun showErrorAlert(it: Throwable) {

        Log.e("HomeActivity", it.localizedMessage)
        Toast.makeText(this@HomeActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun showingData(me: Me?) {

        name.text = me?.name

        if (me?.location !=null) {
            place.text = me.location
        }else{
            place.text = "Alamat"
        }
        if (me?.image?.url != null) {
            Picasso.with(this).load(me.image.url).into(profile)
        }else{
            profile?.setImageResource(R.drawable.ic_person_black)
        }

    }

    override fun showData(projects: MutableList<Project>) {
        adapter.setData(projects)
    }

    override fun showLoading() {

        loading.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initPresenter()
        initRecylerView()
        presenter.getProjects(page, perPage)
        presenter.getMe()

        btn_sub.setOnClickListener { view ->
            val intent = Intent(this@HomeActivity, MySubmissionActivity::class.java)
            startActivity(intent)

        }

    }

    private fun initRecylerView() {
        card_recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = HomeAdapter()
        card_recycler_view.adapter = adapter
    }

    private fun initPresenter() {
        presenter = HomePresenterImp(getSharedPreferences("Jatmico", MODE_PRIVATE))
        presenter.initView(this)
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }
}



