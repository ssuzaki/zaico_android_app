package jp.co.zaico.codingtest

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import jp.co.zaico.codingtest.api.InventoryRepository
import jp.co.zaico.codingtest.api.InventoryRequestBody
import jp.co.zaico.codingtest.api.InventoryService
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

/**
 * InventoryRepository のネットワーク関連の単体テスト
 */
class InventoryRepositoryNetworkTest {

    private lateinit var server: MockWebServer
    private lateinit var api: InventoryService
    private lateinit var repo: InventoryRepository

    @Before
    fun setup() {
        server = MockWebServer().apply { start() }
        val json = Json { ignoreUnknownKeys = true; explicitNulls = false }
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
        api = retrofit.create(InventoryService::class.java)
        repo = InventoryRepository(api)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    /**
     * HTTP 200 かつ body.status が "success" のときは成功扱い
     */
    @Test
    fun createSuccess_whenHttp200AndStatusSuccess() = runBlocking {
        val body = """
          { "code":200, "status":"success", "message":"ok", "data_id":123456 }
        """.trimIndent()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type","application/json")
                .setBody(body)
        )

        val result = repo.create(InventoryRequestBody(title = "X"))
        assertTrue(result.isSuccess)

        val res = result.getOrThrow()          // InventoryResponseBody
        assertEquals(123456, res.dataId)       // ← dataId を検証
        assertEquals(200, res.code)
        assertEquals("success", res.status)

        val recorded = server.takeRequest()
        assertEquals("/api/v1/inventories", recorded.path)
        assertEquals("POST", recorded.method)
        assertTrue(recorded.body.readUtf8().contains("\"title\":\"X\""))
    }

    /**
     * HTTP 500 のときは失敗扱い
     */
    @Test
    fun createFailure_whenHttp500() = runBlocking {
        server.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setHeader("Content-Type","application/json")
                .setBody("""{"message":"ng"}""")
        )
        val result = repo.create(InventoryRequestBody(title = "X"))
        assertTrue(result.isFailure)
    }

    /**
     * HTTP 200 でも body.status が "success" でないときは失敗扱い
     */
    @Test
    fun createFailure_whenBodyStatusNotSuccess() = runBlocking {
        val body = """
          { "code":400, "status":"error", "message":"bad req" }
        """.trimIndent()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type","application/json")
                .setBody(body)
        )
        val result = repo.create(InventoryRequestBody(title = "X"))
        assertTrue(result.isFailure)
    }
}
