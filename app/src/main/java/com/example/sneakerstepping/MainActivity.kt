package com.example.sneakerstepping

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.sneakerstepping.ui.viewmodel.SneakerViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private val viewModel: SneakerViewModel by viewModels()
    private lateinit var navController: NavController




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initViews()

    }

    private fun initViews() {
        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        navController = findNavController(R.id.nav_host_fragment)

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return  when (item.itemId){
            R.id.logOutItem -> {
                viewModel.signOut()
                drawerLayout.closeDrawer(GravityCompat.START)
                Toast.makeText(this, "You succesfully logged out", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.action_homeFragment_to_startupFragment)
                true
            }
            R.id.addShoeItem -> {
                navController.navigate(R.id.action_homeFragment_to_addShoeFragment)
                true
            }
            else -> true
        }
    }
}