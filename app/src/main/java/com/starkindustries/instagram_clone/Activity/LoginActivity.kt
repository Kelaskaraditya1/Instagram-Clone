package com.starkindustries.instagram_clone.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.R
import com.starkindustries.instagram_clone.databinding.ActivityLoginBinding
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    var passed:Boolean=false
    lateinit var auth:FirebaseAuth
    lateinit var user:FirebaseUser
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_login)
        auth=FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.DEFAULT_GOOGLE_SIGNIN_ID))
            .requestEmail()
            .build()
        binding.signUpButton.setOnClickListener()
        {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.loginPassword.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event != null) {
                    if(event.action== MotionEvent.ACTION_UP) {
                        val selection=binding.loginPassword.selectionEnd
                        if(event.rawX>(binding.loginPassword.right-binding.loginPassword.compoundDrawables[Keys.RIGHT].bounds.width())) {
                            if(passed) {
                                binding.loginPassword.transformationMethod=
                                    PasswordTransformationMethod.getInstance()
                                binding.loginPassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.visibility_off,0)
                                passed=false
                            } else {
                                binding.loginPassword.transformationMethod=
                                    HideReturnsTransformationMethod.getInstance()
                                binding.loginPassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.visibility_on_two,0)
                                passed=true
                            }
                            binding.loginPassword.setSelection(selection)
                            return true
                        }
                    }
                }
                return false
            }
        })
        binding.LoginButton.setOnClickListener()
        {
            if(TextUtils.isEmpty(binding.loginUsername.text.toString().trim()))
            {
                binding.loginUsername.setError("Field is Empty!!")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(binding.loginPassword.text.toString().trim()))
            {
                binding.loginPassword.setError("Field is Empty!!")
                return@setOnClickListener
            }
            else if(binding.loginPassword.text.toString().trim().length<8)
            {
                binding.loginPassword.setError("Password should be atleast of 8 charecters.")
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(binding.loginUsername.text.toString().trim(),binding.loginPassword.text.toString().trim())
                .addOnCompleteListener()
                {
                    if(it.isSuccessful)
                    {
                        sharedPreferences=getSharedPreferences(Keys.SHARED_PREFRENCES_NAME, MODE_PRIVATE)
                        editor=sharedPreferences.edit()
                        editor.putBoolean(Keys.LOGIN_STATUS,true)
                        editor.apply()
                        user =auth.currentUser!!
                        val intent = Intent(this,DashBoardActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(applicationContext, "Salam "+user.displayName.toString().trim()+" bhai!!", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener()
                {
                    Toast.makeText(applicationContext, " "+it.message.toString().trim(), Toast.LENGTH_SHORT).show()
                }
        }
        googleSignInClient=GoogleSignIn.getClient(this,gso)
        binding.googleSigninButton.setOnClickListener()
        {
            val googleSignInintent = googleSignInClient.signInIntent
            startActivityForResult(googleSignInintent,Keys.GOOGLE_SIGNIN_INTENT_REQ_CODE)
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
            if(requestCode==Keys.GOOGLE_SIGNIN_INTENT_REQ_CODE)
            {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    if(account!=null)
                    {
                        val credentials = GoogleAuthProvider.getCredential(account.idToken,null)
                        auth.signInWithCredential(credentials).addOnCompleteListener()
                        {
                            if(it.isSuccessful)
                            {
                                sharedPreferences=getSharedPreferences(Keys.SHARED_PREFRENCES_NAME, MODE_PRIVATE)
                                editor=sharedPreferences.edit()
                                editor.putBoolean(Keys.LOGIN_STATUS,true)
                                editor.apply()
                                user =auth.currentUser!!
                                val intent = Intent(this,DashBoardActivity::class.java)
                                startActivity(intent)
                                finish()
                                Toast.makeText(applicationContext, "Salam "+user.displayName.toString().trim()+" bhai!!", Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener()
                        {
                            Log.d("ErrorListner"," "+it.message.toString().trim())
                        }
                    }
                }
                catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }
        }
    }
}