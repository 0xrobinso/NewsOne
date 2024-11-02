package com.example.newsone

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsone.adapters.NewsAdapter
import com.example.newsone.api.RetrofitInstance
import com.example.newsone.models.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.widget.SearchView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchTopHeadlines()

        // Set up search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchNews(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optional: could filter results locally as text changes
                return false
            }
        })
    }

    private fun fetchTopHeadlines() {
        RetrofitInstance.api.getTopHeadlines("us", "your_api_key_here")
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    if (response.isSuccessful) {
                        val articles = response.body()?.articles ?: emptyList()
                        newsAdapter = NewsAdapter(articles)
                        recyclerView.adapter = newsAdapter
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Failed to fetch news", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun searchNews(query: String) {
        RetrofitInstance.api.searchNews(query, "05f57c89ed4947a9a255769ef7650b57")
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    if (response.isSuccessful) {
                        val articles = response.body()?.articles ?: emptyList()
                        newsAdapter = NewsAdapter(articles)
                        recyclerView.adapter = newsAdapter
                    } else {
                        Toast.makeText(this@MainActivity, "No results found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Failed to fetch search results", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
