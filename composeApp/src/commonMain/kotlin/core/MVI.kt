package core

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val TAG_STATE = "ScreenState"
private const val TAG_EVENT = "ScreenEvent"
private const val TAG_EFFECT = "ScreenEffect"

interface UiState
interface UiEvent
interface UiEffect

abstract class MVIScreenModel<State : UiState, Event : UiEvent, Effect : UiEffect> : ScreenModel {

    abstract val uiState: StateFlow<State>
    protected val currentState get() = uiState.value

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    protected abstract fun processUiEvent(event: Event)

    fun onUiEvent(event: Event) {
        processUiEvent(event)
    }

    protected fun sendUiEffect(effect: Effect) {
        screenModelScope.launch {
            _effect.send(effect)
        }
    }
}