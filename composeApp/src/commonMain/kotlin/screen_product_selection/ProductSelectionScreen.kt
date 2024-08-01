package screen_product_selection

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import chef_ai_mobile.composeapp.generated.resources.Res
import chef_ai_mobile.composeapp.generated.resources.ic_selected_products_count
import core.components.ChefAiButton
import core.components.ChefAiTextField
import core.extensions.clickableNoRipple
import core.theme.ChefAiColor
import core.theme.ChefAiTextStyle
import feature_products.Category
import feature_products.Product
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class ProductSelectionScreenRoute : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<ProductSelectionScreenModel>()
        val state by screenModel.uiState.collectAsState()
        ProductSelectionScreen(state = state, onUiEvent = screenModel::onUiEvent)
        ProductSelectionScreenEffectHandler(screenModel.effect)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ProductSelectionScreen(
    state: ProductSelectionUiState,
    onUiEvent: (ProductSelectionUiEvent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(87.dp))
        Text(
            text = "Choose Products",
            style = ChefAiTextStyle.h2,
            color = ChefAiColor.MySin,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Now add the products you have in your fridge",
            fontSize = 16.sp,
            color = ChefAiColor.WildSand,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        var enteredSearch by remember { mutableStateOf("") }
        ChefAiTextField(
            value = enteredSearch,
            hint = "Search",
            onValueChange = {
                enteredSearch = it
                onUiEvent(ProductSelectionUiEvent.OnEnteredSearchChanged(it))
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                state.productsByCategory.keys.forEachIndexed { index, category ->
                    val topPadding = if (index == 0) 24.dp else 20.dp
                    val bottomPadding =
                        if (index == state.productsByCategory.keys.size - 1) 24.dp else 0.dp
                    LazyRow(
                        state = rememberLazyListState(),
                        contentPadding = PaddingValues(
                            start = 24.dp,
                            top = topPadding,
                            bottom = bottomPadding
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(items = state.productsByCategory[category]!!) { index, product ->
                            val backgroundColor =
                                if (product.selected) Color(product.color) else ChefAiColor.MineShaftSolid
                            val textColor =
                                if (product.selected) ChefAiColor.CodGray else Color.White
                            val shape =
                                if (index % 2 == 0) RoundedCornerShape(100.dp) else RectangleShape

                            Text(
                                text = "${product.icon} ${product.name.uppercase()}",
                                color = textColor,
                                style = ChefAiTextStyle.body.copy(fontWeight = FontWeight.ExtraBold),
                                modifier = Modifier
                                    .background(color = backgroundColor, shape = shape)
                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                                    .clickableNoRipple {
                                        onUiEvent(
                                            ProductSelectionUiEvent.OnProductSelected(
                                                product
                                            )
                                        )
                                    }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
            androidx.compose.animation.AnimatedVisibility(
                visible = state.selectedProductsCount > 0,
                enter = slideInHorizontally(initialOffsetX = { it }),
                exit = slideOutHorizontally(targetOffsetX = { it }),
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Box(Modifier.padding(bottom = 24.dp, end = 24.dp)) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .zIndex(1F)
                            .align(Alignment.TopEnd)
                            .size(20.dp)
                            .background(shape = CircleShape, color = ChefAiColor.MySin)
                            .border(width = 2.dp, color = ChefAiColor.CodGray, shape = CircleShape)
                    ) {
                        Text(
                            text = state.selectedProductsCount.toString(),
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.ExtraBold),
                            color = ChefAiColor.CodGray
                        )
                    }
                    Image(
                        painter = painterResource(Res.drawable.ic_selected_products_count),
                        contentDescription = null
                    )
                }
            }
        }
        ChefAiButton(
            text = "GENERATE RECIPE",
            onClick = { onUiEvent(ProductSelectionUiEvent.OnGenerateClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
@Preview
private fun ProductSelectionScreenPreview() {
    ProductSelectionScreen(
        state = ProductSelectionUiState(
            productsByCategory = mapOf(
                Category.DAIRY_AND_EGGS to listOf(
                    Product("Milk", "🥛", 0xFFFFFFFF),
                    Product("Cheese", "🧀", 0xFFFFD700),
                    Product("Egg", "🥚", 0xFFFFFFE0),
                    Product("Butter", "🧈", 0xFFFFFACD),
                    Product("Yogurt", "🥄", 0xFFFFFFFF)
                ),
                Category.MEAT_AND_POULTRY to listOf(
                    Product("Steak", "🥩", 0xFF8B0000),
                    Product("Chicken leg", "🍗", 0xFFDAA520),
                    Product("Bacon", "🥓", 0xFFFF6347),
                    Product("Generic meat cut", "🍖", 0xFF8B0000)
                ),
                Category.SEAFOOD to listOf(
                    Product("Fish", "🐟", 0xFF1E90FF),
                    Product("Shrimp", "🍤", 0xFFFF6347),
                    Product("Octopus", "🐙", 0xFF8A2BE2)
                )
            )
        ),
        onUiEvent = {}
    )
}