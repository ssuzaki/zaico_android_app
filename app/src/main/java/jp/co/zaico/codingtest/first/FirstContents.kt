package jp.co.zaico.codingtest.first

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import jp.co.zaico.codingtest.compose.ProgressIndicator

/**
 * 在庫一覧画面のコンテンツ部
 * @param uiState UI状態
 * @param query 検索クエリ
 * @param onQueryChange 検索クエリ変更時コールバック
 * @param onSearch 検索実行時コールバック
 * @param onItemClick 在庫アイテムクリック時コールバック
 * @param onRefresh 更新実行時コールバック
 */
@Composable
@Preview
fun FirstContents(
    uiState: FirstUiState = FirstUiState(),
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    onItemClick: (Inventory) -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        SearchBarCard(
            query = query,
            onQueryChange = onQueryChange,
            onClear = { onQueryChange("") },
            onSearch = onSearch,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // ?attr/actionBarSize 相当
        )

        Spacer(Modifier.height(16.dp))

        val refreshing = uiState.running
        val swipeState = rememberSwipeRefreshState(refreshing)
        SwipeRefresh(state = swipeState, onRefresh = onRefresh) {

            // リスト部（RecyclerView → LazyColumn）
            Box(Modifier.fillMaxSize()) {
                when {
                    uiState.running -> {
                        ProgressIndicator()
                    }

                    uiState.errorMessage.isNotEmpty() -> {
                        Text(
                            text = uiState.errorMessage ?: "Error",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            items(
                                items = uiState.itemsFiltered,
                                key = { it.id }
                            ) { item ->
                                InventoryListRow(
                                    idText = item.id.toString(),
                                    titleText = item.title,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onItemClick(item) }
                                        .padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 検索バー風Card
 * @param query 検索クエリ
 * @param onQueryChange 検索クエリ変更時コールバック
 * @param onClear クリアボタン押下時コールバック
 * @param onSearch 検索実行時コールバック
 * @param modifier 修飾子
 */
@Composable
fun SearchBarCard(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Card(modifier) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxSize(),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
            placeholder = { Text("検索", fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "検索") },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClear) {
                        Icon(Icons.Default.Close, contentDescription = "クリア")
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus() // キーボードを閉じる
                    onSearch()
                }
            ),
            colors = TextFieldDefaults.colors(
                // TextInputLayoutの透明背景っぽさ
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}

/**
 * 在庫リストの1行分
 * @param idText ID文字列
 * @param titleText タイトル文字列
 * @param modifier 修飾子
 */
@Composable
fun InventoryListRow(
    idText: String,
    titleText: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = idText,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.alignByBaseline()
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = titleText,
            fontSize = 14.sp,
            color = Color.White,
            // 長文タイトルでも1行で省略したい場合は以下を有効化
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.alignByBaseline()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InventoryListRowPreview() {
    InventoryListRow(
        idText = "12345678",
        titleText = "title",
        modifier = Modifier.background(Color.Black)
    )
}
