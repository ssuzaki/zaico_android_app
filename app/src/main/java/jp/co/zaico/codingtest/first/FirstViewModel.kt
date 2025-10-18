package jp.co.zaico.codingtest.first

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.text.Normalizer
import javax.inject.Inject

/**
 * 在庫一覧画面用ViewModel
 * @param context アプリケーションコンテキスト
 * @param client KtorのHTTPクライアント
 */
@HiltViewModel
class FirstViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val client: HttpClient
) : ViewModel() {

    var uiState by mutableStateOf(FirstUiState())
        private set
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * 在庫一覧を読み込む
     */
    fun load() {
        viewModelScope.launch {
            uiState = uiState.copy(running = true)

            runCatching {
                withContext(Dispatchers.IO) {
                    val endpoint = context.getString(R.string.api_endpoint)
                    val token = context.getString(R.string.api_token)

                    val responseText = client.get("$endpoint/api/v1/inventories") {
                        header("Authorization", "Bearer $token")
                    }.bodyAsText()

                    val root: JsonElement = json.parseToJsonElement(responseText)

                    // ルートが配列でもオブジェクトでもハンドリング
                    val array: JsonArray = when (root) {
                        is JsonArray -> root
                        is JsonObject -> {
                            root["inventories"]?.jsonArray
                                ?: root["data"]?.jsonArray
                                ?: root["items"]?.jsonArray
                                ?: JsonArray(emptyList())
                        }

                        else -> JsonArray(emptyList())
                    }

                    array.map { el ->
                        val obj = el.jsonObject
                        Inventory(
                            id = obj["id"]?.jsonPrimitive?.intOrNull ?: 0,
                            title = obj["title"]?.jsonPrimitive?.contentOrNull.orEmpty(),
                            quantity = obj["quantity"]?.jsonPrimitive?.contentOrNull.orEmpty()
                        )
                    }
                }
            }.onSuccess { list ->
                uiState = uiState.copy(items = list, itemsFiltered = list, running = false)
            }.onFailure { e ->
                uiState = uiState.copy(running = false, errorMessage = e.message ?: "Error")
            }
        }
    }

    /**
     * 検索クエリが変更されたときのfilter処理
     * @param newQuery 新しい検索クエリ
     */
    fun onQueryChange(newQuery: String) {
        uiState = uiState.copy(query = newQuery)
        uiState = uiState.copy(itemsFiltered = filteredItems())
    }

    /**
     * 検索クエリに基づいてアイテムをフィルタリングする
     * @return フィルタリングされたアイテムのリスト
     */
    private fun filteredItems(): List<Inventory> {
        val q = normalizeJa(uiState.query)
        return uiState.items.filter { it ->
            normalizeJa(it.title).contains(q) ||
                    normalizeJa(it.id.toString()).contains(q) ||
                    normalizeJa(it.quantity.orEmpty()).contains(q)
        }
    }

    /**
     * 日本語文字列の正規化を行う
     * @param s 正規化対象の文字列
     * @return 正規化された文字列
     */
    private fun normalizeJa(s: String): String {
        val nk = Normalizer.normalize(s, Normalizer.Form.NFKC).lowercase()
        val hira = buildString {
            nk.forEach { ch ->
                append(
                    when (ch) {
                        in 'ァ'..'ヶ' -> (ch.code - 0x60).toChar()
                        'ヴ' -> 'ゔ'
                        else -> ch
                    }
                )
            }
        }
        return hira.replace("\\s+".toRegex(), "")
    }
}
