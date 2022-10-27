package com.example.room_test


import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.paging.toLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.room_test.NewsViewHolder.Companion.diffCallback


class NewsAdapter constructor(
    private val context: Context,
    private val newsDataSource: NewsDataSource
) :
    PagedListAdapter<List<RoomEntity>, RecyclerView.ViewHolder>(diffCallback) {

    //private lateinit var mPostLiveData: MutableLiveData<List<RoomEntity>>
    //private val sourceLiveData = MutableLiveData<List<RoomEntity>>()
    //sourceLiveData.postValue(source)
    val dataSourceFactory: DataSourceFactory = DataSourceFactory(newsDataSource)
    var totalItem = 100
    var livePagedListBuilder = LivePagedListBuilder(dataSourceFactory, newsDataSource.getConfig())
    var mLiveData: LiveData<PagedList<List<RoomEntity>>> =
        livePagedListBuilder.setBoundaryCallback(PagingBoundaryCallback(context)).build()

    //val PVM = PagingViewModel(dataSourceFactory)
    //var mLiveData: LiveData<PagedList<List<RoomEntity>>> = PVM.pagingDataItems

    //lateinit var lifecycleOwner : LifecycleOwner
    //val lock = java.lang.Object()
    //var current_page = 0
    var dataSourceLoaded = false
    lateinit var datalist: MutableList<RoomEntity>

    //    var mRecyclerView: RecyclerView? = null
//    //var myListAdapter: MyListAdapter? = null
//    var arrayList: ArrayList<HashMap<String, String>> = ArrayList()
    @SuppressLint("ResourceType")
    override fun onCurrentListChanged(currentList: PagedList<List<RoomEntity>>?) {
        super.onCurrentListChanged(currentList)

    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh = NewsViewHolder(parent)
        parent.findViewTreeLifecycleOwner()?.let {
            mLiveData.observe(it) { jt ->
                ///add  datasource to pagedlist
                Log.e("lifecycleowner", jt.size.toString())
                this.submitList(jt)


                if (jt.size > 0) {

                    dataSourceLoaded = true
                    //newsDataSource.invalidate()
                    if(totalItem != jt.size *100){
                        totalItem = jt.size *100
                        Handler().post{
                            Runnable {
                                dataSourceFactory.refresh()
                            }
                        }
                    }
                    //ioThread {
                    //val tv = vh.itemView.findViewById<TextView>(R.id.tv_menuname)
                    //tv.text = jt[0][]?.name ?: ""
                    //}
//                    ioThread {
                       //notifyItemRangeChanged(0,16)
//                    }
                } else {
                    //Log.e("","")
                }
            }
        }
        return vh
        //add data
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        //super.onBindViewHolder(viewHolder, position)
        if (viewHolder is NewsViewHolder) {
            //setting viewHolder

            //current_page = position
            if (dataSourceLoaded) {
                try {
                    val page = position /100
                    val real_position = position -(page *100)
                    //datalist = getItem(0) as MutableList<RoomEntity>
                    val content = getItem(page)
                    if (content != null) {
                        if (content.size > 0) {
//                            Log.e("onBindViewHolder", position.toString() + ":" + content.size.toString())
                            viewHolder.bindTo(content[real_position])
                        }
                    }
                } catch (e: Exception) {
                    Log.e("BVHException", "page:" + position.toString() + " = " + e.message!!)
                }
            }
        }

    }

    override fun getItemId(position: Int): Long {
        return (position + 1).toLong()
    }

    override fun getItemCount(): Int {
        return 1000
    }


}


class NewsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_myholder, parent, false)
) {
    //    var room: MutableList<RoomEntity> = mutableListOf()
//        private set
//    private val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_users)
    var room: RoomEntity? = null
        private set
    private val textView = itemView.findViewById<TextView>(R.id.tv_menuname)
    //val nameObserver : Observer<>
    fun bindTo(item: RoomEntity?) {
        if (item != null) {
            room = item
        }
        //recyclerView.adapter = ListAdapter(room)
        textView.text = item?.name

    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<List<RoomEntity>>() {
            override fun areItemsTheSame(
                oldItem: List<RoomEntity>,
                newItem: List<RoomEntity>
            ): Boolean {
                return if (oldItem is List<RoomEntity> && newItem is List<RoomEntity>) {
                    oldItem[1].id == newItem[1].id
                } else {
                    false
                }
            }

            override fun areContentsTheSame(
                oldItem: List<RoomEntity>,
                newItem: List<RoomEntity>
            ): Boolean {
                return if (oldItem is List<RoomEntity> && newItem is List<RoomEntity>) {
                    oldItem[1].id == newItem[1].id
                } else {
                    false
                }
            }
        }
    }
}

class PagingViewModel(sFactory: DataSourceFactory) : ViewModel() {

    //private val sFactory

    val pagingDataItems: LiveData<PagedList<List<RoomEntity>>> by lazy {
        sFactory.toLiveData(3, null)
    }
}


class PagingBoundaryCallback(context: Context) :
    PagedList.BoundaryCallback<List<RoomEntity>>() {
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        Log.e("boundaryCallBack", "onzeroitemloaded")
    }

    override fun onItemAtFrontLoaded(itemAtFront: List<RoomEntity>) {
        super.onItemAtFrontLoaded(itemAtFront)
        Log.e("boundaryCallBack", "onitematfrontloaded")
    }

    override fun onItemAtEndLoaded(itemAtEnd: List<RoomEntity>) {
        super.onItemAtEndLoaded(itemAtEnd)
        Log.e("boundaryCallBack", "onitematendloaded")
    }
    //val pagingDataItems: LiveData<PagedList<RoomEntity>> by lazy {
    //ewsDataFactory.toLiveData(6, null)
    //}


}