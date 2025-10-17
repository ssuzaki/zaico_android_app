package jp.co.zaico.codingtest.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * 在庫APIサービス
 */
interface InventoryService {
    @Headers("Content-Type: application/json")
    @POST("api/v1/inventories")
    suspend fun createInventory(
        @Body body: InventoryRequestBody
    ): Response<InventoryResponseBody>
}
