package com.starkindustries.instagram_clone.Adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.starkindustries.instagram_clone.Model.UserProfile
import com.starkindustries.instagram_clone.R
import de.hdodenhof.circleimageview.CircleImageView

open class FollowListAdapter(var context_:Context,var followList_:ArrayList<UserProfile>) :RecyclerView.Adapter<FollowListAdapter.Viewholder>()
{
    lateinit var context:Context
    lateinit var followList:ArrayList<UserProfile>
    init {
        this.context=context_
        this.followList=followList_
    }
    open inner class Viewholder(var view:View):RecyclerView.ViewHolder(view)
    {
        lateinit var followProfileImage:CircleImageView
        lateinit var followName:AppCompatTextView
        init {
            followProfileImage=view.findViewById(R.id.followImageView)
            followName=view.findViewById(R.id.followName)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view:View =LayoutInflater.from(context).inflate(R.layout.follow_circle_design,parent,false)
        val viewholder:Viewholder = Viewholder(view)
        return viewholder
    }

    override fun getItemCount(): Int {
        return followList.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.followName.setText(followList.get(position).name)
        Picasso.get().load(followList.get(position).downloadUrl).into(holder.followProfileImage)
    }
}