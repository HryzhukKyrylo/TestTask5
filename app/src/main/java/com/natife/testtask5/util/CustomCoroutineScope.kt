package com.natife.testtask5.util

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CustomScope : CoroutineScope {

    private var parentJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + parentJob

    fun cancelChildren() {
//        parentJob.children.forEach {
//            it.cancel()
//        }
        // some
        parentJob.cancelChildren()
    }
}
