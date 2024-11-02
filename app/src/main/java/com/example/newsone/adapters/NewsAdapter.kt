package com.example.newsone.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsone.R
import com.example.newsone.models.NewsArticle
import com.example.newsone.ui.NewsDetailActivity
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale


class NewsAdapter(private val articles: List<NewsArticle>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.description)
        val image: ImageView = itemView.findViewById(R.id.image)
        val publishedAt: TextView = itemView.findViewById(R.id.publishedAt)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val article = articles[position]
                    val intent = Intent(itemView.context, NewsDetailActivity::class.java).apply {
                        putExtra("title", article.title)
                        putExtra("imageUrl", article.urlToImage)
                        putExtra("publishedAt", article.publishedAt)
                        putExtra("author", article.author)
                        putExtra("sourceName", article.source.name)
                        putExtra("description", article.description)
                        putExtra("content", article.content)
                        putExtra("url", article.url) // Make sure this is included

                    }
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.title.text = article.title
        holder.description.text = article.description
        holder.publishedAt.text = article.publishedAt
        if (!article.urlToImage.isNullOrEmpty()) {
            Picasso.get().load(article.urlToImage).into(holder.image)
        }
    }

    override fun getItemCount() = articles.size
}
