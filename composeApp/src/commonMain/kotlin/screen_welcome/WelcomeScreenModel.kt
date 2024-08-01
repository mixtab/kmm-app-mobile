package screen_welcome

import core.MVIScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WelcomeScreenModel :
    MVIScreenModel<WelcomeScreenState, WelcomeScreenEvent, WelcomeScreenEffect>() {

    override val uiState = MutableStateFlow(WelcomeScreenState).asStateFlow()

    override fun processUiEvent(event: WelcomeScreenEvent): Unit = when (event) {
        WelcomeScreenEvent.OnStartClicked -> sendUiEffect(WelcomeScreenEffect.NavigateToProductSelection)
    }
}