package com.starkindustries.instagram_clone.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.Model.ReelsModel
import com.starkindustries.instagram_clone.Model.UserPost
import com.starkindustries.instagram_clone.R
import com.starkindustries.instagram_clone.databinding.ActivityReelsUploadBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

class ReelsUploadActivity : AppCompatActivity() {
    lateinit var binding:ActivityReelsUploadBinding
    lateinit var profileImageUri:Uri
    lateinit var postImageUri:Uri
    lateinit var dbRefrence:DatabaseReference
    lateinit var auth:FirebaseAuth
    lateinit var user:FirebaseUser
    lateinit var storageReference: StorageReference
    lateinit var childRefrence:StorageReference
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var docRefrence:DocumentReference
    lateinit var sharedPrefrences:SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    var postUrl:String=""
    var profileUrl:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reels_upload)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_reels_upload)
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        sharedPrefrences=getSharedPreferences(Keys.POSTS_SHARED_PREFRENCES_NAME, MODE_PRIVATE)
        editor=sharedPrefrences.edit()
        binding.ProfileImageButton.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,Keys.PROFILE_IMAGE_GALLERY_REQ)
        }
        binding.videoButton.setOnClickListener()
        {val intent = Intent(Intent.ACTION_PICK)
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,Keys.POST_IMAGE_GALLERY_REQ)
        }
        binding.uploadProfileUrl.setOnClickListener()
        {
            storageReference = FirebaseStorage.getInstance().reference
            childRefrence=storageReference.child(user.displayName.toString().trim()+"/"+user.uid+"/"+Keys.CUSTOM_POSTS+"/"+Keys.PROFILE_IMAGE_FOLDER+"/"+binding.reelName.text.toString().trim())
            childRefrence.putFile(profileImageUri).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {
                    profileUrl=it.toString().trim()
                    editor.putString(Keys.PROFILE_URL,profileUrl)
                    editor.commit()
                }.addOnFailureListener {
                    Log.d("ErrorListner"," "+it.message.toString().trim())
                }
            }.addOnFailureListener {
                Log.d("ErrorListner"," "+it.message.toString().trim())
            }
        }
        binding.uploadPostUrl.setOnClickListener()
        {
            storageReference = FirebaseStorage.getInstance().reference
            childRefrence=storageReference.child(user.displayName.toString().trim()+"/"+user.uid+"/"+Keys.CUSTOM_POSTS+"/"+Keys.POSTS_FOLDER+"/"+binding.reelTitle.text.toString().trim())
            childRefrence.putFile(postImageUri).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {
                    postUrl=it.toString().trim()
                    editor.putString(Keys.POST_URL,postUrl)
                    editor.commit()
                }.addOnFailureListener {
                    Log.d("ErrorListner"," "+it.message.toString().trim())
                }
            }.addOnFailureListener {
                Log.d("ErrorListner"," "+it.message.toString().trim())
            }
        }
        binding.uploadButton.setOnClickListener()
        {
//            storageReference=FirebaseStorage.getInstance().reference
//            childRefrence=storageReference.child(user.displayName.toString().trim()+"/"+user.uid+"/"+Keys.REELS+"/"+profileImageUri.toString().trim())
//            childRefrence.putFile(profileImageUri).addOnSuccessListener {
//                it.storage.downloadUrl.addOnSuccessListener {
//                    imaegurl=it.toString()
//                }.addOnFailureListener {
//                    Log.d("ErrorListner"," "+it.message.toString().trim())
//                }
//            }.addOnFailureListener {
//                Log.d("ErrorListner"," "+it.message.toString().trim())
//            }
//            storageReference=FirebaseStorage.getInstance().reference
//            childRefrence=storageReference.child(user.displayName.toString().trim()+"/"+user.uid+"/"+Keys.REELS+"/"+binding.reelName.text.toString().trim())
//            childRefrence.putFile(reeluri).addOnSuccessListener {
//                it.storage.downloadUrl.addOnSuccessListener {
//                    firebaseFirestore=FirebaseFirestore.getInstance()
//                    docRefrence=firebaseFirestore.collection(Keys.CUSTOM_REELS).document(user.uid)
//                    val reel = ReelsModel( binding.reelName.text.toString().trim(),imaegurl, it.toString(), binding.captionEdittext.text.toString().trim())
//                    docRefrence.set(reel!!).addOnSuccessListener {
//                        docRefrence=firebaseFirestore.collection(user.uid!!+Keys.CUSTOM_REELS).document()
//                        docRefrence.set(reel).addOnSuccessListener {
//                            val view = this.currentFocus
//                            if(view!=null)
//                            {
//                                val manager:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                                manager.hideSoftInputFromWindow(view.windowToken,0)
//                            }
//
//                            Toast.makeText(applicationContext, "uploaded successfully!!", Toast.LENGTH_SHORT).show()
//                        }.addOnFailureListener {
//                            Log.d("ErrorListner"," "+it.message.toString().trim())
//                        }
//                    }.addOnFailureListener {
//                        Log.d("ErrorListner"," "+it.message.toString().trim())
//                    }
//                }.addOnFailureListener {
//                    Log.d("ErrorListner"," "+it.message.toString().trim())
//                }
//            }.addOnFailureListener {
//                Log.d("ErrorListner"," "+it.message.toString().trim())
//            }
//            dbRefrence=FirebaseDatabase.getInstance().reference
//            val postKey: String? = dbRefrence.child(Keys.POSTS).push().key
//            if(postKey!=null)
//            {
//
////                dbRefrence.child(Keys.POSTS).child(postKey).setValue()
//            }

//            dbRefrence=FirebaseDatabase.getInstance().reference
//            val postsKey=dbRefrence.child(Keys.POSTS).push().key
//            if(postsKey!=null)
//            {
                var post_U: String? =sharedPrefrences.getString(Keys.POST_URL," ")
                var profile_u: String? =sharedPrefrences.getString(Keys.PROFILE_URL," ")
                val post:UserPost= UserPost(profile_u!!,post_U!!,binding.captionEdittext.text.toString().trim(),binding.reelName.text.toString().trim(),binding.reelTitle.text.toString().trim(),TimeAgo.using(System.currentTimeMillis()))
//                dbRefrence.child(Keys.POSTS).child(postsKey).setValue(post).addOnSuccessListener {
//
//                }.addOnFailureListener {
//                    Log.d("ErrorListner"," "+it.message.toString().trim())
//                }
//            }
            firebaseFirestore=FirebaseFirestore.getInstance()
            docRefrence=firebaseFirestore.collection(user.uid+Keys.POSTS_FOLDER).document()
            docRefrence.set(post).addOnSuccessListener {
                editor.clear()
                editor.commit()
            }.addOnFailureListener {

            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK)
        {
            if(requestCode==Keys.PROFILE_IMAGE_GALLERY_REQ)
            {
                profileImageUri=data?.data!!
            }
            else if(requestCode==Keys.POST_IMAGE_GALLERY_REQ)
            {
                postImageUri=data?.data!!
            }
        }
    }

}