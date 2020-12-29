package com.example.sneakerstepping

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
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
    private var inFragment: Int = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        navController = findNavController(R.id.nav_host_fragment)
        initViews()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        switchMenu()
        return super.onCreateOptionsMenu(menu)
    }

    private fun initViews() {
        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
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
                drawerLayout.closeDrawer(GravityCompat.START)
                navController.navigate(R.id.action_homeFragment_to_addShoeFragment)
                true
            }
            R.id.homeItem -> {
                if (inFragment == R.id.addShoeFragment){
                    navController.navigate(R.id.action_addShoeFragment_to_homeFragment)
                }
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
            else -> true
        }
    }

    private fun switchMenu() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                in arrayOf(R.id.homeFragment) -> {
                    inFragment = R.id.homeFragment
                }
                in arrayOf(R.id.addShoeFragment) -> {
                    inFragment = R.id.addShoeFragment
                }
            }
        }
    }
}