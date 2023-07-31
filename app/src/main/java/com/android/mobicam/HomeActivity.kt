package com.android.mobicam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.mobicam.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(RecordingFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.recording -> replaceFragment(RecordingFragment())
                R.id.profile -> replaceFragment(ProfileFragment())

                else -> {

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}