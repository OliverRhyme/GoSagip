package dev.rhyme.gosagip.utils


import com.google.maps.PendingResult
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T> PendingResult<T>.awaitCoroutine(): T {
    return suspendCoroutine { cont ->
        setCallback(
            object : PendingResult.Callback<T> {
                override fun onResult(result: T) {
                    cont.resume(result)
                }

                override fun onFailure(e: Throwable) {
                    cont.resumeWithException(e)
                }
            }
        )
    }
}