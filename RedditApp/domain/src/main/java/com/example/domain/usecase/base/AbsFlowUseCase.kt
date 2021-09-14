package com.example.domain.usecase.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.NotNull
import kotlin.coroutines.CoroutineContext


abstract class AbsFlowUseCase<Result, Params>() {

    protected abstract fun buildUseCase(@NotNull params: Params): Flow<Result>

    fun execute(
        coroutineScope: CoroutineScope,
        @NotNull listener: Request<Result>,
        @NotNull params: Params,
    ) {
                listener.onStart?.invoke()
                buildUseCase(params)
                    .onEach { listener.onComplete?.invoke(it) }
                    .catch { listener.onError?.invoke(it) }
                    .onCompletion { listener.onTerminate?.invoke() }
                    .launchIn(coroutineScope)
        }

}

