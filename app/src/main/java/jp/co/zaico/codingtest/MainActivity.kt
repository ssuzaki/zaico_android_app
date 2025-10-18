package jp.co.zaico.codingtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.zaico.codingtest.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // 追加ボタンの表示・非表示をナビゲーション先に応じて切り替え
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val showFab = destination.id == R.id.FirstFragment
            if (showFab) {
                binding.fab.apply {
                    isVisible = true
                    animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(120).start()
                }
            } else {
                binding.fab.apply {
                    animate().alpha(0f).scaleX(0.9f).scaleY(0.9f).setDuration(100)
                        .withEndAction { isGone = true }
                        .start()
                }
            }
        }

        binding.fab.setOnClickListener { view ->
            navController.navigate(R.id.AddFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
