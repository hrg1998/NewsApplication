package com.hrg.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.hrg.myapplication.databinding.ItemNewsBinding
import com.hrg.myapplication.domain.models.Article
import com.hrg.myapplication.ui.adapters.viewHolders.NewsViewHolder
import com.hrg.myapplication.utils.myListeners.ItemClickListener

class NewsPagingAdapter(private val listener: ItemClickListener<Any>) :
    PagingDataAdapter<Article, NewsViewHolder>(DataDiff) {

    object DataDiff : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding, listener)
    }
}