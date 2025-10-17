package jp.co.zaico.codingtest

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

@HiltViewModel
class SecondViewModel @Inject constructor (
    @ApplicationContext private val context: Context,
    private val client: HttpClient
) : ViewModel() {

    var uiState by mutableStateOf(SecondUiState())
        private set

    fun load(inventoryId: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(running = true)

            runCatching {
                val url =
                    "${context.getString(R.string.api_endpoint)}/api/v1/inventories/$inventoryId"
                val jsonText = client.get(url) {
                    header("Authorization", "Bearer ${context.getString(R.string.api_token)}")
                }.bodyAsText()

                val obj = Json.parseToJsonElement(jsonText).jsonObject
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

//    // データ取得
//    @SuppressLint("DefaultLocale")
//    fun getInventory(inventoryId: Int): Inventory = runBlocking {
//
//        val client = HttpClient(Android) {
//            install(ContentNegotiation) {
//                json(Json { ignoreUnknownKeys = true })
//            }
//        }
//
//        return@runBlocking GlobalScope.async {
//            val response: HttpResponse = client!!.get(
//                String.format(
//                    "%s/api/v1/inventories/%d",
//                    context.getString(R.string.api_endpoint),
//                    inventoryId
//                )
//            ) {
//                header("Authorization", String.format("Bearer %s", context.getString(R.string.api_token)))
//            }
//
//            val jsonText = response.bodyAsText()
//            val json = Json.parseToJsonElement(jsonText).jsonObject
//
//            return@async Inventory(
//                id = json["id"].toString().replace(""""""", "").toInt(),
//                title = json["title"].toString().replace(""""""", ""),
//                quantity = json["quantity"].toString().replace(""""""", "")
//            )
//
//        }.await()
//    }
}
