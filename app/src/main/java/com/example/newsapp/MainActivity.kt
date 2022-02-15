package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity(), NewItemCliked {
    private lateinit var rv_news: RecyclerView
    private lateinit var mAdapter: NewsListAdapter

    private lateinit var apiInterface: ApiInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_news = findViewById(R.id.rv_news)
        rv_news.layoutManager = LinearLayoutManager(this)
        //fetchData()
        fetchNewsData()
        mAdapter = NewsListAdapter(this)
        rv_news.adapter = mAdapter
    }

    private fun fetchData() {
        val url =
            "https://newsapi.org/v2/everything?q=bitcoin&apiKey=6e05718e201f4db9a4698bfe3e690682"
        val jsonObject = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val obj = newsJsonArray.getJSONObject(i);
                    val news = News(
                        obj.getString("title"),
                        //obj.getString("author"),
                        obj.getString("url"),
                        obj.getString("urltoImage"),

                        )
                    newsArray.add(news)
                    Log.d("news", "fetchData: $newsArray")
                }

                mAdapter.updateNews(newsArray)

            },
            Response.ErrorListener {
                Log.d("API Faild", "fetchData: ${it.toString()}")
            })

        MySingleton.getInstance(this).addToRequestQueue(jsonObject)

    }

    private fun fetchNewsData() {
        apiInterface = ApiInterface.create()

        val news = apiInterface.getNews().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(
                call: Call<NewsResponse>,
                response: retrofit2.Response<NewsResponse>
            ) {
                val newsArray = ArrayList<News>()
                val article = response.body()?.articles

                for (news in response.body()?.articles!!) {
                    newsArray.add(News(news.title,news.url, news.urlToImage))
                }
                Log.d("NewsArray", "onResponse: ${newsArray.size}")
                mAdapter.updateNews(newsArray)
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onItemClicked(item: News) {
        Toast.makeText(this, "Clicked Item Is $item", Toast.LENGTH_LONG).show()
    }
}