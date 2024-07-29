package com.ekenya.rnd.common.form_validation.ui_extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

internal fun <T, R> List<LiveData<T>>.combineLatest(skipFirstEvents: Boolean = false, combiner: (List<T?>) -> R): LiveData<R> {

    val latestValues = mutableMapOf<LiveData<T>, T>()

    val combineIfNeeded: (MediatorLiveData<R>) -> Unit = { mediatorLiveData ->
        if (!skipFirstEvents || (skipFirstEvents && latestValues.size == this.size)) {
            mediatorLiveData.value = combiner.invoke(latestValues.values.toList())
        }
    }

    val mediator = MediatorLiveData<R>()

    forEach { item ->
        mediator.addSource(item) {
            latestValues[item] = it
            combineIfNeeded(mediator)
        }
    }

    return mediator
}