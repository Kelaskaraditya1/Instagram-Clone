package com.starkindustries.instagram_clone.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.starkindustries.instagram_clone.Fragments.AddFragment
import com.starkindustries.instagram_clone.Fragments.HomeFragment
import com.starkindustries.instagram_clone.Fragments.ProfileFragment
import com.starkindustries.instagram_clone.Fragments.ReelsFragment
import com.starkindustries.instagram_clone.Fragments.SearchFragment
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.R
import com.starkindustries.instagram_clone.databinding.ActivityDashBoardBinding
class DashBoardActivity : AppCompatActivity() {
    lateinit var binding:ActivityDashBoardBinding
    lateinit var auth:FirebaseAuth
    lateinit var user:FirebaseUser
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dash_board)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_dash_board)
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.DEFAULT_GOOGLE_SIGNIN_ID))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(this,gso)
//        binding.logoutButton.setOnClickListener()
//        {
//            googleSignInClient.signOut().addOnCompleteListener()
//            {
//                if(it.isSuccessful)
//                {
//                    val intent = Intent(this,LoginActivity::class.java)
//                    sharedPreferences=getSharedPreferences(Keys.SHARED_PREFRENCES_NAME, MODE_PRIVATE)
//                    editor=sharedPreferences.edit()
//                    editor.putBoolean(Keys.LOGIN_STATUS,false)
//                    editor.apply()
//                    startActivity(intent)
//                    finish()
//                }
//            }.addOnFailureListener()
//            {
//                Log.d("ErrorListner"," "+it.message.toString().trim())
//            }
//        }
        val manaer:FragmentManager= supportFragmentManager
        val transaction:FragmentTransaction = manaer.beginTransaction()
        transaction.add(R.id.frameLayout,HomeFragment())
        transaction.commit()
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(object:BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(p0: MenuItem): Boolean
            {
                var id = p0.itemId
                when(id)
                {
                    R.id.home->{
                        val manaer:FragmentManager= supportFragmentManager
                        val transaction:FragmentTransaction = manaer.beginTransaction()
                        transaction.replace(R.id.frameLayout,HomeFragment())
                        transaction.commit()
                    }
                    R.id.search->{
                        val manaer:FragmentManager= supportFragmentManager
                        val transaction:FragmentTransaction = manaer.beginTransaction()
                        transaction.replace(R.id.frameLayout,SearchFragment())
                        transaction.commit()
                    }
                    R.id.add->{
                        val manaer:FragmentManager= supportFragmentManager
                        val transaction:FragmentTransaction = manaer.beginTransaction()
                        transaction.replace(R.id.frameLayout,AddFragment())
                        transaction.commit()
                    }
                    R.id.reels->{
                        val manaer:FragmentManager= supportFragmentManager
                        val transaction:FragmentTransaction = manaer.beginTransaction()
                        transaction.replace(R.id.frameLayout,ReelsFragment())
                        transaction.commit()
                    }
                    R.id.profile->{
                        val manaer:FragmentManager= supportFragmentManager
                        val transaction:FragmentTransaction = manaer.beginTransaction()
                        transaction.replace(R.id.frameLayout,ProfileFragment())
                        transaction.commit()
                    }
                }
                return true
            }
        })
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}