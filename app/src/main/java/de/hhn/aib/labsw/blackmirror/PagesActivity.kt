package de.hhn.aib.labsw.blackmirror

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hhn.aib.labsw.blackmirror.adapter.MyRecyclerAdapter
import de.hhn.aib.labsw.blackmirror.helper.MyItemTouchHelperCallback
import de.hhn.aib.labsw.blackmirror.helper.OnStartDragListener

class PagesActivity : AppCompatActivity() {

    var itemTouchHelper: ItemTouchHelper? = null
    private val recyclerView: RecyclerView = findViewById(R.id.pagesRecyclerView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pages)
        init()
        generateItem()
    }

    private fun generateItem() {
        val data: MutableList<String?> = ArrayList()
        val myMirror = intent.getSerializableExtra("myMirror")
        // Merge required
        // ata.addAll(Arrays.asList())
        val adapter = MyRecyclerAdapter(this, data, object:OnStartDragListener {
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
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this, 3)
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