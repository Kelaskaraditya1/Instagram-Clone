package com.starkindustries.instagram_clone.Activity
import android.content.Context
import android.content.Intent
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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.Model.ReelsModel
import com.starkindustries.instagram_clone.R
import com.starkindustries.instagram_clone.databinding.ActivityReelsUploadBinding
class ReelsUploadActivity : AppCompatActivity() {
    lateinit var binding:ActivityReelsUploadBinding
    lateinit var profileImageUri:Uri
    lateinit var reeluri:Uri
    lateinit var dbRefrence:DatabaseReference
    lateinit var auth:FirebaseAuth
    lateinit var user:FirebaseUser
    lateinit var storageReference: StorageReference
    lateinit var childRefrence:StorageReference
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var docRefrence:DocumentReference
    lateinit var imaegurl:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reels_upload)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_reels_upload)
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        binding.ProfileImageButton.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,Keys.GALLERY_REQ_CODE)
        }
        binding.videoButton.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("video/*")
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent,Keys.VIDEO_REQ_CODE)
        }
        binding.uploadButton.setOnClickListener()
        {
            storageReference=FirebaseStorage.getInstance().reference
            childRefrence=storageReference.child(user.displayName.toString().trim()+"/"+user.uid+"/"+Keys.REELS+"/"+profileImageUri.toString().trim())
            childRefrence.putFile(profileImageUri).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {
                    imaegurl=it.toString()
                }.addOnFailureListener {
                    Log.d("ErrorListner"," "+it.message.toString().trim())
                }
            }.addOnFailureListener {
                Log.d("ErrorListner"," "+it.message.toString().trim())
            }
            storageReference=FirebaseStorage.getInstance().reference
            childRefrence=storageReference.child(user.displayName.toString().trim()+"/"+user.uid+"/"+Keys.REELS+"/"+binding.reelName.text.toString().trim())
            childRefrence.putFile(reeluri).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {
                    firebaseFirestore=FirebaseFirestore.getInstance()
                    docRefrence=firebaseFirestore.collection(Keys.CUSTOM_REELS).document(user.uid)
                    val reel = ReelsModel( binding.reelName.text.toString().trim(),imaegurl, it.toString(), binding.captionEdittext.text.toString().trim())
                    docRefrence.set(reel!!).addOnSuccessListener {
                        docRefrence=firebaseFirestore.collection(user.uid!!+Keys.CUSTOM_REELS).document()
                        docRefrence.set(reel).addOnSuccessListener {
                            val view = this.currentFocus
                            if(view!=null)
                            {
                                val manager:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                manager.hideSoftInputFromWindow(view.windowToken,0)
                            }

                            Toast.makeText(applicationContext, "uploaded successfully!!", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Log.d("ErrorListner"," "+it.message.toString().trim())
                        }
                    }.addOnFailureListener {
                        Log.d("ErrorListner"," "+it.message.toString().trim())
                    }
                }.addOnFailureListener {
                    Log.d("ErrorListner"," "+it.message.toString().trim())
                }
            }.addOnFailureListener {
                Log.d("ErrorListner"," "+it.message.toString().trim())
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
            if(requestCode==Keys.GALLERY_REQ_CODE)
            {
                profileImageUri=data?.data!!
            }
            else if(requestCode==Keys.VIDEO_REQ_CODE)
            {
                reeluri=data?.data!!
            }
        }
    }
}