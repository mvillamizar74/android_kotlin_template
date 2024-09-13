package co.frekuency.assets

import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import co.frekuency.assets.data.utilities.KeyCodePressEvent
import co.frekuency.assets.databinding.ActivityMainBinding
import co.frekuency.assets.ui.interfaces.DrawerStatusListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.greenrobot.eventbus.EventBus

class MainActivity : AppCompatActivity(), DrawerStatusListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appMainContent.toolbar)

        drawerLayout = binding.drawerLayout
        navView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.findNavController()
        //navController = findNavController(R.id.nav_host_fragment_content_main)
        val appBarLayout = binding.appMainContent.appBarLayout
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_login, R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.nav_home)
                }
                R.id.nav_gallery -> {
                    // To navigate to another fragment, use the following code
                    navController.navigate(R.id.nav_gallery)
                }
                R.id.nav_slideshow -> {
                    navController.navigate(R.id.nav_slideshow)
                }
                R.id.nav_logout -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(resources.getString(R.string.app_name))
                        .setMessage(resources.getString(R.string.logout_app_msg))
                        .setNeutralButton(resources.getString(R.string.cancel)) { _, _ ->
                            // Do nothing
                        }
                        .setPositiveButton(resources.getString(R.string.logout)) { _, _ ->
                            navController.popBackStack(R.id.nav_login,inclusive = true)
                            navController.navigate(R.id.nav_login)
                        }
                        .show()

                }
                R.id.nav_exit -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(resources.getString(R.string.app_name))
                        .setMessage(resources.getString(R.string.exit_app_msg))
                        .setNeutralButton(resources.getString(R.string.cancel)) { _, _ ->
                            // Do nothing
                        }
                        .setPositiveButton(resources.getString(R.string.exit)) { _, _ ->
                            finishAndRemoveTask()
                        }
                        .show()
                }
            }
            it.isChecked = true
            closeDrawer()
            true
        }

        //How to lock or unlock the nav drawer menu depending on the fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_login -> {
                    lockDrawer()
                    appBarLayout.visibility = View.GONE
                }
                else -> {
                    supportActionBar?.title = navController.currentDestination?.label
                    unlockDrawer()
                    // In case there is need to hide or show the app bar
                    if (!appBarLayout.isVisible) {
                        appBarLayout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event?.repeatCount == 0) {
            EventBus.getDefault().post(KeyCodePressEvent(keyCode = keyCode))
            return true
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun isDrawerOpen(): Boolean {
        return drawerLayout.isDrawerOpen(GravityCompat.START)
    }

    override fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun closeDrawer() {
        if (isDrawerOpen()) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    override fun lockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
}