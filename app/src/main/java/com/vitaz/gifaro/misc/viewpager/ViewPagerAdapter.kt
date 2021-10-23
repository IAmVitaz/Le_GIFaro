package com.vitaz.gifaro.misc.viewpager

import androidx.fragment.app.*
import com.vitaz.gifaro.fragments.favourites.FavouritesFragment
import com.vitaz.gifaro.fragments.list.ListFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ListFragment()
            else -> FavouritesFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "All"
            else -> "Favourites"
        }
    }
}
