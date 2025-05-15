package com.example.icecream.presentation.viewmodel.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ViewModel.launchIO(
    onError: (Throwable) -> Unit = {},
    block: suspend () -> Unit
) {
    val handler = CoroutineExceptionHandler { _, exception ->
        onError(exception)
    }

    viewModelScope.launch(Dispatchers.IO + handler) {
        block()
    }
}