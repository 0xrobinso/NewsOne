package com.example.newsone.ui

import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.newsone.R
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var detailTitle: TextView
    private lateinit var detailPublishedAt: TextView
    private lateinit var detailAuthor: TextView
    private lateinit var detailSource: TextView
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
        val articleUrl = intent.getStringExtra("url")

        // Initialize views
        imageView = findViewById(R.id.detailImage)
        detailTitle = findViewById(R.id.detailTitle)
        detailPublishedAt = findViewById(R.id.detailPublishedAt)
        detailAuthor = findViewById(R.id.detailAuthor)
        detailSource = findViewById(R.id.detailSource)
        detailContent = findViewById(R.id.detailContent)

        // Set data to views
        detailTitle.text = title
        detailPublishedAt.text = publishedAt
        detailAuthor.text = author ?: "Unknown Author"
        detailSource.text = sourceName

        // Load image using Picasso
        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(imageView)
        }

        // Check the article URL and scrape content
        if (!articleUrl.isNullOrEmpty()) {
            ScrapeArticleContent().execute(articleUrl)
        } else {
            detailContent.text = "No article URL provided."
        }
    }

    private inner class ScrapeArticleContent : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            return try {
                // Connect to the URL and parse the HTML
                val doc = Jsoup.connect(params[0]!!).get()
                // Select the main content element - this will depend on the website's structure
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
