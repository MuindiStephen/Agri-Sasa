package com.steve_md.smartmkulima.utils.form_validation.util

import androidx.annotation.NonNull
import androidx.lifecycle.*
import com.ekenya.rnd.common.form_validation.ui_extensions.combineLatest

internal class ButtonObserverEngine(
    private var lifecycleOwner: LifecycleOwner?,
    private val validators: List<LiveData<Boolean>>,
    private val skipFirstEvents: Boolean = false,
    private val fireIfAtLeastOneIsValid: Boolean = false,
    callback: ((Boolean) -> Unit)?
) : DefaultLifecycleObserver {

    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    private lateinit var lastResult: LiveData<Boolean>

    private val observer = object : Observer<Boolean> {
        override fun onChanged(value: Boolean) {
            callback?.invoke(value ?: return)
        }
    }

    override fun onStart(@NonNull owner : LifecycleOwner ) {
        lastResult = validators.combineLatest(skipFirstEvents) {
            if (fireIfAtLeastOneIsValid) it.contains(true) else !it.contains(false)
        }
        lastResult.observe(owner, observer)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        lastResult.removeObserver(observer)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        lifecycleOwner?.lifecycle?.removeObserver(this)
        lifecycleOwner = null
        lastResult.removeObservers(owner)
    }

}