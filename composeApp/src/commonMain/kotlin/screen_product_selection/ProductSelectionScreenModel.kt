package screen_product_selection

import cafe.adriel.voyager.core.model.screenModelScope
import core.MVIScreenModel
import core.extensions.Launched
import core.extensions.stateInMerge
import feature_products.Category
import feature_products.Product
import feature_products.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update


class ProductSelectionScreenModel(
    private val productsRepository: ProductsRepository
) : MVIScreenModel<ProductSelectionUiState, ProductSelectionUiEvent, ProductSelectionUiEffect>() {

    private val productsByCategoryState = MutableStateFlow(productsRepository.productsByCategory)

    private val selectedProductsFlow = productsByCategoryState.map { categoryWithProduct ->
        categoryWithProduct.flatMap { it.value }.filter { it.selected }
    }

    private val _uiState = MutableStateFlow(ProductSelectionUiState())
        .stateInMerge(screenModelScope, Launched.WhileSubscribed(),
            { productsByCategoryState.onEachToState(::mapProductsToUiState) },
            { selectedProductsFlow.onEachToState(::mapSelectedProductsToUiState) }
        )
    override val uiState = _uiState.asStateFlow()

    private fun mapProductsToUiState(
        state: ProductSelectionUiState,
        categoryWithProducts: Map<Category, List<Product>>
    ) = state.copy(productsByCategory = categoryWithProducts)

    private fun mapSelectedProductsToUiState(
        state: ProductSelectionUiState,
        products: List<Product>
    ) = state.copy(selectedProductsCount = products.size)

    override fun processUiEvent(event: ProductSelectionUiEvent): Unit = when (event) {
        is ProductSelectionUiEvent.OnEnteredSearchChanged -> filterProductsBySearch(event.enteredSearch)
        is ProductSelectionUiEvent.OnProductSelected -> selectProduct(event.product)
        is ProductSelectionUiEvent.OnGenerateClicked -> sendUiEffect(ProductSelectionUiEffect.NavigateToReceipt(
            productsByCategoryState.value.flatMap { it.value }.filter { it.selected }
        ))
    }

    private fun filterProductsBySearch(enteredSearch: String) {
        if (enteredSearch.isNotEmpty()) {
            _uiState.update { it.copy(productsByCategory = productsByCategoryState.value) }
        }

        val filteredProducts = productsByCategoryState.value.mapValues { (_, products) ->
            products.filter { it.name.contains(enteredSearch, true) }
        }.filterValues { it.isNotEmpty() }

        _uiState.update { it.copy(productsByCategory = filteredProducts) }
    }

    private fun selectProduct(selectedProduct: Product) {
        val categoryWithProducts = productsByCategoryState.value

        val updated = categoryWithProducts.mapValues { (_, products) ->
            products.map { product ->
                if (product.name == selectedProduct.name) {
                    product.copy(selected = !product.selected)
                } else {
                    product
                }
            }
        }

        productsByCategoryState.update { updated }
    }
}