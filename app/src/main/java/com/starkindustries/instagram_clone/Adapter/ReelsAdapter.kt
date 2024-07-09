package com.starkindustries.instagram_clone.Adapter
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.starkindustries.instagram_clone.Model.ReelsModel
import com.starkindustries.instagram_clone.R
import de.hdodenhof.circleimageview.CircleImageView
open class ReelsAdapter (var context_:Context,var reelsList_:ArrayList<ReelsModel>):RecyclerView.Adapter<ReelsAdapter.ViewHolder>()
{   lateinit var context:Context
    lateinit var reelsList:ArrayList<ReelsModel>
    init {
        this.context=context_
        this.reelsList=reelsList_
    }
    open inner class ViewHolder(var view:View):RecyclerView.ViewHolder(view)
    {
        lateinit var videoView:VideoView
        lateinit var reelProfileImage:CircleImageView
        lateinit var reelCaption:AppCompatTextView
        lateinit var progressBar:ProgressBar
        init {
            videoView=view.findViewById(R.id.videoView)
            reelProfileImage=view.findViewById(R.id.reelProfileImage)
            reelCaption=view.findViewById(R.id.reelCaption)
            progressBar=view.findViewById(R.id.progressBar)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.reel_design,parent,false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return reelsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(reelsList.get(position).reelProfileImage).into(holder.reelProfileImage)
        holder.reelCaption.setText(reelsList.get(position).reelCaption)
        holder.videoView.setVideoPath(reelsList.get(position).downlloadUrl)
        holder.videoView.setOnPreparedListener()
        {
            holder.progressBar.visibility=View.GONE
            holder.videoView.start()
        }
    }
}