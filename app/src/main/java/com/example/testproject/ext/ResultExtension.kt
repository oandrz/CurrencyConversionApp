package com.example.testproject.ext

import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import timber.log.Timber

inline fun <R> resultOf(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (t: TimeoutCancellationException) {
        Result.failure(t)
    } catch (c: CancellationException) {
        throw c
    } catch (e: Exception) {
        Timber.e(e)
        Result.failure(e)
    }
}

private const val RETRY_TIME_IN_MILLIS = 15_000L
fun <R> Flow<R>.toResult(): Flow<Result<R>> = this.map { resultOf { it } }
    .retryWhen { cause, _ ->
        if (cause is IOException) {
            emit(Result.failure(cause))

            delay(RETRY_TIME_IN_MILLIS)
            true
        } else {
            false
        }
    }
    .catch {
        emit(Result.failure(it))
    }