package com.starkindustries.instagram_clone.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.starkindustries.instagram_clone.Adapter.SearchAdapter
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.Model.UserProfile
import com.starkindustries.instagram_clone.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    lateinit var auth:FirebaseAuth
    lateinit var user:FirebaseUser
    lateinit var recyclerView:RecyclerView
    lateinit var searchView:TextInputEditText

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
        val view:View =inflater.inflate(R.layout.fragment_search, container, false)
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        recyclerView=view.findViewById(R.id.searchRecyclerView)
        searchView=view.findViewById(R.id.searchView)
        Firebase.firestore.collection(Keys.COLLECTION_NAME).get().addOnSuccessListener {
            val searchList:ArrayList<UserProfile> = ArrayList<UserProfile>()
            for(userItem in it.documents)
            {
                if(userItem.id!=user.uid)
                {
                    val user: UserProfile? = userItem.toObject<UserProfile>()
                    searchList.add(user!!)
                }
            }
            recyclerView.layoutManager=LinearLayoutManager(context)
            recyclerView.adapter=SearchAdapter(requireContext(),searchList)
        }.addOnFailureListener {
            Log.d("ErrorListner"," "+it.message.toString().trim())
        }
        searchView.setOnClickListener()
        {
            Firebase.firestore.collection(Keys.COLLECTION_NAME).whereEqualTo("name",searchView.text.toString().trim()).get().addOnSuccessListener {
                val searchList:ArrayList<UserProfile> = ArrayList<UserProfile>()
                for(userItem in it.documents)
                {
                    if(userItem.id!=user.uid)
                    {
                        val user: UserProfile? = userItem.toObject<UserProfile>()
                        searchList.add(user!!)
                    }
                }
                recyclerView.layoutManager=LinearLayoutManager(context)
                recyclerView.adapter=SearchAdapter(requireContext(),searchList)
            }.addOnFailureListener {
                Log.d("ErrorListner"," "+it.message.toString().trim())
            }
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
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}