package com.starkindustries.instagram_clone.Adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.Model.UserProfile
import com.starkindustries.instagram_clone.R
import de.hdodenhof.circleimageview.CircleImageView
open class SearchAdapter(var context_:Context,var searchProfileList_:ArrayList<UserProfile>):RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    lateinit var context:Context
    lateinit var searchProfileList:ArrayList<UserProfile>
    var status:Boolean=false
    init {
        this.context=context_
        this.searchProfileList=searchProfileList_
    }
    open inner class ViewHolder(var view:View):RecyclerView.ViewHolder(view){
        lateinit var searchProfileImage:CircleImageView
        lateinit var searchProfileName:AppCompatTextView
        lateinit var followButton:AppCompatButton
        init {
            searchProfileImage=view.findViewById(R.id.searchProfileImage)
            searchProfileName=view.findViewById(R.id.searchProfileName)
            followButton=view.findViewById(R.id.followButton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_row_design,parent,false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return searchProfileList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     Picasso.get().load(searchProfileList.get(position).downloadUrl).into(holder.searchProfileImage)
        holder.searchProfileName.setText(searchProfileList.get(position).name)
        Firebase.firestore.collection(Firebase.auth.currentUser?.uid+Keys.FOLLOW).whereEqualTo("email",searchProfileList.get(position).email).get().addOnSuccessListener {
            if(it.documents.size==0)
            {
                status=false
            }else
            {
                holder.followButton.setText("Un Follow")
                status=true
            }
        }.addOnFailureListener {
            Log.d("ErrorListner"," "+it.message.toString().trim())
        }
        holder.followButton.setOnClickListener()
        {
            if(status)
            {
                Firebase.firestore.collection(Firebase.auth.currentUser?.uid+Keys.FOLLOW).whereEqualTo("email",searchProfileList.get(position).email).get().addOnSuccessListener {
                  Firebase.firestore.collection(Firebase.auth.currentUser?.uid+Keys.FOLLOW).document(it.documents.get(0).id).delete()
                    holder.followButton.setText("Follow")
                    status=false
                }.addOnFailureListener {
                    Log.d("ErrorListner"," "+it.message.toString().trim())
                }
            }else
            {
                Firebase.firestore.collection(Firebase.auth.currentUser?.uid+Keys.FOLLOW).document().set(searchProfileList.get(position))
                holder.followButton.setText("Un Follow")
                status=true
            }
        }
    }
}