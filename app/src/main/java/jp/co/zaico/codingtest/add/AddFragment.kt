package jp.co.zaico.codingtest.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

/**
 * アイテム追加画面のフラグメント
 */
@AndroidEntryPoint
class AddFragment: Fragment() {

    private val viewModel: AddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(context = requireContext()).apply {
            setContent {
                val uiState by viewModel::uiState

                AddContents(
                    uiState,
                    onSubmit = { title ->
                        viewModel.requestCreate(title)
                    },
                    onDialogDismiss = { viewModel.onDialogDismiss() }
                )
            }
        }
    }
}
