package com.amrabdelhamiddiab.waiting

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_SIGNATURES
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.afollestad.materialdialogs.MaterialDialog
import com.amrabdelhamiddiab.waiting.databinding.ActivityMainBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.LocaleHelper
import com.amrabdelhamiddiab.waiting.presentation.loginflow.MyDrawerController
import com.google.android.gms.ads.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


@Suppress("DEPRECATION")
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Google Ads
        MobileAds.initialize(this)
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().setTestDeviceIds(listOf("", "")).build()
        )
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)



        //*****************************************
        try {
            val info = packageManager.getPackageInfo(
                "com.amrabdelhamiddiab.waiting",
                GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
        //****************************************



        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val language = sharedPreferences.getString("language_list", "en")
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

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.settingsFragment -> {
                    NavigationUI.onNavDestinationSelected(it, navController)
                    binding.drawerLayout.closeDrawer(GravityCompat.START, true)
                    true
                }
                R.id.sendUsEmail -> {
                    choiceSendUsEmail()
                    binding.drawerLayout.closeDrawer(GravityCompat.START, true)
                    true
                }
                R.id.reportIssue -> {
                    choiceReportIssue()
                    binding.drawerLayout.closeDrawer(GravityCompat.START, true)
                    true
                }
                R.id.suggestFeature -> {
                    suggestFeature()
                    binding.drawerLayout.closeDrawer(GravityCompat.START, true)
                    true
                }
                R.id.aboutWaiting -> {
                    choiceAboutWaiting()
                    binding.drawerLayout.closeDrawer(GravityCompat.START, true)
                    true
                }
                R.id.inviteFriends -> {
                    choiceInviteFriends()
                    binding.drawerLayout.closeDrawer(GravityCompat.START, true)
                    true
                }
                R.id.privacyFragment -> {
                    choiceTermsOfUse()
                    NavigationUI.onNavDestinationSelected(it, navController)
                    binding.drawerLayout.closeDrawer(GravityCompat.START, true)
                    true
                }
                R.id.termsFragment -> {
                    NavigationUI.onNavDestinationSelected(it, navController)
                    binding.drawerLayout.closeDrawer(GravityCompat.START, true)
                    true
                }
                else -> false
            }
        }

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

    private fun choiceTermsOfUse() {
    }

    private fun choiceAboutWaiting() {
        MaterialDialog(this).show {
            title(R.string.about_waiting)
            message(R.string.about_eaiting_description)
        }
    }

    private fun choiceInviteFriends() {
        val url = "http://play.google.com/store/apps/details?id=" + this.packageName
        Log.d(TAG, url)
        val urlOfApp = getString(R.string.checkout_app) + "\n\n" + url
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, urlOfApp).type = "text/plain"
        startActivity(Intent.createChooser(shareIntent, "Share to "))
    }


    private fun suggestFeature() {
        val subject = "Waiting ${getVersionName()}"
        val title = "Suggest a feature "
        val body = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n ${getHardWareInformation()}"
        val mailIntent = Intent(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_SUBJECT, subject)
            .putExtra(Intent.EXTRA_TITLE, title)
            .putExtra(Intent.EXTRA_TEXT, body)
            .putExtra(Intent.EXTRA_EMAIL, arrayOf("engamrdiab52@gmail.com"))
        mailIntent.type = "text/plain"
        startActivity(Intent.createChooser(mailIntent, "Send by "))
    }

    private fun getVersionName(): String {
        return packageManager
            .getPackageInfo(packageName, 0).versionName
    }


    private fun getHardWareInformation(): String {
        return "Brand: ${Build.BRAND} \n\n" +
                "Model: ${Build.MODEL} \n\n" +
                "ID: ${Build.ID} \n\n" +
                "SDK: ${Build.VERSION.SDK_INT} \n\n" +
                "Manufacture: ${Build.MANUFACTURER} \n\n" +
                "Brand: ${Build.BRAND} \n\n" +
                "User: ${Build.USER} \n\n" +
                "Type: ${Build.TYPE} \n\n" +
                "Base: ${Build.VERSION_CODES.BASE} \n\n" +
                "Incremental: ${Build.VERSION.INCREMENTAL} \n\n" +
                "Board: ${Build.BOARD} \n\n" +
                "Host: ${Build.HOST} \n\n" +
                "FingerPrint: ${Build.FINGERPRINT} \n\n" +
                "Version Code: ${Build.VERSION.RELEASE}"
    }

    private fun choiceReportIssue() {

        val subject = "Waiting ${getVersionName()}"
        val title = "Report an Issue "
        val body = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n ${getHardWareInformation()}"
        val mailIntent = Intent(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_SUBJECT, subject)
            .putExtra(Intent.EXTRA_TITLE, title)
            .putExtra(Intent.EXTRA_TEXT, body)
            .putExtra(Intent.EXTRA_EMAIL, arrayOf("engamrdiab52@gmail.com"))
        mailIntent.type = "text/plain"
        startActivity(Intent.createChooser(mailIntent, "Send by "))
    }

    private fun choiceSendUsEmail() {
        val subject = "Waiting ${getVersionName()}"
        val title = "Contact Us "
        val body = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n ${getHardWareInformation()}"
        val mailIntent = Intent(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_SUBJECT, subject)
            .putExtra(Intent.EXTRA_TITLE, title)
            .putExtra(Intent.EXTRA_TEXT, body)
            .putExtra(Intent.EXTRA_EMAIL, arrayOf("engamrdiab52@gmail.com"))
        mailIntent.type = "text/plain"
        startActivity(Intent.createChooser(mailIntent, "Send by "))
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    override fun setDrawerLocked() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toolbar.visibility = View.GONE
    }

    override fun setDrawerUnlocked() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.visibility = View.VISIBLE
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

    }

}