package com.example.dwi.jatmico.view.my_submission

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.models.Isues
import com.example.dwi.jatmico.data.models.Project
import com.example.dwi.jatmico.data.models.Severitys
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_isues.view.*

class MySubmissionAdapter  : RecyclerView.Adapter<MySubmissionAdapter.SubViewHolder>() {
    private var submission: MutableList<Isues> = ArrayList()
    private var project: MutableList<Project> = ArrayList()
    private var severity: MutableList<Severitys> = ArrayList()
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_isues, parent, false)
        return SubViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        return submission.size
    }

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        holder.itemView.bug_name.text = submission [position].title
        holder.itemView.descripsion.text  = submission [position].description
        holder.itemView.name_user.text = submission [position].user.name
        holder.itemView.severity.text = submission [position].severity.name
        holder.itemView.time.text = submission[position].updated_at

        Picasso.with(holder.itemView.getContext()).load(submission[position].user.image.url)
            .into(holder.itemView.profile_user)

// ---Show detail isues--

        holder.itemView.setOnClickListener {
            listener?.onClickItem(submission[position], position)
        }
    }

//        var sf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//        var input = "2018-09-13T10:22:50.000+07:00"
//        var date = sf.parse(input)

    class SubViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    }
    interface Listener {
        fun onClickItem(isues: Isues, position: Int)
    }

//show isues
    fun setData(submission: MutableList<Isues>) {
        this.submission = submission
        notifyDataSetChanged()
    }

//show project
    fun setProject(project: MutableList<Project>) {
        this.project = project
        notifyDataSetChanged()
    }

//show severity
    fun setSeverity(severities: MutableList<Severitys>) {
        this.severity = severities
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        submission.removeAt(position)
        notifyItemRemoved(position)
    }




}