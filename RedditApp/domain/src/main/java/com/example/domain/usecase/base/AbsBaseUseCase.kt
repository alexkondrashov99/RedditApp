package com.example.domain.usecase.base

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jetbrains.annotations.NotNull
import kotlin.coroutines.CoroutineContext

abstract class AbsBaseUseCase<Result, Params>() {

    protected abstract suspend fun buildUseCase(@NotNull params: Params): Result

    open fun execute(
        coroutineScope: CoroutineScope,
        @NotNull params: Params,
        @NotNull listener: Request<Result>,

    ) {

        val handler = CoroutineExceptionHandler { _, exception ->
            listener.onError?.invoke(exception)

            //finally
            listener.onTerminate?.invoke()
        }
        coroutineScope.launch(handler) {
            val result = buildUseCase(params)
            listener.onComplete?.invoke(result)
            listener.onTerminate?.invoke()
        }

    }
}