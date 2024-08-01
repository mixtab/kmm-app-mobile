package screen_recipe

import core.UiEffect
import core.UiEvent
import core.UiState
import feature_products.Receipt

data class ReceiptScreenUiState(val receipt: Receipt? = null) : UiState

sealed interface ReceiptScreenUiEvent : UiEvent

sealed interface ReceiptScreenUiEffect : UiEffect