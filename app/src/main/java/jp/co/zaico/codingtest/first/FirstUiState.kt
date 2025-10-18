package jp.co.zaico.codingtest.first

/**
 * アイテム一覧画面のUIステート
 * @param query 検索クエリ
 * @param items アイテム一覧
 * @param itemsFiltered 検索結果のアイテム一覧
 * @param running アイテム取得処理が実行中かどうか
 * @param errorMessage エラーメッセージ
 */
data class FirstUiState(
    val query: String = "",
    val items: List<Inventory> = emptyList(),
    val itemsFiltered: List<Inventory> = emptyList(),
    val running: Boolean = false,
    val errorMessage: String = ""
)
