package com.starkindustries.instagram_clone.Posts
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.starkindustries.instagram_clone.R
import com.starkindustries.instagram_clone.databinding.ActivityPostsBinding
import java.lang.Exception

class PostsActivity : AppCompatActivity() {
    lateinit var binding:ActivityPostsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_posts)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_posts)
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}