package jp.co.zaico.codingtest.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

/**
 * 在庫詳細画面のフラグメント
 */
@AndroidEntryPoint
class SecondFragment : Fragment() {

    private val viewModel: SecondViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inventoryId = arguments?.getString("inventoryId")?.toInt() ?: 0
        viewModel.load(inventoryId)

        return ComposeView(context = requireContext()).apply {
            setContent {
                val uiState by viewModel::uiState

                SecondContents(
                    uiState = uiState
                )
            }
        }
    }
}