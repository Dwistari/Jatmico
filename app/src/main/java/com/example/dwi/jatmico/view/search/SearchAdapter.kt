package com.example.dwi.jatmico.view.search

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.models.Issues
import com.example.dwi.jatmico.view.detail_isues.DetailIssueActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_isues.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private var search: MutableList<Issues> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SearchAdapter.SearchViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_isues, parent, false)
        return SearchViewHolder( itemView)
    }

    override fun getItemCount(): Int {
       return search.size
    }

    override fun onBindViewHolder(holder: SearchAdapter.SearchViewHolder, position: Int) {

            holder.itemView.bug_name.text = search [position].title
            holder.itemView.descripsion.text  = search [position].description
            holder.itemView.name_user.text = search [position].user?.name
            holder.itemView.severity.text = search [position].severity?.name
            holder.itemView.time.text = search[position].updated_at


        Picasso.with(holder.itemView.getContext()).load(search[position].user?.image?.url)
            .into(holder.itemView.profile_user)


        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailIssueActivity::class.java)
            intent.putExtra("issue_id",search[position].id )
            holder.itemView.context.startActivity(intent)
        }
    }

    class SearchViewHolder (itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    }


    fun setData(search: MutableList<Issues>) {
        this.search = search
        notifyDataSetChanged()
    }
}