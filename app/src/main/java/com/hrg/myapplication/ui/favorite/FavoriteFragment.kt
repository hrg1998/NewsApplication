package com.hrg.myapplication.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hrg.myapplication.R
import com.hrg.myapplication.databinding.FragmentFavoriteNewsBinding
import com.hrg.myapplication.domain.models.Article
import com.hrg.myapplication.ui.adapters.NewsAdapter
import com.hrg.myapplication.utils.base.BaseFragment
import com.hrg.myapplication.utils.extensions.gone
import com.hrg.myapplication.utils.extensions.hide
import com.hrg.myapplication.utils.extensions.show
import com.hrg.myapplication.utils.myListeners.ItemClickListener
import com.hrg.myapplication.utils.toolbar.ToolbarManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FavoriteViewModel, FragmentFavoriteNewsBinding>(),
    ItemClickListener<Any> {

    @Inject
    lateinit var toolbarManager: ToolbarManager

    private var loading: Boolean = false
        set(value) {
            when (value) {
                true -> {
                    mViewBindingFrag.pbLoading.show()
                    mViewBindingFrag.rcFavNews.hide()
                    mViewBindingFrag.rlError.gone()
                }

                false -> {
                    mViewBindingFrag.pbLoading.gone()
                    mViewBindingFrag.rcFavNews.show()
                }
            }
            field = value
        }

    private val adapter by lazy {
        NewsAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onResume() {
        super.onResume()
        loading = true
    }

    private fun init() {
        initToolbar()
        initFlow()
        initRecyclerView()
    }

    private fun initToolbar() {
        toolbarManager.initMainToolbar(
            mViewBindingFrag.toolbar,
            getString(R.string.lbl_favorite_news)
        )
    }

    private fun initRecyclerView() {
        mViewBindingFrag.rcFavNews.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mViewBindingFrag.rcFavNews.adapter = adapter
    }

    private fun initFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mViewModelFrag.getFavNews().collect {
                    adapter.setData(it)
                    if (it.isEmpty()) {
                        mViewBindingFrag.tvError.text = getString(R.string.error_no_favorite_news)
                        mViewBindingFrag.rlError.show()
                    }
                    loading = false
                }
            }
        }
    }

    override fun getViewModel(): FavoriteViewModel {
        val viewModel by viewModels<FavoriteViewModel>()
        return viewModel
    }

    override fun getViewBinding(): FragmentFavoriteNewsBinding =
        FragmentFavoriteNewsBinding.inflate(layoutInflater)

    override fun itemClicked(item: Any) {
        when (item) {
            is Article -> {
                val navAction =
                    FavoriteFragmentDirections.actionFavoriteFragmentToNewsDetailFragment(item)
                navController.navigate(navAction)
            }
        }
    }
}