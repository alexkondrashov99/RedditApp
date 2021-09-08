package com.example.domain.usecase.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NotNull
import kotlin.coroutines.CoroutineContext


abstract class AbsFlowUseCase<Result, Params>(private val coroutineScope: CoroutineScope) {

    protected abstract fun buildUseCase(@NotNull params: Params): Flow<Result>

    fun execute(
        @NotNull listener: Request<Result>,
        @NotNull params: Params,
    ) {
        coroutineScope.launch {
            listener.onStart?.invoke()
            buildUseCase(params)
                //.flowOn(coroutineContext)
                .onEach { listener.onComplete?.invoke(it) }
                .catch { listener.onError?.invoke(it) }
                .onCompletion { listener.onTerminate?.invoke() }
                .collect()
        }
    }
}

