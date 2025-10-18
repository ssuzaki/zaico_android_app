package jp.co.zaico.codingtest

data class FirstUiState(
    val query: String = "",
    val items: List<Inventory> = emptyList(),
    val itemsFiltered: List<Inventory> = emptyList(),
    val running: Boolean = false,
    val errorMessage: String = ""
)
