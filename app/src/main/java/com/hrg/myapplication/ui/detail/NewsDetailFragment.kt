package com.hrg.myapplication.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.hrg.myapplication.R
import com.hrg.myapplication.databinding.FragmentNewsDetailBinding
import com.hrg.myapplication.utils.base.BaseFragment
import com.hrg.myapplication.utils.extensions.gone
import com.hrg.myapplication.utils.extensions.hide
import com.hrg.myapplication.utils.extensions.loadImage
import com.hrg.myapplication.utils.extensions.show
import com.hrg.myapplication.utils.toolbar.ToolbarManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject


@AndroidEntryPoint
class NewsDetailFragment : BaseFragment<NewsDetailViewModel, FragmentNewsDetailBinding>(),
    View.OnClickListener {

    @Inject
    lateinit var toolbarManager: ToolbarManager

    private val args by navArgs<NewsDetailFragmentArgs>()

    private val article by lazy { args.inputModel }

    private val isExistArticleInDb: Boolean
        get() = mViewModelFrag.checkExistArticleInFavList(article)

    private val dateFormatInput: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    private val dateFormatOutput: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initToolbar()
        initInputArgs()
        initFlow()
    }

    private fun initFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mViewModelFrag.getAllFavNewsLive().collect {
                    mViewModelFrag.updateFavList(it)
                    initToolbar()
                }
            }
        }
    }

    private fun initInputArgs() {
        args.inputModel.let { article ->
            when (article.urlToImage) {
                null -> {
                    mViewBindingFrag.ivBanner.gone()
                }

                else -> {
                    mViewBindingFrag.ivBanner.show()
                    mViewBindingFrag.ivBanner.loadImage(article.urlToImage)
                }
            }

            if (article.url.isNullOrEmpty()) {
                mViewBindingFrag.btnOpenUrl.gone()
            } else {
                mViewBindingFrag.btnOpenUrl.show()
                mViewBindingFrag.btnOpenUrl.setOnClickListener(this)
            }

            mViewBindingFrag.tvTitle.text = article.title
            mViewBindingFrag.tvContent.text = article.content

            if (article.author != null) {
                mViewBindingFrag.tvAuthor.text = article.author
                mViewBindingFrag.tvAuthor.show()
            } else {
                mViewBindingFrag.tvAuthor.hide()
            }

            if (article.publishedAt != null) {
                mViewBindingFrag.tvPublishAt.text = getString(
                    R.string.lbl_publish_at,
                    dateFormatOutput.format(dateFormatInput.parse(article.publishedAt)!!)
                )
                mViewBindingFrag.tvPublishAt.show()
            } else {
                mViewBindingFrag.tvPublishAt.hide()
            }
        }
    }

    private fun initToolbar() {
        toolbarManager.initMainToolbar(
            mViewBindingFrag.toolbar,
            "",
            primaryBtnIcon = R.drawable.ic_back,
            primaryBtnClickListener = this,
            actionBtnIcon = when (isExistArticleInDb) {
                true -> R.drawable.ic_favorite
                false -> R.drawable.ic_un_favorite
            },
            actionBtnClickListener = this
        )
    }

    private fun openBrowser() {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(article.url)
        startActivity(i)
    }

    override fun getViewModel(): NewsDetailViewModel {
        val viewModel by viewModels<NewsDetailViewModel>()
        return viewModel
    }

    override fun getViewBinding(): FragmentNewsDetailBinding =
        FragmentNewsDetailBinding.inflate(layoutInflater)

    override fun onClick(v: View) {
        when (v) {
            mViewBindingFrag.btnOpenUrl -> {
                openBrowser()
            }

            mViewBindingFrag.toolbar.toolbarBtnAction -> {
                when (isExistArticleInDb) {
                    true -> mViewModelFrag.removeFav(article)
                    false -> mViewModelFrag.saveFav(article)
                }
            }

            mViewBindingFrag.toolbar.toolbarBtnBack -> {
                navController.navigateUp()
            }
        }
    }
}