package com.giftcard8.todoapp.ui.auth.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.giftcard8.todoapp.R
import com.giftcard8.todoapp.ui.task.activity.TaskListActivity
import com.giftcard8.todoapp.utils.UserPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val token = userPreferences.getToken().firstOrNull()

            if (token != null) {
                startActivity(Intent(this@AuthActivity, TaskListActivity::class.java))
                finish()
            } else {
                setContentView(R.layout.activity_auth)

                val navHostFragment =
                    supportFragmentManager
                        .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

                val navController = navHostFragment.navController
                setupActionBarWithNavController(navController)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
}
