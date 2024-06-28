package com.example.project_pao

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.example.project_pao.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(applicationContext)

        if (session.isLoggedIn()) {

            var i: Intent = Intent(applicationContext, OverViewActivity::class.java)
            startActivity(i)
            finish()
        } else {
            var Fragment: Fragment? = null
            Fragment = FirstFragment()
            replaceFragment(Fragment)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(
                binding.frameLayout.id,
                FirstFragment()
            ).commit()
        }
    }

    fun replaceFragment(someFragment: Fragment) {
        var binding: ActivityMainBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.frameLayout.id, someFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }


}