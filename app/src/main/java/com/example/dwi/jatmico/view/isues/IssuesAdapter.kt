package com.example.dwi.jatmico.view.isues

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.models.Isues
import com.example.dwi.jatmico.data.models.Severity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_isues.view.*

class IssuesAdapter : RecyclerView.Adapter<IssuesAdapter.IsuesViewHolder>() {
    private var isues: MutableList<Isues> = ArrayList()
    private var severity: MutableList<Severity> = ArrayList()
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IsuesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_isues, parent, false)
        return IsuesViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return isues.size
    }

    override fun onBindViewHolder(holder: IsuesViewHolder, position: Int) {
        holder.itemView.bug_name.text = isues[position].title
        holder.itemView.descripsion.text = isues[position].description
        holder.itemView.name_user.text = isues[position].user.name
        holder.itemView.severity.text = isues[position].severity.name
        holder.itemView.time.text = isues[position].updated_at

        holder.itemView.severity.setBackgroundColor(Color.parseColor(isues[position].severity.color))

        Picasso.with(holder.itemView.getContext()).load(isues[position].user.image.url)
            .into(holder.itemView.profile_user)

// ---Show detail isues--

        holder.itemView.setOnClickListener {
            listener?.onClickItem(isues[position], position)
        }
    }

//        var sf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//        var input = "2018-09-13T10:22:50.000+07:00"
//        var date = sf.parse(input)

    class IsuesViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    }

    interface Listener {
        fun onClickItem(isues: Isues, position: Int)
    }


    fun setData(isues: MutableList<Isues>) {
        this.isues = isues
        notifyDataSetChanged()
    }
    //show severity
    fun setSeverity(severities: MutableList<Severity>) {
        this.severity = severities
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        isues.removeAt(position)
        notifyItemRemoved(position)
    }
//
//    fun addItem(position: Int, issue: Isues) {
//        isues.add(issue)
//        notifyItemInserted(position)
//    }

}

