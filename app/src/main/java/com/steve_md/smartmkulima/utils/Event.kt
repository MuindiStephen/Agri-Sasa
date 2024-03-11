package com.steve_md.smartmkulima.utils

import androidx.lifecycle.Observer


/**
 * Handle events that are happening in the observer
 */
class Event<out T>(private val content: T) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (!hasBeenHandled) {
            hasBeenHandled = true
            content
        } else null
    }

    fun peekContent() = content
}

class EventObserver<T>(
    private inline val onError: ((String) -> Unit)? = null,
    private inline val onLoading: (() -> Unit)? = null,
    private inline val onSuccess: (T) -> Unit
) : Observer<Event<Resource<T>>> {
    override fun onChanged(value: Event<Resource<T>>) {
        when (val content = value.peekContent()) {
            is Resource.Success -> {
                content.data?.let(onSuccess)
            }
            is Resource.Error -> {
                value.getContentIfNotHandled().let {
                    onError?.let { error ->
                        error(it?.message!!)
                    }
                }
            }
            is Resource.Loading -> {
                onLoading?.let { loading ->
                    loading()
                }
            }
            is Resource.Empty -> TODO()
        }
    }

}