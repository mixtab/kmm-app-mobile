package screen_recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import core.theme.ChefAiColor
import core.theme.ChefAiTextStyle
import feature_products.Product

data class ReceiptScreenRoute(
    private val products: List<Product>
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<ReceiptScreenModel>()
        val state by screenModel.uiState.collectAsState()
        LaunchedEffect(Unit) {
            screenModel.loadReceipt(products)
        }
        ReceiptScreen(state = state, onUiEvent = screenModel::onUiEvent)
        ReceiptScreenEffectHandler(screenModel.effect)
    }
}


@Composable
private fun ReceiptScreen(state: ReceiptScreenUiState, onUiEvent: (ReceiptScreenUiEvent) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        if (state.receipt == null) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(color = ChefAiColor.MySin)
            }
            return
        }
        Spacer(modifier = Modifier.height(87.dp))
        Text(
            text = state.receipt.name,
            style = ChefAiTextStyle.h2,
            color = ChefAiColor.MySin,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = ChefAiColor.MineShaftSolid, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Ingridients",
                style = ChefAiTextStyle.button,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(18.dp))
            state.receipt.ingredients.forEach { ingredient ->
                Text(
                    text = "* $ingredient",
                    style = ChefAiTextStyle.body,
                    color = ChefAiColor.SilverChalice,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = ChefAiColor.MineShaftSolid, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Instructions:",
                style = ChefAiTextStyle.body,
                color = ChefAiColor.SilverChalice,
            )
            state.receipt.instructions.forEachIndexed { index, instruction ->
                Text(
                    text = "${index.inc()}. $instruction",
                    style = ChefAiTextStyle.body,
                    color = ChefAiColor.SilverChalice,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
    }
}