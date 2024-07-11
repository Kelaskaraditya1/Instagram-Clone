package com.starkindustries.instagram_clone.Fragments
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso
import com.starkindustries.instagram_clone.Adapter.FollowListAdapter
import com.starkindustries.instagram_clone.Adapter.PostsListAdapter
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.Model.Posts
import com.starkindustries.instagram_clone.Model.UserProfile
import com.starkindustries.instagram_clone.R
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    lateinit var homeToolbar:MaterialToolbar
    lateinit var postsRecyclerView:RecyclerView
    lateinit var auth:FirebaseAuth
    lateinit var user: FirebaseUser
    lateinit var registerProfileImageViewer:CircleImageView
    lateinit var followRecyclerView:RecyclerView
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            setHasOptionsMenu(true)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View =  inflater.inflate(R.layout.fragment_home, container, false)
        homeToolbar=view.findViewById(R.id.homeToolbar)
        (activity as AppCompatActivity).setSupportActionBar(homeToolbar)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Instagram")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        postsRecyclerView=view.findViewById(R.id.postsRecyclerView)
        followRecyclerView=view.findViewById(R.id.followRecyclerView)
        registerProfileImageViewer=view.findViewById(R.id.registerProfileImageViewer)
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        Firebase.firestore.collection(user.uid+Keys.POSTS).get().addOnSuccessListener {
            val postsList:ArrayList<Posts> = ArrayList<Posts>()
            for(posts in it.documents)
            {
                val post = posts.toObject(Posts::class.java)
                postsList.add(post!!)
            }
            postsRecyclerView.layoutManager=LinearLayoutManager(context)
            postsRecyclerView.adapter=PostsListAdapter(requireContext(),postsList)
        }.addOnFailureListener {
            Log.d("ErrorListner"," "+it.message.toString().trim())
        }
                Firebase.firestore.collection(user.uid+Keys.FOLLOW).get().addOnSuccessListener {
            val List:ArrayList<UserProfile> = ArrayList<UserProfile>()
            for(posts in it.documents)
            {
                val post = posts.toObject<UserProfile>()
                Log.d("ValueListner","name:"+post?.name.toString().trim())
                List.add(post!!)
            }
            followRecyclerView.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            followRecyclerView.adapter=FollowListAdapter(requireContext(),List)
        }.addOnFailureListener {
            Log.d("ErrorListner"," "+it.message.toString().trim())
        }
        return view
    }
    override fun onStart() {
        super.onStart()
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        Firebase.firestore.collection(Keys.COLLECTION_NAME).document(user.uid).addSnapshotListener(object :EventListener<DocumentSnapshot>{
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                registerProfileImageViewer= view?.findViewById(R.id.registerProfileImageViewer)!!
                Picasso.get().load(user.photoUrl).into(registerProfileImageViewer)
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}