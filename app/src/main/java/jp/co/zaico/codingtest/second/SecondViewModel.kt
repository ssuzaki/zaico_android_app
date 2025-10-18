package jp.co.zaico.codingtest.second

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import jp.co.zaico.codingtest.R
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

/**
 * 在庫詳細画面用ViewModel
 * @param context アプリケーションコンテキスト
 * @param client KtorのHTTPクライアント
 */
@HiltViewModel
class SecondViewModel @Inject constructor (
    @ApplicationContext private val context: Context,
    private val client: HttpClient
) : ViewModel() {

    var uiState by mutableStateOf(SecondUiState())
        private set

    /**
     * 在庫詳細を読み込む
     * @param inventoryId 在庫ID
     */
    fun load(inventoryId: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(running = true)

            runCatching {
                val url =
                    "${context.getString(R.string.api_endpoint)}/api/v1/inventories/$inventoryId"
                val jsonText = client.get(url) {
                    header("Authorization", "Bearer ${context.getString(R.string.api_token)}")
                }.bodyAsText()

                val obj = Json.Default.parseToJsonElement(jsonText).jsonObject
                val id = obj["id"]?.jsonPrimitive?.intOrNull ?: 0
                val title = obj["title"]?.jsonPrimitive?.contentOrNull ?: "-"
                val quantity = obj["quantity"]?.jsonPrimitive?.contentOrNull ?: "-"
                SecondUiState(
                    id = id,
                    title = title,
                    quantity = quantity
                )
            }.onSuccess { inv ->
                uiState = uiState.copy(
                    id = inv.id,
                    title = inv.title,
                    quantity = inv.quantity,
                    running = false
                )
            }.onFailure { e ->
                uiState = uiState.copy(
                    running = false,
                    errorMessage = e.message ?: "Error"
                )
            }
        }
    }
}
