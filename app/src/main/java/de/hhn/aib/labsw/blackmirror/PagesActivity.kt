package de.hhn.aib.labsw.blackmirror

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hhn.aib.labsw.blackmirror.adapter.MyRecyclerAdapter
import de.hhn.aib.labsw.blackmirror.dataclasses.Mirror
import de.hhn.aib.labsw.blackmirror.helper.MyItemTouchHelperCallback
import de.hhn.aib.labsw.blackmirror.helper.OnStartDragListener
import java.util.*
import kotlin.collections.ArrayList

class PagesActivity : AppCompatActivity() {

    var itemTouchHelper: ItemTouchHelper? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pages)
        init()
        generateItem()
    }

    private fun generateItem() {
        val myMirror = intent.getSerializableExtra("myMirror") as Mirror
        val adapter = MyRecyclerAdapter(this, myMirror.pages, object:OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
                itemTouchHelper!!.startDrag(viewHolder!!)
            }
        })
        recyclerView.adapter = adapter
        val callback = MyItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper!!.attachToRecyclerView(recyclerView)

    }

    private fun init() {
        recyclerView = findViewById(R.id.pagesRecyclerView)
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager

        val actionButton = findViewById<FloatingActionButton>(R.id.addPageActionButton)
        actionButton.setOnClickListener {

        }

        val saveButton = findViewById<MaterialButton>(R.id.saveButton)
        saveButton.setOnClickListener {

        }

        val exitButton = findViewById<MaterialButton>(R.id.exitButton)
        exitButton.setOnClickListener {
            // intent = Intent(this, MainMenuActivity::class.java)
            // startActivity(intent)
        }
    }
}