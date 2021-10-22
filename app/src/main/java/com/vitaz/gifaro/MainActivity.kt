package com.vitaz.gifaro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vitaz.gifaro.databinding.ActivityMainBinding
import com.vitaz.gifaro.misc.ViewPagerAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)

        val tabLayout = binding.tabLayout
        tabLayout.setupWithViewPager(viewPager)

    }
}
