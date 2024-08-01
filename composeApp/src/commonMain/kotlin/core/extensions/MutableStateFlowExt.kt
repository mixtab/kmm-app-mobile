package core.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

fun <T> MutableStateFlow<T>.stateInMerge(
    scope: CoroutineScope,
    launched: Launched,
    vararg flows: StateInMergeContext<T>.() -> Flow<*>,
): MutableStateFlow<T> =
    MutableStateFlowWithStateInMerge(
        scope = scope,
        state = this,
        launched = launched,
        lambdas = flows,
    )

interface StateInMergeContext<T> {

    val state: MutableStateFlow<T>
    fun <R> Flow<R>.onEachToState(mapper: suspend (T, R) -> T): Flow<R>
}

sealed interface Launched {

    data object Eagerly : Launched
    data class WhileSubscribed(val stopTimeoutMillis: Long = 0L) : Launched
    data object Lazily : Launched
}

@OptIn(ExperimentalCoroutinesApi::class)
private class MutableStateFlowWithStateInMerge<T>(
    private val scope: CoroutineScope,
    launched: Launched,
    private val state: MutableStateFlow<T>,
    lambdas: Array<out StateInMergeContext<T>.() -> Flow<*>>,
) : MutableStateFlow<T> by state {

    private val context: StateInMergeContext<T> = object :
        StateInMergeContext<T> {
        override val state: MutableStateFlow<T>
            get() = this@MutableStateFlowWithStateInMerge

        override fun <R> Flow<R>.onEachToState(mapper: suspend (T, R) -> T): Flow<R> =
            onEach { value -> state.update { state -> mapper(state, value) } }
    }

    private val flows: List<Flow<*>> = lambdas.map { produceFlow -> produceFlow(context) }

    init {
        when (launched) {
            Launched.Eagerly -> launchAll()
            Launched.Lazily -> scope.launch {
                waitForFirstSubscriber()
                launchAll()
            }

            is Launched.WhileSubscribed -> {
                var jobs: Array<Job> = emptyArray()
                state.subscriptionCount
                    .map { it > 0 }
                    .distinctUntilChanged()
                    .flatMapLatest { subscribed ->
                        flow<Unit> {
                            when {
                                subscribed && jobs.isEmpty() -> jobs = launchAll()
                                subscribed -> launchInactive(jobs)
                                !subscribed && jobs.isNotEmpty() -> {
                                    delay(launched.stopTimeoutMillis)
                                    jobs.cancelActive()
                                }
                            }
                        }
                    }
                    .launchIn(scope)
            }
        }
    }

    private suspend fun waitForFirstSubscriber() {
        state.subscriptionCount.first { it > 0 }
    }

    private fun launchAll(): Array<Job> = flows
        .map { flow -> flow.launchIn(scope) }
        .toTypedArray()

    private fun launchInactive(jobs: Array<Job>) {
        check(jobs.size == flows.size)
        jobs.forEachIndexed { index, job ->
            if (job.isCancelled || job.isCompleted) jobs[index] = flows[index].launchIn(scope)
        }
    }

    private suspend fun Array<Job>.cancelActive() {
        forEach { job -> if (job.isActive) job.cancelAndJoin() }
    }
}