package com.hrg.myapplication.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.hrg.myapplication.R
import com.hrg.myapplication.utils.extensions.getWindowBounds

abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment() {
    protected lateinit var mViewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.DialogFragmentStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewBinding = getViewBinding()
        return mViewBinding.root
    }

    override fun onResume() {
        super.onResume()

        val attr = dialog?.window?.attributes
        attr?.width = (requireActivity().getWindowBounds().first * 0.9).toInt()
        attr?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = attr
    }

    abstract fun getViewBinding(): VB
}