package com.starkindustries.instagram_clone.Posts
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.starkindustries.instagram_clone.Activity.DashBoardActivity
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.Model.Posts
import com.starkindustries.instagram_clone.R
import com.starkindustries.instagram_clone.databinding.ActivityPostsBinding
import java.lang.Exception

class PostsActivity : AppCompatActivity() {
    lateinit var binding:ActivityPostsBinding
    lateinit var auth:FirebaseAuth
    lateinit var user:FirebaseUser
    lateinit var firebaseFireStore:FirebaseFirestore
    lateinit var docRefrence:DocumentReference
    lateinit var storageRefrence:StorageReference
    lateinit var childRefrence:StorageReference
    lateinit var postUri:Uri
    lateinit var postDownloadUrl:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_posts)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_posts)
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        setSupportActionBar(binding.toolbar)
        try{
            supportActionBar?.setTitle("New Post")
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }
        binding.toolbar.setNavigationOnClickListener{
            finish()
        }
        binding.postCard.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,Keys.GALLERY_REQ_CODE)
        }
        binding.postButton.setOnClickListener()
        {
            auth=FirebaseAuth.getInstance()
            user=auth.currentUser!!
            firebaseFireStore=FirebaseFirestore.getInstance()
            docRefrence=firebaseFireStore.collection(Keys.COLLECTION_NAME).document(user.uid)
            docRefrence.addSnapshotListener(object:EventListener<DocumentSnapshot>{
                override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                    storageRefrence=FirebaseStorage.getInstance().reference
                    childRefrence=storageRefrence.child(value?.getString(Keys.NAME)+"/"+user.uid+"/"+Keys.POSTS+"/"+binding.title.text.toString().trim())
                    childRefrence.putFile(postUri).addOnSuccessListener {
                        it.storage.downloadUrl.addOnSuccessListener {
                            val map = mutableMapOf<String,Any>()
                            map.put(Keys.TITLE,binding.title.text.toString().trim())
                            map.put(Keys.CAPTION,binding.caption.text.toString().trim())
                            map.put(Keys.DOWNLOAD_URL,it.toString().trim())
                            val post:Posts = Posts(it.toString().trim(),binding.caption.text.toString().trim(),binding.title.text.toString().trim())
                            firebaseFireStore=FirebaseFirestore.getInstance()
                            docRefrence=firebaseFireStore.collection(Keys.POSTS_COLLECTIONS).document(user.uid)
                            docRefrence.set(post).addOnCompleteListener()
                            {
                                if(it.isSuccessful)
                                {
                                    docRefrence=firebaseFireStore.collection(user.uid+Keys.POSTS).document()
                                    docRefrence.set(post).addOnCompleteListener()
                                    {
                                        if(it.isSuccessful)
                                        {
                                            binding.PostPic.visibility=View.VISIBLE
                                            binding.viewPost.visibility=View.GONE
                                            binding.title.setText("")
                                            binding.caption.setText(" ")
                                            val intent = Intent(applicationContext,DashBoardActivity::class.java)
                                            startActivity(intent)
                                            Toast.makeText(applicationContext, "uploaded in firebasefirestore successfully", Toast.LENGTH_SHORT).show()
                                        }
                                    }.addOnFailureListener {
                                        Log.d("ErrorListner"," "+it.message.toString().trim())
                                    }
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
            })
        }
        binding.cancelButton.setOnClickListener()
        {
            val intent = Intent(applicationContext,DashBoardActivity::class.java)
            startActivity(intent)
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
                storageRefrence=FirebaseStorage.getInstance().reference
                binding.PostPic.visibility= View.GONE
                binding.viewPost.visibility=View.VISIBLE
                binding.viewPost.setImageURI(data?.data!!)
                postUri=data.data!!
            }
        }
    }
}