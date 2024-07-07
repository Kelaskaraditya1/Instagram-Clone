package com.starkindustries.instagram_clone.Fragments

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.R
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UpdateProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpdateProfileFragment : Fragment() {
    lateinit var updateProfileImageView:CircleImageView
    lateinit var updateprofileName:TextInputEditText
    lateinit var updateProfileEmail:TextInputEditText
    lateinit var updateProfilePhoneNo:TextInputEditText
    lateinit var updateProfileUsername:TextInputEditText
    lateinit var updateButton:AppCompatButton
    lateinit var auth:FirebaseAuth
    lateinit var user: FirebaseUser
    lateinit var firebaseFireStore:FirebaseFirestore
    lateinit var docRefrence:DocumentReference
    lateinit var storageRefrence:StorageReference
    lateinit var childRefrence:StorageReference
    lateinit var docRefernce:DocumentReference
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
        val view =  inflater.inflate(R.layout.fragment_update_profile, container, false)
        updateProfileEmail=view.findViewById(R.id.updateProfileEmail)
        updateProfilePhoneNo=view.findViewById(R.id.updateProfilePhoneNo)
        updateProfileUsername=view.findViewById(R.id.updateProfileUsername)
        updateprofileName=view.findViewById(R.id.updateProfileName)
        updateProfileImageView=view.findViewById(R.id.updateProfileImageView)
        updateButton=view.findViewById(R.id.updateButton)
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        firebaseFireStore=FirebaseFirestore.getInstance()
        docRefrence=firebaseFireStore.collection(Keys.COLLECTION_NAME).document(user.uid)
        updateProfileImageView.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,Keys.GALLERY_REQ_CODE)
        }
        updateButton.setOnClickListener()
        {
            if(TextUtils.isEmpty(updateprofileName.text.toString().trim()))
            {
                updateprofileName.setError("Field is empty!!")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(updateProfileEmail.text.toString().trim()))
            {
                updateProfileEmail.setError("Field is empty!!")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(updateProfilePhoneNo.text.toString().trim()))
            {
                updateProfilePhoneNo.setError("Field is empty!!")
                return@setOnClickListener
            }
            else if(updateProfilePhoneNo.text.toString().trim().length<10)
            {
                updateProfilePhoneNo.setError("Phone no should be atleast of 10 digits!!")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(updateProfileUsername.text.toString().trim()))
            {
                updateProfileUsername.setError("Field is empty!!")
                return@setOnClickListener
            }
            user=auth?.currentUser!!
            firebaseFireStore=FirebaseFirestore.getInstance()
            docRefrence=firebaseFireStore.collection(Keys.COLLECTION_NAME).document(user.uid)
            val map = mutableMapOf<String,Any>()
            map.put(Keys.NAME,updateprofileName.text.toString().trim())
            map.put(Keys.EMAIL,updateProfileEmail.text.toString().trim())
            map.put(Keys.PHONE_NO,updateProfilePhoneNo.text.toString().trim())
            map.put(Keys.USERNAME,updateProfileUsername.text.toString().trim())
            docRefrence.update(map).addOnSuccessListener {
                val update = UserProfileChangeRequest.Builder()
                    .setDisplayName(updateProfileUsername.text.toString().trim())
                    .build()
                user.updateProfile(update).addOnSuccessListener {
                    val manager = parentFragmentManager
                    val transaction:FragmentTransaction = manager.beginTransaction()
                    transaction.replace(R.id.frameLayout,ProfileFragment())
                    transaction.commit()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to update Profile", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to update Profile", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        firebaseFireStore=FirebaseFirestore.getInstance()
        docRefrence=firebaseFireStore.collection(Keys.COLLECTION_NAME).document(user.uid)
        docRefrence.addSnapshotListener(object:EventListener<DocumentSnapshot>{
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                Picasso.get().load(value?.getString(Keys.DOWNLOAD_URL)).into(updateProfileImageView)
                updateprofileName.setText(value?.getString(Keys.NAME))
                updateProfileEmail.setText(value?.getString(Keys.EMAIL))
                updateProfilePhoneNo.setText(value?.getString(Keys.PHONE_NO))
                updateProfileUsername.setText(value?.getString(Keys.USERNAME))
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
         * @return A new instance of fragment UpdateProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UpdateProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Keys.GALLERY_REQ_CODE)
        {
            storageRefrence=FirebaseStorage.getInstance().reference
            childRefrence=storageRefrence.child(updateprofileName.text.toString().trim()+"/"+user.uid+"/"+Keys.IMAGES+"/"+Keys.USER_PROFILE_IMAGE)
            childRefrence.putFile(data?.data!!).
            addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {
                    uri->
                    firebaseFireStore=FirebaseFirestore.getInstance()
                    docRefrence=firebaseFireStore.collection(Keys.COLLECTION_NAME).document(user.uid)
                    val map = mutableMapOf<String,Any>()
                    map.put(Keys.PHOTO_URI,data.data.toString().trim())
                    map.put(Keys.DOWNLOAD_URL,uri.toString().trim())
                    docRefrence.update(map).addOnSuccessListener {
                        Picasso.get().load(uri.toString()).into(updateProfileImageView)
                        user=auth?.currentUser!!
                        val update =UserProfileChangeRequest.Builder()
                            .setPhotoUri(data?.data!!)
                            .build()
                        user.updateProfile(update).addOnSuccessListener {
                            Toast.makeText(requireContext(), "Profile Pic Updated successfully", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Log.d("errorListner"," "+it.message.toString().trim())
                        }
                    }.addOnFailureListener {
                        Log.d("errorListner"," "+it.message.toString().trim())
                    }
                }.addOnFailureListener {
                    Log.d("errorListner"," "+it.message.toString().trim())
                }

            }.addOnFailureListener{
                Log.d("errorListner"," "+it.message.toString().trim())
            }

        }
    }
}