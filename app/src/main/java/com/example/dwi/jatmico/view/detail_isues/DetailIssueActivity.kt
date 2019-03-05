package com.example.dwi.jatmico.view.detail_isues

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.models.Detail
import com.example.dwi.jatmico.view.create.CreateIssueActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_issue.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class DetailIssueActivity : AppCompatActivity(), DetailIssueView {

    private var isues_id = 0
    private var user_id = 0
    private var isDataEnd = false
    private var isLoading = false
    private var position: Int? = null
    private var issue: Detail? = null
    private var editMenu: MenuItem? = null
    private var deleteMenu: MenuItem? = null
    private lateinit var issuePresenter: DetailIssuePresenter

    override fun dismissLoading() {
        loadings.visibility = View.GONE
    }

    override fun showLoading() {
        loadings.visibility = View.GONE
    }

    override fun showErrorAlert(e: Throwable) {

        Log.e("DetailIssueActivity", e.localizedMessage)
        Toast.makeText(this@DetailIssueActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun showingData(detail: Detail?) {
        issue = detail
        user.text = detail?.user?.name
        descripsion.text = detail?.description

        if (user_id != detail?.user?.id) {
            editMenu?.isVisible = false
            deleteMenu?.isVisible = false
        }

        if (detail?.title != null) {
            title_isues.text = detail.title
        } else {
            title_isues.text = ("No title available")
        }

        if (detail?.link != null) {
            link.text = detail.link
        } else {
            link.text = ("no link available")
        }

        if (detail?.image?.url != null) {
            Picasso.with(this).load(detail?.image?.url).into(detail_image)
        } else {
            detail_image?.setImageResource(R.drawable.no_image)
        }

        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val past = format.parse(detail?.updated_at)
        val now = Date()
        val convert = SimpleDateFormat("MMMM dd, yyyy", Locale.US)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
        val days = TimeUnit.MILLISECONDS.toDays(now.time - past.time)

        when {
            seconds < 60 -> time?.text = (seconds.toString() + "" + "sec ago")
            minutes < 60 -> time?.text = (minutes.toString() + "" + "min ago")
            hours < 24 -> time?.text = (hours.toString() + "" + "hrs ago")
//                hours > 24 && < 48 -> time?.text = (days.toString()  + "day ago")
            else -> time?.text = convert.format(past)
        }
        refresh?.isRefreshing = false
    }

    override fun onSuccess() {
        val intentResult = Intent()
        intentResult.putExtra("position", position)
        setResult(RESULT_OK, intentResult)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_issue)
        setSupportActionBar(toolbar_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initPresenter()

        isues_id = intent.getIntExtra("issue_id", isues_id)
        position = intent.getIntExtra("position", 0)

        getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->
            issuePresenter.getDetail(isues_id, sp.getString(getString(R.string.access_token), "")!!)
            user_id = sp.getInt("user_id", 0)


        }
        refresh?.setOnRefreshListener {
            refreshItem()
        }
    }

    private fun refreshItem() {
        isLoading = false
        isDataEnd = false
//        issuePresenter.getDetail(isues_id, sp.getString(getString(R.string.access_token), "")!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        editMenu = menu?.findItem(R.id.menu_edit)
        deleteMenu = menu?.findItem(R.id.menu_delete)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item?.getItemId()

        if (id == R.id.menu_edit) {
            val intent = Intent(this@DetailIssueActivity, CreateIssueActivity::class.java)
            intent.putExtra("data", issue)

            startActivity(intent)
            return true
        }

        if (id == R.id.menu_delete) {
            val builder = AlertDialog.Builder(this@DetailIssueActivity)
            builder.setMessage("Are you sure to delete this issue?")
            builder.setPositiveButton("YES") { dialog, which ->

                getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->
                    issuePresenter.delIssues(
                        isues_id.toString(),
                        sp.getString(getString(R.string.access_token), "")!!
                    )

                }

            }
            builder.setNegativeButton("No") { dialog, which -> }

            val dialog: AlertDialog = builder.create()
            dialog.show()


        }

        return super.onOptionsItemSelected(item)
    }

    private fun initPresenter() {
        issuePresenter = DetailIssuePresenterImp()
        issuePresenter.initView(this)
    }
}
