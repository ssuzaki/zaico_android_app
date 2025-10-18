package jp.co.zaico.codingtest.first

import kotlinx.serialization.Serializable

/**
 * 在庫データモデル
 * @param id 在庫ID
 * @param title 在庫タイトル
 * @param quantity 在庫数量
 */
@Serializable
data class Inventory(
    val id: Int,
    val title: String,
    val quantity: String
)
