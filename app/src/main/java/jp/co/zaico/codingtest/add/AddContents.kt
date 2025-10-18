package jp.co.zaico.codingtest.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.co.zaico.codingtest.compose.ProgressIndicator

/**
 * アイテム追加画面のコンテンツ
 * @param uiState 画面のUIステート
 * @param onSubmit 追加ボタン押下時のコールバック
 * @param onDialogDismiss ダイアログが閉じられたときのコールバック
 */
@Composable
@Preview
fun AddContents(
    uiState: AddUiState = AddUiState(),
    onSubmit: (String) -> Unit = {},
    onDialogDismiss: () -> Unit = {}
) {
    var text by rememberSaveable { mutableStateOf("") }
    val canSubmit = text.isNotBlank() && !uiState.running

    // 追加結果ダイアログ（成功／失敗）
    val dialogMsg = uiState.errorMessage ?: uiState.successMessage
    if (dialogMsg?.isNotEmpty() == true) {
        AlertDialog(
            onDismissRequest = { onDialogDismiss() },
            text = { Text(text = dialogMsg) },
            confirmButton = {
                Button(
                    onClick = { onDialogDismiss() }) {
                    Text("OK")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { if (it.length <= 20) text = it }, // 最大20文字まで
                label = { Text("タイトル") },
                placeholder = { Text("在庫名など") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { /* キーボードの完了ボタン押下時の処理 */ }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    onSubmit(text)
                },
                enabled = canSubmit,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("追加")
            }
        }

        // 通信中Progress表示
        if (uiState.running) {
            ProgressIndicator()
        }
    }
}
