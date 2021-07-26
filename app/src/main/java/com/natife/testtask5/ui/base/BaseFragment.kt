package com.natife.testtask5.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var mutableBinding: VB? = null
    protected val binding: VB
        get() = mutableBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mutableBinding = createBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mutableBinding = null
    }

        private fun createBinding(inflater: LayoutInflater, container: ViewGroup?): VB {
        val bindingClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val inflateMethod = bindingClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        val binding = inflateMethod.invoke(null, inflater, container, false)
        return binding as VB
    }

}
