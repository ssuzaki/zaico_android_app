package jp.co.zaico.codingtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint

/**
 * 在庫一覧を表示するフラグメント
 */
@AndroidEntryPoint
class FirstFragment : Fragment() {
    private val viewModel: FirstViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.load()

        return ComposeView(context = requireContext()).apply {
            setContent {
                val uiState by viewModel::uiState

                FirstContents(
                    uiState = uiState,
                    query = uiState.query,
                    onQueryChange = viewModel::onQueryChange,
                    onSearch = {},
                    onItemClick = { item ->
                        val bundle = bundleOf("inventoryId" to item.id.toString())
                        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
                    },
                    onRefresh = { viewModel.load() }
                )
            }
        }
    }
}
