package com.example.newsone.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsone.R
import com.example.newsone.adapters.NewsAdapter
import com.example.newsone.api.RetrofitInstance
import com.example.newsone.models.NewsArticle
import com.example.newsone.models.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private val articles = mutableListOf<NewsArticle>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        newsAdapter = NewsAdapter(articles)
        recyclerView.adapter = newsAdapter

        // Fetch general news
        fetchGeneralNews()
    }

    private fun fetchGeneralNews() {
        // Make API call using Retrofit
        RetrofitInstance.api.getGeneralNews("us").enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    articles.clear()
                    articles.addAll(response.body()!!.articles)
                    newsAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Handle error
                t.printStackTrace()
            }
        })
    }
}
