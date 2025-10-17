package jp.co.zaico.codingtest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.co.zaico.codingtest.compose.ProgressIndicator

/**
 * 在庫詳細画面のコンテンツ
 * @param uiState 画面のUIステート
 */
@Composable
@Preview
fun SecondContents(
    uiState: SecondUiState = SecondUiState(
        id = 12345678,
        imageResId = R.drawable.no_image,
        title = "サンプルアイテム",
        quantity = "10"
    )
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // === Row: ID ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "ID", color = Color.White)
            Spacer(Modifier.width(16.dp))
            Text(text = uiState.id.toString(), color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), thickness = 1.dp)

        // === Row: 在庫画像 ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "在庫画像", color = Color.White)
            Spacer(Modifier.width(16.dp))
            Image(
                painter = painterResource(id = uiState.imageResId),
                contentDescription = "在庫画像",
                modifier = Modifier.size(64.dp)
            )
        }

        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), thickness = 1.dp)

        // === Row: タイトル ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "タイトル", color = Color.White)
            Spacer(Modifier.width(16.dp))
            Text(text = uiState.title, color = Color.White, style = MaterialTheme.typography.bodyLarge)
        }

        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), thickness = 1.dp)

        // === Row: 数量 ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "数量", color = Color.White)
            Spacer(Modifier.width(16.dp))
            Text(text = uiState.quantity, color = Color.White, style = MaterialTheme.typography.bodyLarge)
        }

        // 通信中Progress表示
        if (uiState.running) {
            ProgressIndicator()
        }
    }
}
