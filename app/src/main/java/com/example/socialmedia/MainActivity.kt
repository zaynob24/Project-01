package com.example.socialmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.socialmedia.databinding.ActivityMainBinding
import com.example.socialmedia.repositories.FirebaseServiceRepository
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseServiceRepository.init() // init for the Repository then we use it any where


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
       navController = navHostFragment.navController
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)



        bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.action_profileFragment2_to_loginFragment -> {
                    // bottomNavView.visibility = View.GONE
                    bottomNavView.visibility = View.GONE
                }
                R.id.singeUpFragment2 -> {
                    //   bottomNavView.visibility = View.GONE
                    bottomNavView.visibility = View.GONE
                }
                R.id.loginFragment -> {
                    bottomNavView.visibility = View.GONE
                }
                else -> {
                    //bottomNavView.visibility = View.VISIBLE
                    bottomNavView.visibility = View.VISIBLE
                }
            }
        }

    }


}