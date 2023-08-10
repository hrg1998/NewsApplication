package com.hrg.myapplication.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hrg.myapplication.utils.extensions.getWindowBounds

abstract class BaseBottomSheetDialogFragment<VB : ViewBinding> : BottomSheetDialogFragment() {
    // variable region
    protected lateinit var mViewBinding: VB

    lateinit var behavior: BottomSheetBehavior<*>

    protected val navController: NavController by lazy { findNavController() }

    private var contentPeekHeightState = false
    private var draggable = true

    // util region
    fun contentPeekHeight(state: Boolean) {
        contentPeekHeightState = state
    }

    fun setDraggable(state: Boolean) {
        draggable = state
    }

    // override region
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as BottomSheetDialog).let { bottomSheetDialog ->
            behavior = bottomSheetDialog.behavior
            behavior.isDraggable = draggable

        }
        dialog?.setOnShowListener {
            val bottomSheet =
                dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val layoutParam = it.layoutParams
                if (!contentPeekHeightState) {
                    layoutParam?.height = requireActivity().getWindowBounds().second
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
                it.layoutParams = layoutParam
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            mViewBinding.root
        } catch (e: Exception) {
            mViewBinding = getViewBinding()
            mViewBinding.root
        }
    }

    // abstract region
    abstract fun getViewBinding(): VB
}