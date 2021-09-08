package com.example.domain.usecase.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.NotNull
import kotlin.coroutines.CoroutineContext

abstract class AbsBaseUseCase<Result, Params>(private val coroutineScope: CoroutineScope) {

    protected abstract suspend fun buildUseCase(@NotNull params: Params): Result

    open fun execute(
        @NotNull params: Params,
        onComplete: ((Result) -> Unit)? = null
    ) {
        coroutineScope.launch {
                val result = buildUseCase(params)
                /* invoking some lambda from viewmodel */
                onComplete?.invoke(result)
        }
    }
}