package screen_welcome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import core.components.ChefAiButton
import core.theme.ChefAiColor
import core.theme.ChefAiTextStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

class WelcomeScreenRoute : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<WelcomeScreenModel>()
        val state by screenModel.uiState.collectAsState()
        WelcomeScreen(state = state, onUiEvent = screenModel::onUiEvent)
        WelcomeScreenEffectHandler(screenModel.effect)
    }
}

@Composable
private fun WelcomeScreen(
    state: WelcomeScreenState,
    onUiEvent: (WelcomeScreenEvent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = "Welcome to\nСhef AI",
            style = ChefAiTextStyle.h1,
            color = ChefAiColor.MySin,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Welcome to Food GPT Welcome to Food GPT Welcome to Food GPT",
            fontSize = 16.sp,
            color = ChefAiColor.WildSand,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.weight(1F))
        ChefAiButton(
            text = "START NOW!",
            onClick = { onUiEvent(WelcomeScreenEvent.OnStartClicked) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(40.dp))
    }
}

@Composable
@Preview
private fun WelcomeScreenPreview() {
     WelcomeScreen(state = WelcomeScreenState, onUiEvent = {})
}