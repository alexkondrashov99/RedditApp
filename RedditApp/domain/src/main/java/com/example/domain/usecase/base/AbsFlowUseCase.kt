package com.example.domain.usecase

import org.jetbrains.annotations.NotNull
import kotlin.coroutines.CoroutineContext


abstract class AbsFlowUseCase<Result, Params>(private val coroutineContext: CoroutineContext) :
    AbsBaseFlowUseCase<Params, Result>() {

    suspend fun execute(
        @NotNull listener: Request<Result>,
        @NotNull params: Params
    ) {
        listener.onStart?.invoke()
        buildUseCase(params)
            .flowOn(coroutineContext)
            .onEach { listener.onComplete?.invoke(it) }
            .catch { listener.onError?.invoke(it) }
            .onCompletion { listener.onTerminate?.invoke() }
            .collect()
    }

}