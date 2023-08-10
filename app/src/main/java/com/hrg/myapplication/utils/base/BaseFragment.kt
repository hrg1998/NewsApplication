package com.hrg.myapplication.utils.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VM : ViewModel, VB : ViewBinding> : Fragment() {

    lateinit var mViewBindingFrag: VB

    protected val mActivity: Context by lazy { requireContext() }
    protected val mContextActivity: Context by lazy { requireActivity() }
    protected val navController: NavController by lazy { findNavController() }
    protected val mViewModelFrag by lazy { getViewModel() }

    private val mCurrentBackStack: NavBackStackEntry?
        get() = navController.currentBackStackEntry
    protected val mSavedStateHandle: SavedStateHandle?
        get() = mCurrentBackStack?.savedStateHandle

    protected var mNeedListenResult = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewBindingFrag = getViewBinding()
        return mViewBindingFrag.root
    }

    abstract fun getViewModel(): VM

    abstract fun getViewBinding(): VB
}