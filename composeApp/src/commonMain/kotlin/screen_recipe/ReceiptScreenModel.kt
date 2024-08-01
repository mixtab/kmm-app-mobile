package screen_recipe

import cafe.adriel.voyager.core.model.screenModelScope
import core.MVIScreenModel
import feature_products.OpenAiRepository
import feature_products.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReceiptScreenModel(
    private val repository: OpenAiRepository
) : MVIScreenModel<ReceiptScreenUiState, ReceiptScreenUiEvent, ReceiptScreenUiEffect>() {

    private val _uiState = MutableStateFlow(ReceiptScreenUiState())
    override val uiState = _uiState.asStateFlow()

    fun loadReceipt(products: List<Product>) {
        screenModelScope.launch {
            repository.request(products).onSuccess { receipt ->
                _uiState.update { it.copy(receipt = receipt) }
            }.onFailure {
                println(it)
            }
        }
    }

    override fun processUiEvent(event: ReceiptScreenUiEvent) {

    }
}