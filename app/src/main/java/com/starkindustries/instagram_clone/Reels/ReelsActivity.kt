package com.starkindustries.instagram_clone.Reels
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
import com.starkindustries.instagram_clone.Model.Reels
import com.starkindustries.instagram_clone.R
import com.starkindustries.instagram_clone.databinding.ActivityReelsBinding
import java.lang.Exception
class ReelsActivity : AppCompatActivity() {
    lateinit var binding:ActivityReelsBinding
    lateinit var auth:FirebaseAuth
    lateinit var user: FirebaseUser
    lateinit var firebaseFireStore: FirebaseFirestore
    lateinit var docRefrence: DocumentReference
    lateinit var storageRefrence: StorageReference
    lateinit var childRefrence:StorageReference
    lateinit var reelUri: Uri
    lateinit var progressDialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reels)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_reels)
        auth= FirebaseAuth.getInstance()
        user=auth.currentUser!!
        setSupportActionBar(binding.reelsToolbar)
        try{
            supportActionBar?.setTitle("New Reel")
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        binding.reelsToolbar.setNavigationOnClickListener{
            finish()
        }
        binding.pickVideo.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("video/*")
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent,Keys.GALLERY_REQ_CODE)
        }
        binding.reelcancelButton.setOnClickListener()
        {
            val intent = Intent(applicationContext, DashBoardActivity::class.java)
            startActivity(intent)
        }
        binding.reelPostButton.setOnClickListener()
        {
            binding.rellsprogressBar.visibility=View.VISIBLE
            progressDialog= ProgressDialog(applicationContext)
            progressDialog.setTitle("Uploading your reel")
            auth=FirebaseAuth.getInstance()
            user=auth.currentUser!!
            firebaseFireStore=FirebaseFirestore.getInstance()
            docRefrence=firebaseFireStore.collection(Keys.COLLECTION_NAME).document(user.uid)
            docRefrence.addSnapshotListener(object: EventListener<DocumentSnapshot> {
                override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                    storageRefrence=FirebaseStorage.getInstance().reference
                    childRefrence=storageRefrence.child(user.displayName.toString().trim()+"/"+user.uid+"/"+Keys.PROFILE_REELS+"/"+binding.Reelstitle.text.toString().trim())
                    childRefrence.putFile(reelUri).addOnSuccessListener {
                        it.storage.downloadUrl.addOnSuccessListener {
                            val map = mutableMapOf<String,Any>()
                            map.put(Keys.TITLE,binding.Reelstitle.text.toString().trim())
                            map.put(Keys.CAPTION,binding.Reelscaption.text.toString().trim())
                            map.put(Keys.DOWNLOAD_URL,it.toString().trim())
                            val reel: Reels = Reels(it.toString().trim(),binding.Reelscaption.text.toString().trim(),binding.Reelstitle.text.toString().trim())
                            firebaseFireStore=FirebaseFirestore.getInstance()
                            docRefrence=firebaseFireStore.collection(Keys.REELS_COLLECTION).document(user.uid)
                            docRefrence.set(reel).addOnCompleteListener()
                            {
                                if(it.isSuccessful)
                                {
                                    docRefrence=firebaseFireStore.collection(user.uid+Keys.REELS).document()
                                    docRefrence.set(reel).addOnCompleteListener()
                                    {
                                        if(it.isSuccessful)
                                        {
                                            progressDialog.dismiss()
                                            binding.Reelstitle.setText("")
                                            binding.Reelscaption.setText(" ")
                                            binding.rellsprogressBar.visibility=View.GONE
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
                    }.addOnProgressListener {
                        val uploadedStatus = (it.bytesTransferred/it.totalByteCount)*100
                        progressDialog.setMessage("Uploaded "+uploadedStatus+"%")
                    }
                }
            })
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
                reelUri=data?.data!!
            }
        }
    }
}