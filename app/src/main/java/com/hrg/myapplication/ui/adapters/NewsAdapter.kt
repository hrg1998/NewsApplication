package com.hrg.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hrg.myapplication.databinding.ItemNewsBinding
import com.hrg.myapplication.domain.models.Article
import com.hrg.myapplication.ui.adapters.viewHolders.NewsViewHolder
import com.hrg.myapplication.utils.myListeners.ItemClickListener

class NewsAdapter(private val listener: ItemClickListener<Any>) :
    RecyclerView.Adapter<NewsViewHolder>() {
    private val data: ArrayList<Article> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun setData(newData: List<Article>) {
        val oldSize = data.size
        data.clear()
        data.addAll(newData)
        val newSize = data.size
        when {
            oldSize > newSize -> {
                notifyItemRangeChanged(0, newSize)
                notifyItemRangeRemoved(newSize, oldSize - newSize)
            }

            oldSize == newSize -> {
                notifyItemRangeChanged(0, newSize)
            }

            oldSize < newSize -> {
                notifyItemRangeChanged(0, oldSize)
                notifyItemRangeRemoved(oldSize, newSize - oldSize)
            }
        }
    }
}