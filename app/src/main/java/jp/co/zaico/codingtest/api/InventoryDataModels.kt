package jp.co.zaico.codingtest.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 在庫作成リクエストボディ
 */
@Serializable
data class InventoryRequestBody(
    /**
     * タイトル（必須）他は任意
     */
    val title: String,

    val quantity: String? = null,
    val unit: String? = null,
    val category: String? = null,
    val state: String? = null,
    val place: String? = null,
    val etc: String? = null,
    @SerialName("group_tag") val groupTag: String? = null,
    @SerialName("user_group") val userGroup: String? = null,      // カンマ区切り可
    val code: String? = null,
    @SerialName("item_image") val itemImageBase64: String? = null,
    @SerialName("stocktake_attributes") val stocktake: StocktakeAttributes? = null,
    @SerialName("optional_attributes") val optionalAttributes: List<OptionalAttribute>? = null,
    @SerialName("quantity_management_attributes") val quantityManagement: QuantityManagementAttributes? = null,
    @SerialName("inventory_history") val inventoryHistory: InventoryHistory? = null,
    // 単位換算系
    @SerialName("is_quantity_auto_conversion_by_unit")
    val isQuantityAutoConversionByUnit: String? = null, // "1" or "0"
    @SerialName("quantity_auto_conversion_by_unit_name")
    val quantityAutoConversionUnitName: String? = null,
    @SerialName("quantity_auto_conversion_by_unit_factor")
    val quantityAutoConversionUnitFactor: String? = null
)

@Serializable
data class StocktakeAttributes(
    // 例: "2018-03-27T09:38:19+09:00"（ISO8601）
    @SerialName("checked_at") val checkedAt: String
)

@Serializable
data class OptionalAttribute(
    val name: String,
    val value: String
)

@Serializable
data class QuantityManagementAttributes(
    @SerialName("order_point_quantity") val orderPointQuantity: String
)

@Serializable
data class InventoryHistory(
    val memo: String
)

/**
 * 在庫作成レスポンスボディ
 */
@Serializable
data class InventoryResponseBody(
    @SerialName("code") val code: Int,
    @SerialName("status") val status: String?,
    @SerialName("message") val message: String?,
    @SerialName("data_id") val dataId: Int
)
