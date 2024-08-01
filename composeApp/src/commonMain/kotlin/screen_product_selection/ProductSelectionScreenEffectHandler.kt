package screen_product_selection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.Flow
import screen_recipe.ReceiptScreenRoute

@Composable
fun ProductSelectionScreenEffectHandler(
    effectFlow: Flow<ProductSelectionUiEffect>
) {
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                is ProductSelectionUiEffect.NavigateToReceipt -> {
                    navigator.push(ReceiptScreenRoute(effect.products))
                }
            }
        }
    }
}