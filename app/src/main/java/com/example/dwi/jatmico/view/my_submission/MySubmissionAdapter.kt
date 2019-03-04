package com.example.dwi.jatmico.view.my_submission

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.models.Issues
import com.example.dwi.jatmico.data.models.Project
import com.example.dwi.jatmico.data.models.Severity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_isues.view.*
import java.text.SimpleDateFormat
import java.util.*

class MySubmissionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    private var submission: MutableList<Issues> = ArrayList()
    private var project: MutableList<Project> = ArrayList()
    private var severity: MutableList<Severity> = ArrayList()
    private val VIEW_TYPE_ISSUE = 0
    private val VIEW_TYPE_LOADING = 1
    var listener: Listener? = null


    override fun getItemViewType(position: Int): Int {
        return when {
            submission.get(position).id != null -> VIEW_TYPE_ISSUE
            else -> VIEW_TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ISSUE -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_isues, parent, false)
                return SubViewHolder(itemView)
            }
            else -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.loading_layout, parent, false)
                LoadingViewHolder(itemView)

            }

        }
    }

    override fun getItemCount(): Int {
        return submission.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is SubViewHolder -> {
                holder.onBind(position)
            }
            is LoadingViewHolder -> {
                holder.onBind()
            }
        }
    }

    inner class SubViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val bugname = itemView?.findViewById<TextView>(R.id.bug_name)
        val description = itemView?.findViewById<TextView>(R.id.descripsion)
        val username = itemView?.findViewById<TextView>(R.id.name_user)
        val imageUser = itemView?.findViewById<ImageView>(R.id.profile_user)
        val time = itemView?.findViewById<TextView>(R.id.time)
        val severity = itemView?.findViewById<Button>(R.id.severity)

        fun onBind(position: Int) {
            itemView.setOnClickListener {
                listener?.onClickItem(submission[position], position)
            }
            if (submission[position].user?.image?.url != null) {
                Picasso.with(itemView.getContext()).load(submission[position].user?.image?.url)
                    .into(itemView.profile_user)
            }else{
                imageUser?.setImageResource(R.drawable.ic_profile)
            }
            val date = (submission[position].updated_at)
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val dates = input.parse(date)
            val output = SimpleDateFormat("dd/MMMM/yy", Locale.US)

            time?.text = output.format(dates)

            bugname?.text = submission[position].title
            description?.text =submission[position].description
            username?.text = submission[position].user?.name
            severity?.text = submission[position].severity?.name
            severity?.setBackgroundColor(Color.parseColor(submission[position].severity?.color))

        }
    }

    inner class LoadingViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val loading = itemView?.findViewById<LinearLayout>(R.id.loading)

        fun onBind() {}
    }

    interface Listener {
        fun onClickItem(issues: Issues, position: Int)
    }

    //show isues
    fun setData(submission: MutableList<Issues>) {
        this.submission = submission
        notifyDataSetChanged()
    }

    //show project
    fun setProject(project: MutableList<Project>) {
        this.project = project
        notifyDataSetChanged()
    }

    //show severity
    fun setSeverity(severities: MutableList<Severity>) {
        this.severity = severities
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        submission.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addData(submission: MutableList<Issues>) {
        this.submission.addAll(submission)
        notifyItemRangeInserted(itemCount, submission.size)
    }

    fun setLoading() {
        submission.add(
        Issues(null, null, null, null, null, null, null, null,
                null, null))
        notifyItemInserted(submission.size - 1)
    }

    fun hideLoading() {
        submission.removeAt(submission.size - 1)
        notifyItemRemoved(submission.size)
    }


}