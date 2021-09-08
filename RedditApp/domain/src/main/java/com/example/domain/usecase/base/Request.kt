package com.example.domain.usecase.base

open class Request<T> {
    var onComplete: ((T) -> Unit)? = null
    var onError: ((Throwable) -> Unit)? = null
    var onStart: (() -> Unit)? = null
    var onTerminate: (() -> Unit)? = null
}