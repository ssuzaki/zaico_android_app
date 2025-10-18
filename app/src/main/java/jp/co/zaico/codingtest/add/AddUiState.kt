package jp.co.zaico.codingtest.add

/**
 * アイテム追加画面のUIステート
 * @param title アイテムのタイトル
 * @param running 追加処理が実行中かどうか
 * @param successMessage 成功メッセージ
 * @param errorMessage エラーメッセージ
 */
data class AddUiState(
    val title: String = "",
    val running: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
