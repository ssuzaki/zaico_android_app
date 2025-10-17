package jp.co.zaico.codingtest.add

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.zaico.codingtest.api.InventoryRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * アイテム追加画面のViewModel
 * @param repo 在庫リポジトリ
 */
@HiltViewModel
class AddViewModel @Inject constructor(
    private val repo: InventoryRepository
) : ViewModel() {

    var uiState by mutableStateOf(AddUiState())
        private set

    /**
     * アイテム追加をリクエストする
     * @param title アイテムのタイトル
     */
    fun requestCreate(title: String) {
        uiState = uiState.copy(title = title, running = true)

        viewModelScope.launch {
            val result = runCatching {
                repo.createSimple(title = title)
            }

            // 通信終了
            uiState = uiState.copy(running = false)

            result.onSuccess {
                Log.d("ssuzaki", "Create succeeded: $it")
            }.onFailure {
                Log.e("ssuzaki", "Create failed", it)
            }
        }
    }
}
