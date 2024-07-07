package com.starkindustries.instagram_clone.Fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import com.starkindustries.instagram_clone.Activity.LoginActivity
import com.starkindustries.instagram_clone.Activity.RegisterActivity
import com.starkindustries.instagram_clone.Adapter.ProfileViewPagerAdapter
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.R
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    lateinit var profileImage:CircleImageView
    lateinit var updateProfileButton:CircleImageView
    lateinit var profileUsername:AppCompatTextView
    lateinit var profileBio:AppCompatTextView
    lateinit var auth:FirebaseAuth
    lateinit var user:FirebaseUser
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var viewPager:ViewPager
    lateinit var tablayout:TabLayout
    lateinit var logoutButton:AppCompatButton
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
        val view:View= inflater.inflate(R.layout.fragment_profile, container, false)
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        updateProfileButton=view.findViewById(R.id.updateProfileButton)
        logoutButton=view.findViewById(R.id.logoutButton)

        updateProfileButton.setOnClickListener()
        {
            val manager = parentFragmentManager
            val transaction:FragmentTransaction = manager.beginTransaction()
            transaction.replace(R.id.frameLayout,UpdateProfileFragment())
            transaction.commit()
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.DEFAULT_GOOGLE_SIGNIN_ID))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(requireContext(),gso)
        logoutButton.setOnClickListener()
        {
            googleSignInClient.signOut().addOnCompleteListener()
                {
                    if(it.isSuccessful)
                    {
                        val intent = Intent(context, LoginActivity::class.java)
                        sharedPreferences=context?.getSharedPreferences(Keys.SHARED_PREFRENCES_NAME, Context.MODE_PRIVATE)!!
                        editor=sharedPreferences.edit()
                        editor.putBoolean(Keys.LOGIN_STATUS,false)
                        editor.apply()
                        startActivity(intent)
                        activity?.finish()
                    }
                }.addOnFailureListener()
                {
                    Log.d("ErrorListner"," "+it.message.toString().trim())
                }
        }
        viewPager=view.findViewById(R.id.viewPager)
        tablayout=view.findViewById(R.id.tablayout)
        viewPager.adapter=ProfileViewPagerAdapter(parentFragmentManager)
        tablayout.setupWithViewPager(viewPager)
        return view
    }
    override fun onStart() {
        super.onStart()
        profileImage=view?.findViewById(R.id.profileImage)!!
        profileUsername=view?.findViewById(R.id.profileUsername)!!
        profileBio=view?.findViewById(R.id.pprofileBio)!!
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        profileUsername.setText(user.displayName.toString().trim())
        Picasso.get().load(user.photoUrl).into(profileImage)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}