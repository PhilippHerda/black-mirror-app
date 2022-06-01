package de.hhn.aib.labsw.blackmirror.lists

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Generic android list using a recycler view. You still have to create a xml layout file for the
 * items.
 * @param activity The activity that is calling this constructor, usually `this`
 * @param recyclerView The view object of the [RecyclerView]. Make sure the xml component contains the entry
 * `app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"`
 * @param itemLayoutID The ID of the xml layout file of an individual list item.
 * You probably get it from `findViewByID(R.id.<THE_RECYCLER_VIEW_ID>)`
 * @param itemViewCreator The type checker of Kotlin doesn't allow invoking the constructor of
 * a generic type so we need this workaround with a functor that invokes the constructor on the call site.
 * You can copy this sample and insert the correct class: `{ <YOUR_CLASS>(it) }`
 * @author Markus Marewitz
 */
// having to repeat the ModelType as type parameter for the RecyclerViewList,
// because Kotlin has no feature to infer this type from the ListItem type parameter,
// see https://stackoverflow.com/questions/65745363/kotlin-is-it-possible-to-infer-generics-type-parameter
class RecyclerViewList<ItemViewType : RecyclerViewList.ItemView<ModelType>, ModelType : Any>(
    private val activity: Activity,
    private val recyclerView: RecyclerView,
    val itemLayoutID: Int,

    // fighting Kotlin again, but yeah it doesn't allow you to call the constructor of a generic type EVER
    // so we need this annoying wrapper/factory functor
    val itemViewCreator: (View) -> ItemViewType
) {
    // forwards listItem.size as this.size
    val size: Int get() = listItems.size

    var recentlyClickedItem: ModelType
        get() {
            if (recentlyClickedPos == -1) {
                throw IllegalStateException("There is no recently clicked item")
            }
            return listItems[recentlyClickedPos]
        }
        set(value) {
            val index = listItems.indexOf(value)
            if (index == -1) {
                throw IllegalArgumentException("The item is not contained in this list")
            }
            recentlyClickedPos = index
        }


    /**
     * Base class of an item for a [RecyclerViewList]. Subclass it to deploy your business logic.
     * The signature of the subclass should look like this:
     * ```
     * class MyListItem(itemView: View) : ItemView<MyModel>() { ... }
     * ```
     * Note that in a recycler view item views are constantly reused for different item in the list.
     * Subclasses should populate UI elements in their constructor and override the [onBind] method
     * to bind them to an item in the list. The bound model object is available as the property [model].
     */
    // ModelType has to derive from Any to declare it non-nullable
    open class ItemView<ModelType : Any> {
        lateinit var model: ModelType
        var listPosition = -1

        open fun onBind() {}
    }

    /**
     * Adds an item to the end of the list and updates the UI
     */
    fun add(item: ModelType) {
        listItems.add(item)
        runOnUIThread { recyclerView.adapter?.notifyItemInserted(listItems.size - 1) }
    }

    /**
     * Use this method for synchronous item click actions
     */
    fun setOnItemClickedListener(listener: ((ModelType) -> Unit)?) {
        onItemClickedListener = listener
    }

    /**
     * Use this method for asynchronous item click actions.
     * Call this method after the asynchronous tasks have been finished.
     */
    fun updateRecentlyClickedItem(action: (ModelType) -> Unit) {
        if (recentlyClickedPos == -1) {
            throw IllegalStateException("There is no recently clicked item")
        }
        action(listItems[recentlyClickedPos])
        runOnUIThread { recyclerView.adapter?.notifyItemChanged(recentlyClickedPos) }
    }

    fun deleteItemOnSwipe(delete: Boolean) {
        TODO("Not yet implemented")
    }



    ////////////////////////////////////////////////////////////////////////////////////////
    // IMPLEMENTATION
    ////////////////////////////////////////////////////////////////////////////////////////

    private val listItems = mutableListOf<ModelType>()
    private fun runOnUIThread(action: Runnable) = activity.runOnUiThread(action)

    private var onItemClickedListener: ((ModelType) -> Unit)? = null
    private var recentlyClickedPos = -1

    init {
        recyclerView.adapter = Adapter()
    }

    private inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemViewer = itemViewCreator(itemView)

        init {
            itemView.setOnClickListener {
                // listItem can not be null because it must already have been bound
                // to be clicked
                val pos = itemViewer!!.listPosition
                recentlyClickedPos = pos
                onItemClickedListener?.invoke(listItems[pos])
            }
        }

        fun bind(position: Int) {
            itemViewer.model = listItems[position]
            itemViewer.listPosition = position
            itemViewer.onBind()
        }
    }

    private inner class Adapter : RecyclerView.Adapter<ItemViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            return ItemViewHolder(
                LayoutInflater.from(parent.context).inflate(itemLayoutID, parent, false)
            )
        }

        override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
            viewHolder.bind(position)
        }

        override fun getItemCount() = listItems.size
    }
}
