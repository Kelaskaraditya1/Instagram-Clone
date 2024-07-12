package com.starkindustries.instagram_clone.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.starkindustries.instagram_clone.Adapter.ReelsAdapter
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.Model.ReelsModel
import com.starkindustries.instagram_clone.R
import com.google.firebase.firestore.toObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReelsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReelsFragment : Fragment() {
    lateinit var viewPager:ViewPager2
    lateinit var auth:FirebaseAuth
    lateinit var user:FirebaseUser
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var docRefrence:CollectionReference
    lateinit var reelsList:ArrayList<ReelsModel>
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
        val view:View =  inflater.inflate(R.layout.fragment_reels, container, false)
        viewPager=view.findViewById(R.id.reelsViewPager)
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        firebaseFirestore=FirebaseFirestore.getInstance()
        reelsList= ArrayList<ReelsModel>()
        docRefrence=firebaseFirestore.collection(user.uid+Keys.CUSTOM_REELS)
        docRefrence.get().addOnSuccessListener {
            for(reel in it.documents)
            {
                val reels: ReelsModel? = reel.toObject<ReelsModel>()
                if (reels != null) {
                    reelsList.add(reels)
                }
            }
            Log.d("ValueListner"," "+reelsList.get(0).downlloadUrl  )
            viewPager.adapter=ReelsAdapter(requireContext(),reelsList)
        }.addOnFailureListener {
            Log.d("ErrorListner"," "+it.message.toString().trim())
        }
//        user.let {
//            dbrefrence.child(user.displayName.toString().trim()).child(user.uid).child(Keys.REELS)
//            dbrefrence.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val reelsLists :ArrayList<ReelsModel> = ArrayList<ReelsModel>()
//                    for(reelitem in snapshot.children)
//                    {
//                        val reel=reelitem.getValue(ReelsModel::class.java)
//                        reel.let {
//                            reelsLists.add(it!!)
//                            Log.d("ValueListner"," "+it.reelName)
//                        }
//                    }
//                }
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
//        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReelsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReelsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}