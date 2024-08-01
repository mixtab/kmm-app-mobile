package screen_welcome

import core.UiEffect
import core.UiEvent
import core.UiState

data object WelcomeScreenState : UiState

sealed interface WelcomeScreenEvent : UiEvent {

    data object OnStartClicked : WelcomeScreenEvent
}

sealed interface WelcomeScreenEffect : UiEffect {

    data object NavigateToProductSelection : WelcomeScreenEffect
}