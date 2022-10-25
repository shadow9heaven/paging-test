package com.example.room_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

import com.example.room_test.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    lateinit var db : database.RoomDbHelper
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    lateinit var tv_userlist : TextView
    lateinit var et_name : EditText
    lateinit var et_id: EditText

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (RESULT_OK == activityResult.resultCode) {
                Log.d(
                    "maho",
                    "回傳: "
                            //+ "${activityResult.data?.getStringExtra(BaseConstants.WEAPON)}"
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        tv_userlist = findViewById(R.id.tv_userlist)
        et_id = findViewById(R.id.et_id)
        et_name = findViewById(R.id.et_name)

        db = database.RoomDbHelper(this)
        /*
        GlobalScope.launch {
             db.getRoomDao().update(RoomEntity())
        }
        */
    }

    fun clickAddData(view: View){
        makeLotsdata()

        /*
        GlobalScope.launch{

            val user = db.getRoomDao().insert(RoomEntity().apply {
                try{
                    id = Integer.parseInt(et_id.text.toString());
                    name = et_name.text.toString()
                }
                catch (e : Exception){}
            })
        }

         */
    }

    fun clickCheckData(view : View){

        var user  = listOf<RoomEntity>()
        GlobalScope.launch {
            user = db.getRoomDao().getAll()
            if(user.size >0){
                runOnUiThread { tv_userlist.text = user[0].name }
            }
        }

            val intent = Intent(this@MainActivity, NewsActivity::class.java).apply {
                //var bundle = Bundle()
                //bundle.putParcelable("userlist",user)
                //this.putExtra("user",user)
            }
            startActivity(intent)
            //resultLauncher.launch(intent)
    }
    fun clickDeleteData(view: View){
        GlobalScope.launch {
            val user = db.getRoomDao().findByName(et_name.text.toString())
            try{
                db.getRoomDao().delete(user)
            }
            catch (e :Exception){}
        }
    }

    fun makeLotsdata(){
        val inputdata = loadAssetFile()
        //val temp = inputdata[0].split(" ")
        val list = mutableListOf<RoomEntity>()
        for( i in inputdata){
            val totalstring = i.split(" ")
            list.add(RoomEntity().apply {
                try{
                    id = Integer.parseInt(totalstring[0])
                    name = totalstring[1]
                }
                catch (e : Exception){}
            })
        }
        GlobalScope.launch{
            val user = db.getRoomDao().insertAll(list)
        }
        tv_userlist.text = inputdata[700]
    }
    fun loadAssetFile() :List<String>{
        val assetManager = assets
        val inputStream: InputStream= assetManager.open("inputdata.txt")
        val byteArrayOutputStream = ByteArrayOutputStream()
        val bytes = ByteArray(4096)

        var len: Int
        while (inputStream.read(bytes).also { len = it } > 0) {
            byteArrayOutputStream.write(bytes, 0, len)
        }

        return String(byteArrayOutputStream.toByteArray(), charset("UTF8")).split("\r\n")
    }
}