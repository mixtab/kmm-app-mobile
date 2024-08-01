import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import core.theme.ChefAiColor
import core.theme.ChefAiTheme
import feature_products.OpenAiRepository
import feature_products.ProductsRepository
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.dsl.module
import screen_product_selection.ProductSelectionScreenModel
import screen_recipe.ReceiptScreenModel
import screen_welcome.WelcomeScreenModel
import screen_welcome.WelcomeScreenRoute

@Composable
@Preview
fun KmmMapsApp() {
    KoinApplication(application = { modules(ChefAiModule) }) {
        ChefAiTheme {
            Box(modifier = Modifier.fillMaxSize().background(color = ChefAiColor.CodGray)) {
                Navigator(WelcomeScreenRoute()) {
                    SlideTransition(it)
                }
            }
        }
    }
}

private val ChefAiModule = module {
    factory { WelcomeScreenModel() }
    factory { ProductSelectionScreenModel(ProductsRepository) }
    factory { ReceiptScreenModel(OpenAiRepository) }
}
