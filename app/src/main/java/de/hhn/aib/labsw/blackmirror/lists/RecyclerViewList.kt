package de.hhn.aib.labsw.blackmirror.lists

import android.app.Activity
import android.content.Context
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
 * @param listItemTypeCreator The type checker of Kotlin doesn't allow invoking the constructor of
 * a generic type so we need this workaround with a functor that invokes the constructor on the call site.
 * You can copy this sample and insert the correct class: `{ <YOUR_CLASS>(it) }`
 * @author Markus Marewitz
 */
// having to repeat the ModelType as type parameter for the RecyclerViewList,
// because Kotlin has no feature to infer this type from the ListItem type parameter,
// see https://stackoverflow.com/questions/65745363/kotlin-is-it-possible-to-infer-generics-type-parameter
class RecyclerViewList<ListItemType : RecyclerViewList.ListItem<ModelType>, ModelType>(
    private val activity: Activity,
    private val recyclerView: RecyclerView,
    val itemLayoutID: Int,

    // fighting Kotlin again, but yeah it doesn't allow you to call the constructor of a generic type EVER
    // so we need this annoying wrapper/factory functor
    val listItemTypeCreator: (ModelType) -> ListItemType
) {
    // forwards listItem.size as this.size
    val size: Int get() = listItems.size

    /**
     * Base class of an item for a [RecyclerViewList]. Subclass it to deploy your business logic.
     * The signature of the subclass should look like this:
     * ```
     * class MyListItem(model: MyModel) : ListItem<MyModel>(model) { ... }
     * ```
     * Subclasses should override the [populateView] method to populate the UI and
     * the [updateView] method to update the UI.
     * @param model The data model object associated with this list item.
     * Don't write it into an attribute in the subclass instead access it to the property [model].
     */
    open class ListItem<ModelType>(
        protected var model: ModelType
    ) {
        var listPosition = -1

        open fun populateView(itemView: View) {}
        open fun updateView(itemView: View) {}
    }

    /**
     * Adds an item to the end of the list and updates the UI
     */
    fun add(item: ModelType) {
        listItems.add(listItemTypeCreator(item))
        runOnUIThread { recyclerView.adapter?.notifyItemInserted(listItems.size - 1) }
    }


    ////////////////////////////////////////////////////////////////////////////////////////
    // IMPLEMENTATION
    ////////////////////////////////////////////////////////////////////////////////////////

    private val listItems = mutableListOf<ListItemType>()
    private fun runOnUIThread(action: Runnable) = activity.runOnUiThread(action)

    init {
        recyclerView.adapter = Adapter()
    }

    private inner class ItemViewHolder(
        val itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private var firstBind = true

        fun bind(listItem: ListItemType, position: Int) {
            listItem.listPosition = position
            if (firstBind) {
                listItem.populateView(itemView)
            } else {
                listItem.updateView(itemView)
            }

            firstBind = false
        }
    }

    private inner class Adapter : RecyclerView.Adapter<ItemViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            return ItemViewHolder(
                LayoutInflater.from(parent.context).inflate(itemLayoutID, parent, false)
            )
        }

        override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
            viewHolder.bind(listItems[position], position)
        }

        override fun getItemCount() = listItems.size
    }
}
