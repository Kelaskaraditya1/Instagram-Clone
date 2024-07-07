package com.starkindustries.instagram_clone.Adapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.starkindustries.instagram_clone.Fragments.MyPostsFragment
import com.starkindustries.instagram_clone.Fragments.MyReelsFragment
class ProfileViewPagerAdapter(var manager:FragmentManager):FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
{
    override fun getCount(): Int {
        return 2
    }
    override fun getItem(position: Int): Fragment {
        var fragment:Fragment=MyPostsFragment()
        if(position==0)
            fragment= MyPostsFragment()
        else if(position==1)
            fragment= MyReelsFragment()
        return fragment!!
    }
    override fun getPageTitle(position: Int): CharSequence? {
        var title:String=" "
        if(position==0)
            title="My Posts"
        else if(position==1)
            title="My Reels"
        return title
    }
}