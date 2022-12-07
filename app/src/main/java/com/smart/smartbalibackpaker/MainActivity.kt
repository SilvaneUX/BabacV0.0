package com.smart.smartbalibackpaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.smart.smartbalibackpaker.dashboard.DashboardFragment
import com.smart.smartbalibackpaker.databinding.ActivityMainBinding
import com.smart.smartbalibackpaker.guide.GuideTourFragment
import com.smart.smartbalibackpaker.registration.RegistFirstFormActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var user: FirebaseUser? = null
    private lateinit var dbReference: DatabaseReference
    private var myUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme(R.style.SettingsFragmentStyle)

        user = FirebaseAuth.getInstance().currentUser
        myUid = user?.uid
        val check = intent.getIntExtra(EXTRA_CHECK, 0)

        binding.btnRegistBackpacker.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegistFirstFormActivity::class.java))
        }

        if (check == 1){
           val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
            navView.itemIconTintList = null
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.action_dashboard_to_guide)
    //        val navController = findNavController(R.id.nav_host_fragment)
            navView.setupWithNavController(navController)
            navView.drawableState
        } else {
            val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
            navView.itemIconTintList = null
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
//        val navController = findNavController(R.id.nav_host_fragment)
            navView.setupWithNavController(navController)
            navView.drawableState
        }

    }


    companion object {
        const val EXTRA_CHECK = "extra_check"
        const val EXTRA_ROUTE = "extra_route"
    }

}