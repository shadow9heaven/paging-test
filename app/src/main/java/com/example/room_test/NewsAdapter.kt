package com.example.room_test


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import androidx.lifecycle.LiveData
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
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
    val dataSourceFactory: DataSourceFactory = DataSourceFactory(context, newsDataSource)
    var livePagedListBuilder = LivePagedListBuilder(dataSourceFactory, newsDataSource.getConfig())
    var mLiveData: LiveData<PagedList<List<RoomEntity>>> = livePagedListBuilder.build()

    //lateinit var lifecycleOwner : LifecycleOwner

    val lock = java.lang.Object()
    //var current_page = 0
    var dataSourceLoaded = false
    lateinit var datalist : MutableList<RoomEntity>
//    var mRecyclerView: RecyclerView? = null
//    //var myListAdapter: MyListAdapter? = null
//    var arrayList: ArrayList<HashMap<String, String>> = ArrayList()

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh = NewsViewHolder(parent)
        parent.findViewTreeLifecycleOwner()?.let {
            mLiveData.observe(it) { jt ->
                ///add  datasource to pagedlist
                //Log.e("lifecycleowner", jt.size.toString())
                this.submitList(jt)
                if (jt.size > 0){

                    dataSourceLoaded = true
                    //ioThread {
                        //val tv = vh.itemView.findViewById<TextView>(R.id.tv_menuname)
                        //tv.text = jt[0][]?.name ?: ""
                    //}
                    //notifyItemRangeChanged(0,16)
                }
                else{
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
                    //datalist = getItem(0) as MutableList<RoomEntity>
                    val content = getItem(0)

                    if (content != null) {
                        if (content.size>0) {
//                            Log.e("onBindViewHolder", position.toString() + ":" + content.size.toString())
                            viewHolder.bindTo(content[position])
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
        return 100
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



class PagingBoundaryCallback(context: Context) :
    PagedList.BoundaryCallback<RoomEntity>() {
    //val pagingDataItems: LiveData<PagedList<RoomEntity>> by lazy {
    //ewsDataFactory.toLiveData(6, null)
    //}
}