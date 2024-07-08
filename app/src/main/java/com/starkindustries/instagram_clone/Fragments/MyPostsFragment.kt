package com.starkindustries.instagram_clone.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.starkindustries.instagram_clone.Adapter.MyPostsAdapter
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.Model.Posts
import com.starkindustries.instagram_clone.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyPostsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPostsFragment : Fragment() {
    lateinit var recyclerView:RecyclerView
    lateinit var auth:FirebaseAuth
    lateinit var user:FirebaseUser
    lateinit var firebaseFirestore:FirebaseFirestore
    lateinit var docRefrence:CollectionReference
    lateinit var postsList:ArrayList<Posts>
    lateinit var noPosts:AppCompatTextView
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view :View= inflater.inflate(R.layout.fragment_my_posts, container, false)
        recyclerView=view.findViewById(R.id.postsRecyclerView)
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        firebaseFirestore=FirebaseFirestore.getInstance()
        docRefrence=firebaseFirestore.collection(user.uid!!+Keys.POSTS)
        postsList= ArrayList<Posts>()
        noPosts=view.findViewById(R.id.noPosts)
        docRefrence.get().addOnSuccessListener {
                for(posts in it.documents)
                {
                    val post: Posts? = posts.toObject<Posts>()
                    postsList.add(post!!)
                }
            if(postsList.size!=0)
            {
                noPosts.visibility=View.GONE
                val adapter=MyPostsAdapter(requireContext(),postsList)
                recyclerView.layoutManager=StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
                recyclerView.adapter=adapter
            }
            else
            {
             noPosts.visibility=View.VISIBLE
             recyclerView.visibility=View.GONE
            }
        }.addOnFailureListener {
            Log.d("ErrorListner"," "+it.message.toString().trim())
        }
        return view
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyPostsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyPostsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}