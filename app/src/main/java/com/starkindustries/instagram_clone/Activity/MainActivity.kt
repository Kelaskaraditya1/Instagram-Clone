package com.starkindustries.instagram_clone.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.starkindustries.instagram_clone.Keys.Keys
import com.starkindustries.instagram_clone.R
import com.starkindustries.instagram_clone.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var auth:FirebaseAuth
    lateinit var user:FirebaseUser
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        auth=FirebaseAuth.getInstance()
//        user=auth.currentUser!!
        LongOperation().execute()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    inner open class LongOperation:AsyncTask<String?,Void?,String?>()
    {
        override fun doInBackground(vararg params: String?): String? {
            for(i in 0..2)
            {
                try
                {
                    Thread.sleep(1000)
                }
                catch (e:Exception)
                {
                    Thread.interrupted()
                }
            }
            return "results"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            sharedPreferences=getSharedPreferences(Keys.SHARED_PREFRENCES_NAME, MODE_PRIVATE)
            editor=sharedPreferences.edit()
            if(auth.currentUser!=null&&sharedPreferences.getBoolean(Keys.LOGIN_STATUS,false))
            {
                user=auth.currentUser!!
                val intent = Intent(this@MainActivity,DashBoardActivity::class.java)
                startActivity(intent)
                Toast.makeText(applicationContext, "Salam "+user.displayName.toString().trim()+" bhai!!", Toast.LENGTH_SHORT).show()
                finish()
            }
            else
            {
                val intent = Intent(this@MainActivity,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}