package com.hrg.myapplication.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hrg.myapplication.R
import com.hrg.myapplication.databinding.ActivityMainBinding
import com.hrg.myapplication.utils.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import com.hrg.myapplication.utils.extensions.gone
import com.hrg.myapplication.utils.extensions.show

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(),
    NavController.OnDestinationChangedListener {

    private var showBottomNav: Boolean = true
        set(value) {
            when (value) {
                true -> mViewBinding.navBottom.show()
                false -> mViewBinding.navBottom.gone()
            }
            field = value
        }

    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNavBottom()
    }

    private fun initNavBottom() {
        mViewBinding.navBottom.setupWithNavController(navHostFragment.navController)
        navHostFragment.navController.addOnDestinationChangedListener(this)
    }

    override fun getViewModel(): MainViewModel {
        val viewModel by viewModels<MainViewModel>()
        return viewModel
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.newsFragment -> showBottomNav = true
            R.id.favoriteFragment -> showBottomNav = true
            R.id.newsDetailFragment -> showBottomNav = false
        }
    }
}