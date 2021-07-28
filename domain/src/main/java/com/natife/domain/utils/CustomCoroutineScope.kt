package com.natife.domain.utils

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CustomScope : CoroutineScope {

    private var parentJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + parentJob

    fun cancelChildren() {
        parentJob.cancelChildren()
    }
}
