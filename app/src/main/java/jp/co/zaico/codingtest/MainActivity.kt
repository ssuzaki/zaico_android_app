package jp.co.zaico.codingtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.zaico.codingtest.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private var destListener: NavController.OnDestinationChangedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navController = (supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // 追加ボタンの表示・非表示をナビゲーション先に応じて切り替え
        val fab = binding.fab
        destListener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val showFab = destination.id == R.id.FirstFragment
            if (showFab) {
                fab.isVisible = true
                fab.animate()
                    .alpha(1f).scaleX(1f).scaleY(1f)
                    .setDuration(120)
                    .start()
            } else {
                fab.animate()
                    .alpha(0f).scaleX(0.9f).scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction { fab.isGone = true }
                    .start()
            }
        }.also { navController.addOnDestinationChangedListener(it) }

        fab.setOnClickListener { view ->
            navController.navigate(R.id.AddFragment)
        }
    }

    /**
     * 画面破棄時の処理（リスナー解除）
     */
    override fun onDestroy() {
        destListener?.let { navController.removeOnDestinationChangedListener(it) }
        destListener = null
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
