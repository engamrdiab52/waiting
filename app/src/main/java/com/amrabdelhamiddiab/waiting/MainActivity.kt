package com.amrabdelhamiddiab.waiting

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.waiting.databinding.ActivityMainBinding
import com.amrabdelhamiddiab.waiting.presentation.loginflow.MyDrawerController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MyDrawerController {

    private val navController: NavController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var preferenceHelper: IPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
       /* appBarLayout.background.alpha = 1*/
      //  WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(binding.root)
        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView
        toolbar = binding.toolbar
        appBarLayout = binding.appbar

        setSupportActionBar(toolbar)

        navigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.clientFragment,
                R.id.serviceFragment
            ), drawerLayout
        )
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_app_bar, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        })

        MyFirebaseMessagingService.sharedPref =
            getSharedPreferences("sharedPrefToken", Context.MODE_PRIVATE)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun setDrawerLocked() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toolbar.visibility = View.GONE
        //  fab.visibility = View.GONE
      //  bottomNavigationView.visibility = View.GONE
    }

    override fun setDrawerUnlocked() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.visibility = View.VISIBLE
        //  fab.visibility = View.VISIBLE
     //   bottomNavigationView.visibility = View.VISIBLE
    }



    fun hideStatusBar() {

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun showStatusBar() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, binding.root).show(WindowInsetsCompat.Type.statusBars())

        // your code depending upon what you have implemented
    }
    fun changeTheme() {
       this.theme.applyStyle(R.style.Theme_Waiting_NoActionBar, true)

    }



    companion object {
        const val TAG = "MainActivity"
    }
}