package jp.co.zaico.codingtest.second

import androidx.annotation.DrawableRes
import jp.co.zaico.codingtest.R

/**
 * Second画面のUIステート
 * @param id アイテムのID
 * @param imageResId アイテムの画像URL
 * @param title アイテムのタイトル
 * @param quantity アイテムの数量
 * @param running 処理が実行中かどうか
 * @param errorMessage エラーメッセージ
 */
data class SecondUiState(
    val id: Int = 0,
    @DrawableRes val imageResId: Int = R.drawable.no_image,
    val title: String = "-",
    val quantity: String = "-",
    val running: Boolean = false,
    val errorMessage: String = ""
)
