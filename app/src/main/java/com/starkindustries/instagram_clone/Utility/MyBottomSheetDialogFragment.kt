package com.starkindustries.instagram_clone.Utility
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.starkindustries.instagram_clone.Posts.PostsActivity
import com.starkindustries.instagram_clone.R
import com.starkindustries.instagram_clone.Reels.ReelsActivity
class MyBottomSheetDialogFragment: BottomSheetDialogFragment()
{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.addPosts).setOnClickListener {
            val intent = Intent(requireContext(),PostsActivity()::class.java)
            startActivity(intent)
        }
        view.findViewById<View>(R.id.uploadReels).setOnClickListener {
            val intent = Intent(requireContext(),ReelsActivity::class.java)
            startActivity(intent)
        }
    }
}