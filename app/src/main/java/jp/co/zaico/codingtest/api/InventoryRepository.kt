package jp.co.zaico.codingtest.api

import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.HttpException

/**
 * 在庫リポジトリ
 * @param service 在庫APIサービス
 */
@Singleton
class InventoryRepository @Inject constructor(
    private val service: InventoryService
) {
    /**
     * シンプルな在庫アイテムを作成する
     * @param title アイテムのタイトル
     * @return 在庫レスポンスボディ
     */
    suspend fun createSimple(title: String): Result<InventoryResponseBody> =
        create(InventoryRequestBody(title = title))

    /**
     * 在庫アイテムを作成する
     * @param req 在庫リクエストボディ
     * @return 在庫レスポンスボディ
     */
    suspend fun create(req: InventoryRequestBody): Result<InventoryResponseBody> = runCatching {
        val resp = service.createInventory(req) // Response<InventoryResponseBody> であること！
        if (!resp.isSuccessful) throw HttpException(resp)

        val body = resp.body() ?: error("Empty body")

        // ★ null-safe に判定
        val ok = body.code == 200 && (body.status?.equals("success", ignoreCase = true) == true)
        if (!ok) {
            val st = body.status ?: "null"
            val msg = body.message ?: "Server returned code=${body.code}, status=$st"
            error(msg)
        }
        body
    }
}
