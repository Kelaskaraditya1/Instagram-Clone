package com.starkindustries.instagram_clone.Activity
import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.R
import com.starkindustries.instagram_clone.databinding.ActivityRegisterBinding
class RegisterActivity : AppCompatActivity() {
    lateinit var binding:ActivityRegisterBinding
    lateinit var auth:FirebaseAuth
    lateinit var user:FirebaseUser
    var passed:Boolean=false
    lateinit var firestore:FirebaseFirestore
    lateinit var docrefrence:DocumentReference
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var storageRefrence:StorageReference
    lateinit var childRefrence:StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_register)
        auth=FirebaseAuth.getInstance()
        binding.registerProfileImage.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, Keys.GALLERY_REQ_CODE)
//            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
//            {
//
//            }
//            else
//                ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),Keys.EXTERNAL_STOREAGE_REQ_CODE)
        }
        binding.registerPassword.setOnTouchListener(object:View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event != null) {
                    if(event.action==MotionEvent.ACTION_UP) {
                        val selection=binding.registerPassword.selectionEnd
                        if(event.rawX>(binding.registerPassword.right-binding.registerPassword.compoundDrawables[Keys.RIGHT].bounds.width())) {
                            if(passed) {
                                binding.registerPassword.transformationMethod=PasswordTransformationMethod.getInstance()
                                binding.registerPassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.visibility_off,0)
                                passed=false
                            } else {
                                binding.registerPassword.transformationMethod=HideReturnsTransformationMethod.getInstance()
                                binding.registerPassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.visibility_on_two,0)
                                passed=true
                            }
                            binding.registerPassword.setSelection(selection)
                            return true
                        }
                    }
                }
                return false
            }
        })
        binding.registerButton.setOnClickListener()
        {
            if(TextUtils.isEmpty(binding.registerName.text.toString().trim()))
            {
                binding.registerName.setError("Field is Empty!!")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(binding.registerEmail.text.toString().trim()))
            {
                binding.registerEmail.setError("Field is Empty!!")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(binding.registerPhoneNo.text.toString().trim()))
            {
                binding.registerPhoneNo.setError("Field is Empty!!")
                return@setOnClickListener
            }
            else if(binding.registerPhoneNo.text.toString().trim().length<10)
            {
                binding.registerPhoneNo.setError("Phone no should be at least of 10 digits")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(binding.registerUsername.text.toString().trim()))
            {
                binding.registerUsername.setError("Field is Empty!!")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(binding.registerPassword.text.toString().trim()))
            {
                binding.registerPassword.setError("Field is Empty!!")
                return@setOnClickListener
            }
            else if(binding.registerPassword.text.toString().trim().length<8)
            {
                binding.registerPassword.setError("Password should be atleast of 8 charecters.")
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(binding.registerEmail.text.toString().trim(),binding.registerPassword.text.toString().trim())
                .addOnCompleteListener()
                {
                    if(it.isSuccessful)
                    {
                        val view = this.currentFocus
                        if(view!=null)
                        {
                            val manager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            manager.hideSoftInputFromWindow(view.windowToken,0)
                        }
                        user=auth.currentUser!!
                        storageRefrence=FirebaseStorage.getInstance().reference
                        childRefrence=storageRefrence.child(binding.registerName.text.toString().trim()+"/"+user.uid+"/"+Keys.IMAGES+"/"+Keys.USER_PROFILE_IMAGE)
                        childRefrence.putFile(binding.registerProfileImageViewer.getTag() as Uri).addOnSuccessListener()
                        {
                            it.storage.downloadUrl.addOnSuccessListener {
                                uri->
                                firestore=FirebaseFirestore.getInstance()
                                docrefrence=firestore.collection(Keys.COLLECTION_NAME).document(user.uid)
                                val map = mutableMapOf<String,Any>()
                                map.put(Keys.NAME,binding.registerName.text.toString().trim())
                                map.put(Keys.EMAIL,binding.registerEmail.text.toString().trim())
                                map.put(Keys.PHONE_NO,binding.registerPhoneNo.text.toString().trim())
                                map.put(Keys.PHOTO_URI,binding.registerProfileImageViewer.getTag().toString().trim())
                                map.put(Keys.DOWNLOAD_URL,uri.toString().trim())
                                map.put(Keys.USERNAME,binding.registerUsername.text.toString().trim())
                                map.put(Keys.PASSWORD,binding.registerPassword.text.toString().trim())
                                map.put(Keys.SIGNIN_TYPE,Keys.EMAIL_AND_PASSWORD_SIGNIN_TYPE)
                                docrefrence.set(map).addOnCompleteListener()
                                {
                                    if(it.isSuccessful)
                                    {
                                        val update = UserProfileChangeRequest.Builder()
                                            .setDisplayName(binding.registerUsername.text.toString().trim())
                                            .setPhotoUri(uri)
                                            .build()
                                        user.updateProfile(update).addOnCompleteListener()
                                        {
                                            if(it.isSuccessful)
                                            {
                                                sharedPreferences=getSharedPreferences(Keys.SHARED_PREFRENCES_NAME, MODE_PRIVATE)
                                                editor=sharedPreferences.edit()
                                                editor.putBoolean(Keys.LOGIN_STATUS,true)
                                                editor.apply()
                                                val intent = Intent(this,DashBoardActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                            }
                                        }.addOnFailureListener()
                                        {
                                            Log.d("ErrorListner"," "+it.message.toString().trim())
                                        }
                                    }
                                }.addOnFailureListener {
                                    Log.d("ErrorListner"," "+it.message.toString().trim())
                                }
                            }.addOnFailureListener {
                                Log.d("ErrorListner"," "+it.message.toString().trim())
                            }
                        }.addOnFailureListener()
                        {
                            Log.d("ErrorListner"," "+it.message.toString().trim())
                        }
                    }
                }.addOnFailureListener()
                {
                    Log.d("ErrorListner"," "+it.message.toString().trim())
                }
        }
        binding.loginButton.setOnClickListener()
        {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
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
                binding.registerProfileImageViewer.setImageURI(data?.data!!)
                binding.registerProfileImageViewer.setTag(data?.data)
                binding.registerProfileImage.visibility= View.GONE
            }
        }
    }
}