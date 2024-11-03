package com.example.newsone.ui

import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.newsone.R
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var detailTitle: TextView
    private lateinit var detailPublishedAt: TextView
    private lateinit var detailAuthor: TextView
    private lateinit var detailSource: TextView
    private lateinit var detailDescription: TextView
    private lateinit var detailContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        // Get data from intent
        val title = intent.getStringExtra("title")
        val imageUrl = intent.getStringExtra("imageUrl")
        val publishedAt = intent.getStringExtra("publishedAt")
        val author = intent.getStringExtra("author")
        val sourceName = intent.getStringExtra("sourceName")
        val description = intent.getStringExtra("description")
        val articleUrl = intent.getStringExtra("url")

        // Initialize views
        imageView = findViewById(R.id.detailImage)
        detailTitle = findViewById(R.id.detailTitle)
        detailPublishedAt = findViewById(R.id.detailPublishedAt)
        detailAuthor = findViewById(R.id.detailAuthor)
        detailSource = findViewById(R.id.detailSource)
        detailDescription = findViewById(R.id.detailDescription)
        detailContent = findViewById(R.id.detailContent)

        // Set data to views
        detailTitle.text = title
        detailPublishedAt.text = getRelativeTime(publishedAt ?: "")
        detailAuthor.text = if (!author.isNullOrEmpty()) {
            "$author; $sourceName"
        } else {
            sourceName ?: "Unknown Source"
        }
        detailDescription.text = description ?: "No description available"

        // Load image using Picasso
        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(imageView)
        }

        // Scrape the content from the article URL
        if (articleUrl != null) {
            ScrapeArticleContent().execute(articleUrl)
        }
    }

    private fun getRelativeTime(publishedAt: String): String {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val date = dateFormat.parse(publishedAt)
            val now = System.currentTimeMillis()
            val diff = now - date.time
            when {
                diff < 60_000 -> "now"
                diff < 3_600_000 -> "${(diff / 60_000)} minute${if (diff / 60_000 > 1) "s" else ""} ago"
                diff < 86_400_000 -> "${(diff / 3_600_000)} hour${if (diff / 3_600_000 > 1) "s" else ""} ago"
                diff < 2_592_000_000 -> "${(diff / 86_400_000)} day${if (diff / 86_400_000 > 1) "s" else ""} ago"
                else -> "${(diff / 2_592_000_000)} month${if (diff / 2_592_000_000 > 1) "s" else ""} ago"
            }
        } catch (e: Exception) {
            "Unknown time"
        }
    }

    private inner class ScrapeArticleContent : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            return try {
                // Connect to the URL and parse the HTML
                val doc = Jsoup.connect(params[0]!!).get()
                // Select the main content element - this may vary based on the website's structure
                val contentElement = doc.select("article").first() // Adjust the selector as needed
                contentElement?.text() // Get the text from the selected element
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: String?) {
            // Set the scraped content to the TextView
            detailContent.text = result ?: "Failed to load content."
        }
    }
}
