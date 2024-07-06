package com.starkindustries.instagram_clone.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.R
import com.starkindustries.instagram_clone.databinding.ActivityDashBoardBinding
class DashBoardActivity : AppCompatActivity() {
    lateinit var binding:ActivityDashBoardBinding
    lateinit var auth:FirebaseAuth
    lateinit var user:FirebaseUser
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dash_board)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_dash_board)
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        binding.logoutButton.setOnClickListener()
        {
            auth.signOut()
            val intent = Intent(this,LoginActivity::class.java)
            sharedPreferences=getSharedPreferences(Keys.SHARED_PREFRENCES_NAME, MODE_PRIVATE)
            editor=sharedPreferences.edit()
            editor.putBoolean(Keys.LOGIN_STATUS,false)
            editor.apply()
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}