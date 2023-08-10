package com.hrg.myapplication.ui.home.search

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.navigation.fragment.navArgs
import com.hrg.myapplication.databinding.BottomSheetSearchParameterBinding
import com.hrg.myapplication.domain.models.SearchParametersModel
import com.hrg.myapplication.utils.SearchIn
import com.hrg.myapplication.utils.SortBy
import com.hrg.myapplication.utils.base.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchParameterBottomSheet :
    BaseBottomSheetDialogFragment<BottomSheetSearchParameterBinding>(),
    CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    companion object {
        const val TAG_RESULT = "tag-result-filter"
    }

    private val args by navArgs<SearchParameterBottomSheetArgs>()
    private lateinit var resultSearchParameters: SearchParametersModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initClicks()
        initCheckChange()
        initDataWithInputModel()
    }

    private fun initCheckChange() {
        mViewBinding.rgSort.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                mViewBinding.rbPopularity.id -> resultSearchParameters.sortBy = SortBy.Popularity
                mViewBinding.rbRelevancy.id -> resultSearchParameters.sortBy = SortBy.Relevancy
                mViewBinding.rbPublishAt.id -> resultSearchParameters.sortBy = SortBy.PublishedAt
            }
        }
        mViewBinding.cbTitle.setOnCheckedChangeListener(this)
        mViewBinding.cbContent.setOnCheckedChangeListener(this)
        mViewBinding.cbDescription.setOnCheckedChangeListener(this)
    }

    private fun initClicks() {
        mViewBinding.btnClearFilter.setOnClickListener(this)
        mViewBinding.btnSubmitFilter.setOnClickListener(this)
    }

    private fun initDataWithInputModel(clear: Boolean = false) {
        resultSearchParameters =
            if (!clear)
                args.inputModel.clone()
            else
                SearchParametersModel(
                    args.inputModel.q,
                    args.inputModel.page,
                    args.inputModel.pageSize
                )
        resultSearchParameters.let {
            when (it.sortBy) {
                SortBy.Relevancy -> mViewBinding.rbRelevancy.isChecked = true
                SortBy.Popularity -> mViewBinding.rbPopularity.isChecked = true
                SortBy.PublishedAt -> mViewBinding.rbPublishAt.isChecked = true
            }
            if (it.searchIn.contains(SearchIn.Title)) {
                mViewBinding.cbTitle.isChecked = true
            }
            if (it.searchIn.contains(SearchIn.Description)) {
                mViewBinding.cbDescription.isChecked = true
            }
            if (it.searchIn.contains(SearchIn.Content)) {
                mViewBinding.cbContent.isChecked = true
            }
            if (it.searchIn.isEmpty()) {
                mViewBinding.cbContent.isChecked = false
                mViewBinding.cbDescription.isChecked = false
                mViewBinding.cbTitle.isChecked = false
            }
        }
    }

    override fun getViewBinding(): BottomSheetSearchParameterBinding =
        BottomSheetSearchParameterBinding.inflate(layoutInflater)

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            mViewBinding.cbTitle.id -> changeSearchIn(isChecked, SearchIn.Title)
            mViewBinding.cbContent.id -> changeSearchIn(isChecked, SearchIn.Content)
            mViewBinding.cbDescription.id -> changeSearchIn(isChecked, SearchIn.Description)
        }
    }

    private fun changeSearchIn(isChecked: Boolean, searchIn: SearchIn) {
        when (isChecked) {
            true -> {
                if (!resultSearchParameters.searchIn.contains(searchIn)) {
                    resultSearchParameters.searchIn.add(searchIn)
                }
            }

            false -> {
                if (resultSearchParameters.searchIn.contains(searchIn)) {
                    resultSearchParameters.searchIn.remove(searchIn)
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v) {
            mViewBinding.btnClearFilter -> {
                initDataWithInputModel(true)
            }

            mViewBinding.btnSubmitFilter -> {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    TAG_RESULT,
                    resultSearchParameters
                )
                navController.navigateUp()
            }
        }
    }
}