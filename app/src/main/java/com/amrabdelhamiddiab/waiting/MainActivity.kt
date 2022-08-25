package com.amrabdelhamiddiab.waiting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.waiting.databinding.ActivityMainBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.*
import com.amrabdelhamiddiab.waiting.presentation.loginflow.MyDrawerController
import com.google.android.gms.ads.*
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
 /*       //--------------------------
        val widthPx = screenRectPx.width()
        val heightPx = screenRectPx.height()
        Log.d(TAG,"[PX] screen width: $widthPx , height: $heightPx")

        val widthDp = screenRectDp.width()
        val heightDp = screenRectDp.height()
        Log.d(TAG,"[DP] screen width: $widthDp , height: $heightDp")

        println()

        val physicalWidthPx = physicalScreenRectPx.width()
        val physicalHeightPx = physicalScreenRectPx.height()
        Log.d(TAG,"[PX] physical screen width: $physicalWidthPx , height: $physicalHeightPx")

        val physicalWidthDp = physicalScreenRectDp.width()
        val physicalHeightDp = physicalScreenRectDp.height()
        Log.d(TAG,"[DP] physical screen width: $physicalWidthDp , height: $physicalHeightDp")
        //--------------------------*/

        //Google Ads
        MobileAds.initialize(this) {
            Log.d(TAG, it.toString())
        }
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().setTestDeviceIds(listOf("", "")).build()
        )
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        binding.adView.adListener = object : AdListener() {
            override fun onAdClicked() {
                super.onAdClicked()
                Log.d(TAG,"onAdClicked//////////////////////////////" )
            }

            override fun onAdClosed() {
                super.onAdClosed()
                Log.d(TAG,"onAdClosed...........//////////////////////////////" )
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.d(TAG,"onAdFailedToLoad ${p0.message}...........//////////////////////////////" )
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.d(TAG,"onAdImpression...........//////////////////////////////" )
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.d(TAG,"onAdLoaded...........//////////////////////////////" )
            }

            override fun onAdOpened() {
                super.onAdOpened()
                Log.d(TAG,"onAdOpened...........//////////////////////////////" )
            }
        }

//----------------------------------------
        /* appBarLayout.background.alpha = 1*/
        //  WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(binding.root)
        //  createWaitingNotificationChannel()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val language = sharedPreferences.getString("language_list", "en")
        Log.d(TAG, "888888888888888888888888888888888888888888888888888888" + language.toString())
        LocaleHelper.updateResources(this, language)
        val nightMode = sharedPreferences.getBoolean("night_mode", false)
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

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
/*
    private fun createWaitingNotificationChannel() {
        val soundMe: Uri =
            Uri.parse("android.resource://" + packageName + "/" + com.amrabdelhamiddiab.waiting.R.raw.sound)

        with(NotificationManagerCompat.from(applicationContext)) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "My channel description"
                enableLights(true)
                lightColor = Color.GREEN
                setSound(soundMe, null)
            }
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            createNotificationChannel(channel)
        }
    }*/

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun  onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
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
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun showStatusBar() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window,
            binding.root
        ).show(WindowInsetsCompat.Type.statusBars())

        // your code depending upon what you have implemented
    }

    fun changeTheme() {
        this.theme.applyStyle(R.style.Theme_Waiting_NoActionBar, true)

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    override fun onPause() {
        binding.adView.pause()
        super.onPause()

    }

    override fun onResume() {
        binding.adView.resume()
        super.onResume()

    }

    override fun onDestroy() {
        binding.adView.destroy()
        super.onDestroy()

    }
    companion object {
        const val TAG = "MainActivity"

        /*    const val CHANNEL_ID = "notification_channel"
            const val CHANNEL_NAME = "com.amrabdelhamiddiab.waiting"*/
    }
}