package jp.co.zaico.codingtest.add

/**
 * アイテム追加画面のUIステート
 * @param title アイテムのタイトル
 * @param running 追加処理が実行中かどうか
 */
data class AddUiState(
    val title: String = "",
    val running: Boolean = false
)