package com.starkindustries.instagram_clone.Adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.starkindustries.instagram_clone.Model.Posts
import com.starkindustries.instagram_clone.R
open class MyPostsAdapter(var context_:Context,var postList_:ArrayList<Posts>,var itemClickListner:OnItemClickListner):RecyclerView.Adapter<MyPostsAdapter.ViewHolder>()
{
    interface OnItemClickListner
    {
        fun onRowLongClick(position:Int)
    }
    lateinit var context: Context
    lateinit var postsList:ArrayList<Posts>
    init {
        this.context=context_
        this.postsList=postList_
    }
    open inner class ViewHolder(var view:View):RecyclerView.ViewHolder(view)
    {
        lateinit var postImageView:AppCompatImageView
        init {
            postImageView=view.findViewById(R.id.postImageView)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.post_viewer_container,parent,false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }
    override fun getItemCount(): Int {
        return postsList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(postsList.get(position).postDownloadUrl).into(holder.postImageView)
        holder.postImageView.setOnLongClickListener(object :View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                itemClickListner.onRowLongClick(position)
                return true
            }

        })

    }
}