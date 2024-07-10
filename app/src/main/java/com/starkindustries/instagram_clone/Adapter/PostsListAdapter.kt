package com.starkindustries.instagram_clone.Adapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.Model.Posts
import com.starkindustries.instagram_clone.R
import de.hdodenhof.circleimageview.CircleImageView

open class PostsListAdapter(var context_:Context,var postsList_:ArrayList<Posts>) :RecyclerView.Adapter<PostsListAdapter.ViewHolder>()
{
    lateinit var context:Context
    lateinit var postList: ArrayList<Posts>
     var status:Boolean=false
    var bookmarkStatus=false
    init {
        this.context=context_
        this.postList=postsList_
    }
    open inner class ViewHolder(var view:View):RecyclerView.ViewHolder(view)
    {
        lateinit var postProfileImage:CircleImageView
        lateinit var postProfileName:AppCompatTextView
        lateinit var postImage:AppCompatImageView
        lateinit var postCaption:AppCompatTextView
        lateinit var likeButton:AppCompatImageView
        lateinit var shareButton:AppCompatImageView
        lateinit var bookmarkButton:AppCompatImageView
        lateinit var postUploadtiming:AppCompatTextView
        init {
            postProfileImage=view.findViewById(R.id.postProfileImage)
            postProfileName=view.findViewById(R.id.postProfileName)
            postImage=view.findViewById(R.id.postImage)
            postCaption=view.findViewById(R.id.postCaption)
            likeButton=view.findViewById(R.id.likeButton)
            shareButton=view.findViewById(R.id.shareButton)
            bookmarkButton=view.findViewById(R.id.bookmarkButton)
            postUploadtiming=view.findViewById(R.id.uploadTimeing)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_post_design,parent,false)
        val viewHolder=ViewHolder(view)
        return viewHolder
    }
    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Firebase.firestore.collection(Keys.COLLECTION_NAME).document(postList.get(position).uid).addSnapshotListener(object :EventListener<DocumentSnapshot>{
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                Picasso.get().load(value?.getString(Keys.DOWNLOAD_URL)).into(holder.postProfileImage)
                holder.postProfileName.setText(value?.getString(Keys.USERNAME))
            }
        })
        Picasso.get().load(postList.get(position).postDownloadUrl).into(holder.postImage)
        holder.postCaption.setText(postList.get(position).caption)
        holder.postUploadtiming.setText(TimeAgo.using(postList.get(position).time.toLong()))
        holder.likeButton.setOnClickListener()
        {
            if(status)
            {
                holder.likeButton.setImageResource(R.drawable.heart_plain)
                status=false
            }
            else
            {
                holder.likeButton.setImageResource(R.drawable.heart_red)
                status=true
            }
        }
        holder.bookmarkButton.setOnClickListener()
        {
            if(bookmarkStatus)
            {
                holder.bookmarkButton.setImageResource(R.drawable.bookmark_plain)
                Toast.makeText(context, "Post Removed from collection.", Toast.LENGTH_SHORT).show()
                bookmarkStatus=false
            }
            else{
                holder.bookmarkButton.setImageResource(R.drawable.bookmark_done)
                Toast.makeText(context, "Post added to collection.", Toast.LENGTH_SHORT).show()
                bookmarkStatus=true
            }
        }
        holder.shareButton.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_TEXT,postList.get(position).postDownloadUrl)
            context.startActivity(Intent.createChooser(intent,"Share via!!"))
        }
    }
}