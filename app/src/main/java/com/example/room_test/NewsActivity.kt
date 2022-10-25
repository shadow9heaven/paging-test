package com.example.room_test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.room_test.databinding.ActivityNewsBinding


class NewsActivity : AppCompatActivity(){
    private lateinit var newsBinding: ActivityNewsBinding
    lateinit var newsAdapter: NewsAdapter
    lateinit var newsDataSourceFactory : DataSourceFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newsdatasource = NewsDataSource(this)
        newsDataSourceFactory = DataSourceFactory(this, newsdatasource)
        //val parentname = this.parent.componentName.toString()
        //Log.e("newsActivity",parentname)
        //var livePagedListBuilder = LivePagedListBuilder(newsDataSourceFactory, newsdatasource.getConfig())
        //var mLiveData: LiveData<PagedList<List<RoomEntity>>> = livePagedListBuilder.build()

        newsBinding = ActivityNewsBinding.inflate(LayoutInflater.from(this))
        newsBinding.rvList.layoutManager = LinearLayoutManager(this)
        newsBinding.rvList.itemAnimator = null


        newsAdapter = NewsAdapter(this,newsdatasource)
        /*
        mLiveData.observe(this,Observer{ it->
            newsAdapter.submitList(it)
        })
*/

        newsBinding.rvList.adapter = newsAdapter
        setContentView(newsBinding.root)

    }
}

