package com.example.dwi.jatmico.view.detail_isues

import android.app.AlertDialog
import android.app.Service
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.Detail
import com.example.dwi.jatmico.presenter.DetailPresenter
import com.example.dwi.jatmico.presenter.DetailPresenterImp
import com.example.dwi.jatmico.view.create.CreateActivity
import com.example.dwi.jatmico.view.isues.IsuesActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity(), DetailView {

    override fun dismissLoading() {
        loadings.visibility = View.GONE
    }

    override fun showLoading() {

        loadings.visibility = View.GONE
    }

    override fun showErrorAlert(it: Throwable) {

        Log.e("DetailActivity", it?.localizedMessage)
        Toast.makeText(this@DetailActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun showingData(detail: Detail?) {

        title_isues.text = detail?.title
        user.text = detail?.user?.name
        time.text = detail?.updated_at
        descripsion.text = detail?.description
        link.text = detail?.link

        Picasso.with(this).load(detail?.image?.url).into(detail_image)

    }

    override fun onSuccess() {
        val intentResult = Intent()
        intentResult.putExtra("position", position)
        setResult(RESULT_OK, intentResult)
        finish()
    }

    private var isues_id = 0
    private var position: Int? = null
    private lateinit var presenter: DetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initPresenter()

        isues_id = intent.getIntExtra("issue_id", isues_id)

        position = intent.getIntExtra("position", 0)
        getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->
            presenter.getDetail(isues_id, sp.getString(getString(R.string.access_token), "")!!)

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item?.getItemId()


        if (id == R.id.menu_edit) {
            val intent = Intent(this@DetailActivity, CreateActivity::class.java)
            startActivity(intent)
            return true
        }
        if (id == R.id.menu_delete) {
            val builder = AlertDialog.Builder(this@DetailActivity)
            builder.setMessage("Are you sure to delete this issue?")
            builder.setPositiveButton("YES") { dialog, which ->

                getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->
                    presenter.delIssues(isues_id.toString(), sp.getString(getString(R.string.access_token), "")!!)


                }

            }
            builder.setNegativeButton("No") { dialog, which -> }

            val dialog: AlertDialog = builder.create()
            dialog.show()


        }

        return super.onOptionsItemSelected(item)
    }

    private fun initPresenter() {
        presenter = DetailPresenterImp()
        presenter.initView(this)
    }
}

