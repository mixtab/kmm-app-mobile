package screen_welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.Flow
import screen_product_selection.ProductSelectionScreenRoute

@Composable
fun WelcomeScreenEffectHandler(
    effectFlow: Flow<WelcomeScreenEffect>,
) {
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                WelcomeScreenEffect.NavigateToProductSelection -> navigator.push(
                    ProductSelectionScreenRoute()
                )
            }
        }
    }
}