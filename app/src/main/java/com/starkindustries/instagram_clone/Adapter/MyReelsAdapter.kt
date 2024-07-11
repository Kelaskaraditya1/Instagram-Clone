package com.starkindustries.instagram_clone.Adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.starkindustries.instagram_clone.Fragments.MyReelsFragment
import com.starkindustries.instagram_clone.Model.Reels
import com.starkindustries.instagram_clone.R
class MyReelsAdapter (var context_:Context,var videoLists_:ArrayList<Reels>,var itemClickListner:OnItemClickListner):RecyclerView.Adapter<MyReelsAdapter.ViewHolder>()
{
    interface OnItemClickListner
    {
        fun onRowLongClick(position: Int)
    }
    lateinit var context: Context
    lateinit var videosList:ArrayList<Reels>
    init {
        this.context=context_
        this.videosList=videoLists_
    }
    open inner class ViewHolder(var view:View):RecyclerView.ViewHolder(view)
    {
     lateinit var reelsImageView: AppCompatImageView
     init {
         reelsImageView=view.findViewById(R.id.postImageView)
     }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.post_viewer_container,parent,false)
        val viewHolder=ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return videosList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position:Int) {
        Glide.with(context).load(videosList.get(position).reelDownloadUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.reelsImageView)
        holder.reelsImageView.setOnLongClickListener(object :View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                itemClickListner.onRowLongClick(position)
                return true
            }

        })
    }
}