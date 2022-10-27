package com.example.room_test


import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class NewsDataSource(
    private val context: Context
            )
    : PageKeyedDataSource<Int,List<RoomEntity>>() {
    interface DataSourceCallback {
        fun onEmpty()
    }

    val db = database.RoomDbHelper(context)
    var page = DEFAULT_PAGE

    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>,
                             callback: PageKeyedDataSource.LoadInitialCallback<Int, List<RoomEntity>>
    ) {
        //loadPage(0, callback )
        GlobalScope.launch {
            val list = mutableListOf<List<RoomEntity>>()
            val data = db.getRoomDao().getPage(page - 1 )

            list.add(data)


            Log.e("DataSource","getpage" + page.toString())

            callback.onResult(list, null, page+1)
        }
    }

    override fun loadAfter(params: PageKeyedDataSource.LoadParams<Int>,
                           callback: PageKeyedDataSource.LoadCallback<Int, List<RoomEntity>>
    ){
        GlobalScope.launch{
            val list = mutableListOf<List<RoomEntity>>()
            page += 1
            val data = db.getRoomDao().getPage(params.key - 1 )
            list.add(data)
            callback.onResult(list, params.key+1)
        }

    }

    override fun loadBefore(params: PageKeyedDataSource.LoadParams<Int>,
                            callback: PageKeyedDataSource.LoadCallback<Int, List<RoomEntity>>
    ){

        //do nothing
    }
    fun getConfig(): PagedList.Config {
        return PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(DEFAULT_LOAD_SIZE)
            .build()
    }

    private fun loadPage(i: Int, callback: PageKeyedDataSource.LoadCallback<Int,List<RoomEntity>>) {

    }
    companion object {
        const val DEFAULT_PAGE = 1
        const val BUFFER_PAGE_COUNT = 1
        const val PAGE_SIZE = 2
        const val DEFAULT_LOAD_SIZE = PAGE_SIZE * BUFFER_PAGE_COUNT
    }
}

class DataSourceFactory (val source : NewsDataSource) : DataSource.Factory<Int, List<RoomEntity>>() {

    private val sourceLiveData = MutableLiveData<NewsDataSource>()

    override fun create(): PageKeyedDataSource<Int, List<RoomEntity>> {

        sourceLiveData.postValue(source)

        Log.e("datasourcefactory", source.page.toString())
        return source as PageKeyedDataSource<Int, List<RoomEntity>>
    }
    fun refresh(){
        sourceLiveData.value?.invalidate()
    }
}

