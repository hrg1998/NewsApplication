package com.hrg.myapplication.ui.adapters.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.hrg.myapplication.databinding.ItemNewsBinding
import com.hrg.myapplication.domain.models.Article
import com.hrg.myapplication.utils.extensions.gone
import com.hrg.myapplication.utils.extensions.hide
import com.hrg.myapplication.utils.extensions.loadImage
import com.hrg.myapplication.utils.extensions.show
import com.hrg.myapplication.utils.myListeners.ItemClickListener
import java.text.SimpleDateFormat

class NewsViewHolder(
    private val binding: ItemNewsBinding,
    private val listener: ItemClickListener<Any>
) : RecyclerView.ViewHolder(binding.root) {
    private val dateFormatInput: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    private val dateFormatOutput: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private var _item: Article? = null
    fun bind(item: Article?) {
        _item = item
        _item?.let { article ->
            //author, title, description, url, publishedAt
            if (article.urlToImage != null) {
                binding.ivBanner.loadImage(article.urlToImage)
                binding.ivBanner.show()
            } else {
                binding.ivBanner.gone()
            }

            if (article.author != null) {
                binding.tvAuthor.text = article.author
                binding.tvAuthor.show()
            } else {
                binding.tvAuthor.hide()
            }

            if (article.publishedAt != null) {
                binding.tvPublishAt.text =
                    dateFormatOutput.format(dateFormatInput.parse(article.publishedAt)!!)
                binding.tvPublishAt.show()
            } else {
                binding.tvPublishAt.hide()
            }

            binding.tvTitle.text = article.title
            binding.tvDescription.text = article.description

            binding.root.setOnClickListener {
                listener.itemClicked(article)
            }
        }
    }
}