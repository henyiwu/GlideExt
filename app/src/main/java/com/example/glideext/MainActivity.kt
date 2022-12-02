package com.example.glideext

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Filter
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.glideext.lifecycle.MyLifecycleFragment
import com.example.glideext.module.GlideApp
import java.io.File
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var ivPic: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ivPic = findViewById(R.id.iv)

//        传统方式
//        val requestOptions = RequestOptions().centerCrop()
//        Glide.with(this)
//            .load("")
//            .apply(requestOptions)
//            .into(ivPic)


        // 放入imageView展示
        GlideApp.with(this)
            .fade()
            .load("https://img2.baidu.com/it/u=1465005623,1537005581&fm=253&fmt=auto&app=138&f=JPEG?w=658&h=494")
            .applyAvatar(144 * 2)
            .into(ivPic)


//        // 不放入imageView展示
//        val futureTarget = GlideApp.with(this)
//            .asBitmap()
//            .load("https://img2.baidu.com/it/u=1465005623,1537005581&fm=253&fmt=auto&app=138&f=JPEG?w=658&h=494")
//            .applyAvatar(144 * 3)
//            .submit()
//        // 异步获取bitmap
//        thread {
//            val bitmap = futureTarget.get()
//            runOnUiThread {
//                ivPic.setImageBitmap(bitmap)
//            }
//        }
    }

    fun testLinkedHashMap() {
        val map = LinkedHashMap<String, String>(0, 0.75f, true)
        map.put("a", "a")
        map.put("b", "b")
        map.put("c", "c")
        map.put("d", "d")
        map.put("e", "e")
        map.put("f", "f")

        map.get("a")

        map.forEach {
            Log.d("west", "key: ${it.key}, value: ${it.value}")
        }

//        val lifecycleFragment = MyLifecycleFragment()
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.add(lifecycleFragment)
    }
}