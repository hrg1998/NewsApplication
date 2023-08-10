package com.hrg.myapplication.utils.toolbar

import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView.OnEditorActionListener
import com.hrg.myapplication.databinding.ToolbarMainBinding
import com.hrg.myapplication.databinding.ToolbarSearchNewsBinding
import com.hrg.myapplication.utils.extensions.loadImage
import javax.inject.Inject

class ToolbarManager @Inject constructor() {
    fun initMainToolbar(
        toolbar: ToolbarMainBinding,
        title: String,
        primaryBtnIcon: Int? = null,
        primaryBtnClickListener: View.OnClickListener? = null,
        actionBtnIcon: Int? = null,
        actionBtnClickListener: View.OnClickListener? = null
    ) {
        toolbar.toolbarTvTitle.text = title

        primaryBtnIcon?.let { toolbar.toolbarBtnBack.loadImage(primaryBtnIcon) }
        primaryBtnClickListener?.let { toolbar.toolbarBtnBack.setOnClickListener(it) }

        actionBtnIcon?.let { toolbar.toolbarBtnAction.loadImage(actionBtnIcon) }
        actionBtnClickListener?.let { toolbar.toolbarBtnAction.setOnClickListener(it) }
    }

    fun initSearchToolbar(
        toolbar: ToolbarSearchNewsBinding,
        btnSearchAction: OnClickListener,
        btnFilterAction: OnClickListener,
        searchbarTextChangeListener: TextWatcher,
        searchbarEditorActionListener: OnEditorActionListener
    ) {
        toolbar.btnSearch.setOnClickListener(btnSearchAction)
        toolbar.btnFilterLayout.setOnClickListener(btnFilterAction)
        toolbar.etSearchbar.addTextChangedListener(searchbarTextChangeListener)
        toolbar.etSearchbar.setOnEditorActionListener(searchbarEditorActionListener)
    }
}