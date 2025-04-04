package com.example.drizzle.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeoutException


fun <T> Flow<T>.getOrAwaitValueFromFlow(timeoutMillis: Long = 2000): Unit = runBlocking {
    try {
        withTimeout(timeoutMillis) {
            this@getOrAwaitValueFromFlow.first()
        }
    } catch (e: TimeoutException) {
        throw TimeoutException("Flow did not emit value within time.")
    }
}
