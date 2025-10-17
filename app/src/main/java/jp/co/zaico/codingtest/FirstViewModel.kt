package jp.co.zaico.codingtest

import android.content.Context
import androidx.lifecycle.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.int

class FirstViewModel(
    private val context: Context
) : ViewModel() {

    // Ktorクライアントは再利用（リーク防止のためViewModelのライフサイクルに紐づける）
    private val client: HttpClient by lazy {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * 在庫一覧を取得（UIスレッド非ブロック）
     * - トップが JsonArray でも JsonObject でも安全に処理
     * - JsonObject のときは "inventories" / "data" / "items" のいずれかを配列として読む
     */
    suspend fun getInventories(): List<Inventory> = withContext(Dispatchers.IO) {
        val endpoint = context.getString(R.string.api_endpoint)
        val token = context.getString(R.string.api_token)

        val responseText = client.get("$endpoint/api/v1/inventories") {
            header("Authorization", "Bearer $token")
        }.bodyAsText()

        val root: JsonElement = json.parseToJsonElement(responseText)

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
                id = obj["id"]?.jsonPrimitive?.int ?: 0,
                title = obj["title"]?.jsonPrimitive?.contentOrNull.orEmpty(),
                quantity = obj["quantity"]?.jsonPrimitive?.contentOrNull.orEmpty()
            )
        }
    }
}
