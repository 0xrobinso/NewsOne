package com.example.newsone.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsone.R
import com.example.newsone.helper.getRelativeTime
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
        val author: TextView = itemView.findViewById(R.id.author) // Add this line to get the author TextView

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
                        putExtra("content", article.content)
                        putExtra("url", article.url)
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

        // Set the title, description, and image
        holder.title.text = article.title
        holder.description.text = article.description ?: "No description available"
        holder.publishedAt.text = getRelativeTime(article.publishedAt ?: "")

        // Format author and source
        val authorText = if (!article.author.isNullOrEmpty()) {
            "${article.author}; ${article.source.name}"
        } else {
            article.source.name  // Show only source if author is unavailable
        }
        holder.author.text = authorText

        // Load image if available
        if (!article.urlToImage.isNullOrEmpty()) {
            Picasso.get().load(article.urlToImage).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder_image) // Optional placeholder image
        }

        // Handle click events to navigate to details
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, NewsDetailActivity::class.java).apply {
                putExtra("title", article.title)
                putExtra("imageUrl", article.urlToImage)
                putExtra("publishedAt", article.publishedAt)
                putExtra("author", article.author)
                putExtra("sourceName", article.source.name)
                putExtra("description", article.description)
                putExtra("url", article.url) // Pass the article URL for scraping
            }
            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount() = articles.size
}
