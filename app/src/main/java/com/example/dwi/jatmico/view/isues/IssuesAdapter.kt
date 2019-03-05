package com.example.dwi.jatmico.view.isues

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
import java.util.Locale
import java.util.concurrent.TimeUnit

class IssuesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var issues: MutableList<Issues> = ArrayList()
    private var project: MutableList<Project> = ArrayList()
    private var severity: MutableList<Severity> = ArrayList()
    private val VIEW_TYPE_ISSUE = 0
    private val VIEW_TYPE_LOADING = 1
    var listener: Listener? = null


    override fun getItemViewType(position: Int): Int {
        return when {
            issues.get(position).id != null -> VIEW_TYPE_ISSUE
            else -> VIEW_TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ISSUE -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_isues, parent, false)
                return IsuesViewHolder(itemView)
            }
            else -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.loading_layout, parent, false)
                LoadingViewHolder(itemView)

            }

        }
    }

    override fun getItemCount(): Int {
        return issues.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IsuesViewHolder -> {
                holder.onBind(position)
            }
            is LoadingViewHolder -> {
                holder.onBind()
            }
        }
    }

//        var sf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//        var input = "2018-09-13T10:22:50.000+07:00"
//        var date = sf.parse(input)


    inner class IsuesViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val bugname = itemView?.findViewById<TextView>(R.id.bug_name)
        val description = itemView?.findViewById<TextView>(R.id.descripsion)
        val username = itemView?.findViewById<TextView>(R.id.name_user)
        val imageUser = itemView?.findViewById<ImageView>(R.id.profile_user)
        val time = itemView?.findViewById<TextView>(R.id.time)
        val severity = itemView?.findViewById<Button>(R.id.severity)


        fun onBind(position: Int) {
            itemView.setOnClickListener {
                listener?.onClickItem(issues[position], position)
            }
            if (issues[position].user?.image?.url != null) {
                Picasso.with(itemView.getContext()).load(issues[position].user?.image?.url)
                    .into(itemView.profile_user)
            } else {
                imageUser?.setImageResource(R.drawable.ic_profile)
            }
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val past = format.parse(issues[position].updated_at)
            val now = Date()
            val convert = SimpleDateFormat("MMM dd, yyyy", Locale.US)

            val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
            val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
            val days = TimeUnit.MILLISECONDS.toDays(now.time - past.time)

            when {
                seconds < 60 -> time?.text = (seconds.toString() + ""+ "sec ago")
                minutes < 60 -> time?.text = (minutes.toString() + ""+ "min ago")
                hours   < 24 -> time?.text = (hours.toString()   + ""+"hrs ago")
//                hours > 24 && < 48 -> time?.text = (days.toString()  + "day ago")
                else -> time?.text = convert.format(past)
            }

            bugname?.text = issues[position].title
            description?.text = issues[position].description
            username?.text = issues[position].user?.name
            severity?.text = issues[position].severity?.name
            severity?.setBackgroundColor(Color.parseColor(issues[position].severity?.color))


        }
    }

    inner class LoadingViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val loading = itemView?.findViewById<LinearLayout>(R.id.loading)

        fun onBind() {}
    }


    interface Listener {
        fun onClickItem(issues: Issues, position: Int)
    }

    fun setData(issues: MutableList<Issues>) {
        this.issues.clear()
        this.issues.addAll(issues)
        notifyDataSetChanged()
    }

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
        issues.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addData(issues: MutableList<Issues>) {
        this.issues.addAll(issues)
        notifyItemRangeInserted(itemCount, issues.size)
    }

    fun setLoading() {
        issues.add(
        Issues(null, null, null, null, null, null, null, null,
                null, null))
        notifyItemInserted(issues.size - 1)
    }

    fun hideLoading() {
        issues.removeAt(issues.size - 1)
        notifyItemRemoved(issues.size)
    }

}

