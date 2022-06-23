package de.hhn.aib.labsw.blackmirror.lists

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
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
 * @version 2022-06-02
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

    // forwards listItems as this.data
    val data: List<ModelType> get() = listItems

    var recentlyClickedItem: ModelType
        get() {
            if (recentlyClickedPos == -1) {
                throw IllegalStateException("There is no recently clicked item")
            }
            return listItems[recentlyClickedPos]
        }
        set(value) {
            recentlyClickedPos = tryGetItemPos(value)
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

        open fun onBind() {}
    }

    /**
     * Scrolls the list to the specified item so that is definitely visible.
     */
    fun scrollToItem(item: ModelType) {
        recyclerView.smoothScrollToPosition(tryGetItemPos(item))
    }

    /**
     * Scrolls to the top of the list.
     */
    fun scrollToTop() = recyclerView.smoothScrollToPosition(0)

    /**
     * Scrolls to the bottom of the list.
     */
    fun scrollToBottom() = recyclerView.smoothScrollToPosition(listItems.size - 1)

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
     * Call this method when an item of this list has changed
     */
    fun update(item: ModelType, action: (ModelType) -> Unit = {}) {
        action(item)
        runOnUIThread { recyclerView.adapter?.notifyItemChanged(tryGetItemPos(item)) }
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

    /**
     * Call this method to remove an item from this list. This will notify
     * the item remove listener (see [setOnItemRemovedListener]).
     */
    fun remove(item: ModelType) = removeAt(tryGetItemPos(item), item)

    /**
     * Sets whether removing list items when the user swipes the item to either side
     * should be enabled. When an item is removed the item remove listener
     * will be notified (see [setOnItemRemovedListener]).
     *
     * @param remove Items will be removed when this is set to `true`.
     */
    fun removeItemOnSwipe(remove: Boolean) {
        if (remove) {
            itemTouchHelper.attachToRecyclerView(recyclerView)
        } else {
            itemTouchHelper.attachToRecyclerView(null)
        }
    }

    /**
     * @param listener A callback that is invoked whenever an item is removed.
     * May be `null` to remove the previously set callback.
     */
    fun setOnItemRemovedListener(listener: ((ModelType) -> Unit)?) {
        onItemRemovedListener = listener
    }


    ////////////////////////////////////////////////////////////////////////////////////////
    // IMPLEMENTATION
    ////////////////////////////////////////////////////////////////////////////////////////

    private val listItems = mutableListOf<ModelType>()
    private fun runOnUIThread(action: Runnable) = activity.runOnUiThread(action)

    private var onItemClickedListener: ((ModelType) -> Unit)? = null
    private var onItemRemovedListener: ((ModelType) -> Unit)? = null
    private var recentlyClickedPos = -1

    /**
     * An object containing the callbacks for item swiping and dragging.
     * This is attached and detached to enable/disable item removal on swiping.
     */
    private val itemTouchHelper =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                v: RecyclerView,
                h: RecyclerView.ViewHolder,
                t: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, dir: Int) {
                val pos = viewHolder.adapterPosition
                val itemModel = listItems[pos]
                removeAt(pos, itemModel)
            }
        })

    init {
        recyclerView.adapter = Adapter()
    }

    /**
     * Tries to get the position of the specified item in this list.
     *
     * @throws IllegalArgumentException If the item is not contained in this list.
     */
    private fun tryGetItemPos(item: ModelType): Int {
        val index = listItems.indexOf(item)
        if (index == -1) {
            throw IllegalArgumentException("The item is not contained in this list")
        }
        return index
    }

    /**
     * Removes the item at the specified position and invokes the
     * callback [onItemRemovedListener] if set.
     *
     * @throws IllegalArgumentException if the position is out of bounds or doesn't match
     * the position of the specified item.
     */
    private fun removeAt(position: Int, item: ModelType) {
        if (position != tryGetItemPos(item)) {
            throw IllegalArgumentException(
                "The specified item is not located at the specified position")
        }
        if (position == recentlyClickedPos) {
            recentlyClickedPos = -1
        }
        listItems.removeAt(position)
        runOnUIThread {
            recyclerView.adapter?.notifyItemRemoved(position)
            onItemRemovedListener?.invoke(item)
        }
    }

    /**
     * Wrapper for the generic [ItemView] that implements common functionality of all items views.
     */
    private inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemViewer = itemViewCreator(itemView)

        init {
            itemView.setOnClickListener {
                // listItem can not be null because it must already have been bound
                // to be clicked
                recentlyClickedPos = adapterPosition
                onItemClickedListener?.invoke(listItems[adapterPosition])
            }
        }

        /**
         * Binds the view holder to an item in this list and calls the clients
         * [ItemView.onBind] method.
         */
        fun bind(position: Int) {
            itemViewer.model = listItems[position]
            itemViewer.onBind()
        }
    }

    /**
     * The recycler view adapter that controls the construction and binding of view holders.
     */
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
