package com.example.dwi.jatmico.view.home



import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.models.Project
import com.example.dwi.jatmico.view.isues.IssuesActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_project.view.*

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.ProjectViewHolder>() {
    private var projects: MutableList<Project> = ArrayList()

    override fun getItemCount(): Int {
        return projects.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.itemView.txt_project_name.text = projects[position].name
        holder.itemView.report.text = "${projects[position].total_issue} Report"
        holder.itemView.btn_QA.text = projects[position].tag_list.toString()



            Picasso.with(holder.itemView.getContext()).load(projects[position].image?.url)
                .into(holder.itemView.project_logo)


        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, IssuesActivity::class.java)
            intent.putExtra("project_id",projects[position].id )

            holder.itemView.context.startActivity(intent)
        }
    }

    class ProjectViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    }


    fun setData(projects: MutableList<Project>) {
        this.projects = projects
            notifyDataSetChanged()
     }
    }

