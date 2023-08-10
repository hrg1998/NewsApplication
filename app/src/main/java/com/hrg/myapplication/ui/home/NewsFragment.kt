package com.hrg.myapplication.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.hrg.myapplication.R
import com.hrg.myapplication.databinding.FragmentNewsBinding
import com.hrg.myapplication.domain.models.Article
import com.hrg.myapplication.domain.models.CheckErrorApiClass
import com.hrg.myapplication.domain.models.SearchParametersModel
import com.hrg.myapplication.ui.adapters.NewsPagingAdapter
import com.hrg.myapplication.ui.home.search.SearchParameterBottomSheet
import com.hrg.myapplication.utils.ERROR_CHECK_NETWORK
import com.hrg.myapplication.utils.TIMER_DELAY
import com.hrg.myapplication.utils.base.BaseFragment
import com.hrg.myapplication.utils.extensions.gone
import com.hrg.myapplication.utils.extensions.hide
import com.hrg.myapplication.utils.extensions.show
import com.hrg.myapplication.utils.myListeners.ItemClickListener
import com.hrg.myapplication.utils.toolbar.ToolbarManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : BaseFragment<NewsViewModel, FragmentNewsBinding>(),
    ItemClickListener<Any>, TextWatcher, TextView.OnEditorActionListener, View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var toolbarManager: ToolbarManager

    private var loading: Boolean = false
        set(value) {
            when (value) {
                true -> {
                    mViewBindingFrag.pbLoading.show()
                    mViewBindingFrag.rcNews.hide()
                }

                false -> {
                    mViewBindingFrag.pbLoading.gone()
                    mViewBindingFrag.rcNews.show()
                }
            }
            field = value
        }
    private var loadingPaging: Boolean = false
        set(value) {
            when (value) {
                true -> mViewBindingFrag.pbLoadingPaging.show()
                false -> mViewBindingFrag.pbLoadingPaging.gone()
            }
            field = value
        }

    private val adapter by lazy {
        NewsPagingAdapter(this)
    }

    private var timer: Timer? = null

    private val badge by lazy { BadgeDrawable.create(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        initToolbar()
        initFlows()
        initLiveData()
        initRecyclerView()
        initClicks()
    }

    private fun initToolbar() {
        toolbarManager.initSearchToolbar(
            mViewBindingFrag.toolbar,
            this,
            this,
            this,
            this
        )
    }

    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    private fun initLiveData() {
        mSavedStateHandle?.getLiveData<SearchParametersModel>(SearchParameterBottomSheet.TAG_RESULT)
            ?.observe(viewLifecycleOwner) {
                mViewModelFrag.updateSearchParameter(it.q, it.sortBy, it.searchIn, it.from, it.to)
                when (it.existFilter()) {
                    true -> {
                        BadgeUtils.attachBadgeDrawable(badge, mViewBindingFrag.toolbar.btnFilter)
                    }

                    false -> {
                        BadgeUtils.detachBadgeDrawable(badge, mViewBindingFrag.toolbar.btnFilter)
                    }
                }
                adapter.refresh()
            }
    }

    private fun initClicks() {
        mViewBindingFrag.sivRefreshPaging.setOnClickListener(this)
        mViewBindingFrag.sivRefresh.setOnClickListener(this)
    }

    private fun initRecyclerView() {
        mViewBindingFrag.srRefresh.setOnRefreshListener(this)

        mViewBindingFrag.rcNews.adapter = adapter
        mViewBindingFrag.rcNews.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter.addLoadStateListener { loadState ->
            when {
                loadState.source.refresh is LoadState.Error -> {
                    // show btn for try again
                    val error =
                        ((loadState.source.refresh as LoadState.Error).error as CheckErrorApiClass).userMessage
                    if (!(error == ERROR_CHECK_NETWORK && mViewModelFrag.searchParametersModel.q.isEmpty())) {
                        mViewBindingFrag.tvError.text = error
                        mViewBindingFrag.sivRefresh.show()
                        mViewBindingFrag.rlError.show()
                    }

                    loading = false
                    loadingPaging = false
                }

                loadState.source.append is LoadState.Error -> {
                    // show btn for retry
                    mViewBindingFrag.tvErrorPaging.text =
                        ((loadState.source.append as LoadState.Error).error as CheckErrorApiClass).userMessage
                    mViewBindingFrag.rlErrorPaging.show()

                    loading = false
                    loadingPaging = false
                }

                loadState.source.refresh is LoadState.Loading -> {
                    mViewBindingFrag.rlErrorPaging.gone()
                    mViewBindingFrag.rlError.gone()

                    loading = true
                    loadingPaging = false
                }

                loadState.source.append is LoadState.Loading -> {
                    mViewBindingFrag.rlErrorPaging.gone()
                    mViewBindingFrag.rlError.gone()

                    loading = false
                    loadingPaging = true
                }

                loadState.source.refresh is LoadState.NotLoading -> {
                    if (adapter.itemCount == 0) {
                        if (mViewModelFrag.searchParametersModel.q.isEmpty()) {
                            mViewBindingFrag.rlError.gone()
                        } else {
                            mViewBindingFrag.tvError.text = getString(R.string.error_cant_find_news)
                            mViewBindingFrag.sivRefresh.gone()
                            mViewBindingFrag.rlError.show()
                        }
                    }

                    loading = false
                    loadingPaging = false
                }

                loadState.source.append is LoadState.NotLoading -> {
                    loading = false
                    loadingPaging = false
                }
            }

        }
    }

    private fun initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mViewModelFrag.newsPaging.collect {
                    adapter.submitData(it as PagingData<Article>)
                }
            }
        }
    }

    override fun getViewModel(): NewsViewModel {
        val viewModel by viewModels<NewsViewModel>()
        return viewModel
    }

    override fun getViewBinding(): FragmentNewsBinding = FragmentNewsBinding.inflate(layoutInflater)

    override fun itemClicked(item: Any) {
        if (item is Article) {
            val navAction =
                NewsFragmentDirections.actionNewsFragmentToNewsDetailFragment(item)
            navController.navigate(navAction)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        timer?.cancel()
        mViewModelFrag.updateSearchParameter(query = s.toString())
        timer = Timer().apply {
            this.schedule(object : TimerTask() {
                override fun run() {
                    adapter.refresh()
                }
            }, TIMER_DELAY)
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            timer?.cancel()
            adapter.refresh()
            return true
        }
        return false
    }

    override fun onClick(v: View?) {
        when (v) {
            mViewBindingFrag.toolbar.btnSearch -> {
                timer?.cancel()
                adapter.refresh()
            }

            mViewBindingFrag.toolbar.btnFilterLayout -> {
                val action =
                    NewsFragmentDirections.actionNewsFragmentToSearchParameterBottomSheet(
                        mViewModelFrag.searchParametersModel
                    )
                navController.navigate(action)
            }

            mViewBindingFrag.sivRefresh -> {
                adapter.refresh()
            }

            mViewBindingFrag.sivRefreshPaging -> {
                adapter.retry()
            }
        }
    }

    override fun onRefresh() {
        mViewBindingFrag.srRefresh.isRefreshing = false
        adapter.refresh()
    }
}