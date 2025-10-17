package jp.co.zaico.codingtest

import jp.co.zaico.codingtest.api.InventoryHistory
import jp.co.zaico.codingtest.api.InventoryRequestBody
import jp.co.zaico.codingtest.api.InventoryResponseBody
import jp.co.zaico.codingtest.api.OptionalAttribute
import jp.co.zaico.codingtest.api.QuantityManagementAttributes
import jp.co.zaico.codingtest.api.StocktakeAttributes
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 在庫データモデルのシリアライズ/デシリアライズ単体テスト
 */
class InventoryModelsTest {

    // 本番設定に合わせる：nullを出力しない、未知キーは無視
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    /** 最小のリクエスト(必須だけ)が期待通りのJSONになる */
    @Test
    fun serialize_minimalRequest_onlyTitle() {
        val req = InventoryRequestBody(title = "在庫A")
        val actual = json.encodeToString(req)
        val expected = """{"title":"在庫A"}"""
        assertJsonEquals(expected, actual)
    }

    /** null項目はJSONに含まれない（explicitNulls=false） */
    @Test
    fun serialize_doesNotEmitNulls() {
        val req = InventoryRequestBody(
            title = "在庫B",
            quantity = null,
            unit = null,
            optionalAttributes = null
        )
        val obj = json.parseToJsonElement(json.encodeToString(req)) as JsonObject
        assertTrue(obj.containsKey("title"))
        assertFalse(obj.containsKey("quantity"))
        assertFalse(obj.containsKey("unit"))
        assertFalse(obj.containsKey("optional_attributes"))
    }

    /** @SerialName 指定のキーでシリアライズされる(ネスト含む) */
    @Test
    fun serialize_usesSerialNamesForNested() {
        val req = InventoryRequestBody(
            title = "在庫C",
            groupTag = "tag-1",
            userGroup = "u1,u2",
            itemImageBase64 = "BASE64",
            stocktake = StocktakeAttributes(checkedAt = "2018-03-27T09:38:19+09:00"),
            optionalAttributes = listOf(
                OptionalAttribute("color", "red"),
                OptionalAttribute("size", "M")
            ),
            quantityManagement = QuantityManagementAttributes(orderPointQuantity = "10"),
            inventoryHistory = InventoryHistory(memo = "first")
        )
        val obj = json.parseToJsonElement(json.encodeToString(req)) as JsonObject

        assertEquals(JsonPrimitive("tag-1"), obj["group_tag"])
        assertEquals(JsonPrimitive("u1,u2"), obj["user_group"])
        assertEquals(JsonPrimitive("BASE64"), obj["item_image"])

        val stocktake = obj["stocktake_attributes"] as JsonObject
        assertEquals(JsonPrimitive("2018-03-27T09:38:19+09:00"), stocktake["checked_at"])

        val qm = obj["quantity_management_attributes"] as JsonObject
        assertEquals(JsonPrimitive("10"), qm["order_point_quantity"])

        val opt = obj["optional_attributes"] as? kotlinx.serialization.json.JsonArray
        assertNotNull(opt)
        assertEquals(2, opt!!.size)
        val first = opt[0] as JsonObject
        assertEquals(JsonPrimitive("color"), first["name"])
        assertEquals(JsonPrimitive("red"), first["value"])
    }

    /** レスポンスの最小構成をデコードできる（status/messageはnullable） */
    @Test
    fun deserialize_response_minimal() {
        val body = """{"code":200,"data_id":123456}"""
        val res = json.decodeFromString<InventoryResponseBody>(body)
        assertEquals(200, res.code)
        assertEquals(123456, res.dataId)
        assertNull(res.status)
        assertNull(res.message)
    }

    /** フルレスポンスをデコードでき、値が一致する */
    @Test
    fun deserialize_response_full() {
        val body = """
          {"code":200,"status":"success","message":"ok","data_id":987}
        """.trimIndent()
        val res = json.decodeFromString<InventoryResponseBody>(body)
        assertEquals(200, res.code)
        assertEquals("success", res.status)
        assertEquals("ok", res.message)
        assertEquals(987, res.dataId)
    }

    // ---------- helpers ----------

    /** JSON文字列の「意味的等価」を比較（キー順に依存しない） */
    private fun assertJsonEquals(expected: String, actual: String) {
        val e: JsonElement = json.parseToJsonElement(expected)
        val a: JsonElement = json.parseToJsonElement(actual)
        assertEquals(e, a)
    }
}
