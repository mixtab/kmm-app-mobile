package screen_product_selection

import core.UiEffect
import core.UiEvent
import core.UiState
import feature_products.Category
import feature_products.Product

data class ProductSelectionUiState(
    val productsByCategory: Map<Category, List<Product>> = emptyMap(),
    val selectedProductsCount: Int = 0
) : UiState

sealed interface ProductSelectionUiEvent : UiEvent {

    data object OnGenerateClicked : ProductSelectionUiEvent
    data class OnEnteredSearchChanged(val enteredSearch: String) : ProductSelectionUiEvent
    data class OnProductSelected(val product: Product) : ProductSelectionUiEvent
}

sealed interface ProductSelectionUiEffect : UiEffect {

    data class NavigateToReceipt(val products: List<Product>) : ProductSelectionUiEffect

}