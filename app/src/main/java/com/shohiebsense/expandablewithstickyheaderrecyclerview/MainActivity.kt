package com.shohiebsense.expandablewithstickyheaderrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.shohiebsense.expandablewithstickyheaderrecyclerview.lib.StickyHeaderItemDecoration
import com.shohiebsense.expandablewithstickyheaderrecyclerview.model.generator.MenuService
import com.shohiebsense.expandablewithstickyheaderrecyclerview.ui.MenuAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter = MenuAdapter(recycler_sample, MenuService.generate())
        val stickHeaderDecoration = StickyHeaderItemDecoration(adapter)
        recycler_sample.adapter = adapter
        recycler_sample.addItemDecoration(stickHeaderDecoration)
        recycler_sample.itemAnimator = DefaultItemAnimator()
        recycler_sample.layoutManager = LinearLayoutManager(this)





    }
}
