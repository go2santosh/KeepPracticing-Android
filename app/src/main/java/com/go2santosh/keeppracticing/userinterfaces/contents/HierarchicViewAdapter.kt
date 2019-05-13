package com.go2santosh.keeppracticing.userinterfaces.contents

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.go2santosh.keeppracticing.R

class HierarchicViewAdapter(
    context: Context,
    val resource: Int,
    val list: ArrayList<HierarchicEntity>
) : ArrayAdapter<HierarchicEntity>(context, resource, list) {

    private var listItemsHashCode = this.list.hashCode()
    private var listItems = this.list
    private var layoutInflater: LayoutInflater = context.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE
    ) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        val returnView: View
        val listItem = listItems[position]

        if (convertView == null || convertView.tag != listItemsHashCode) {
            returnView = layoutInflater.inflate(resource, null)
            if (listItem.parentItemName != null) {
                returnView?.setPadding(
                    context.resources.getDimension(R.dimen.padding_large).toInt() * getHierarchyLevel(position),
                    0,
                    0,
                    0
                )
            }
            val imageView = returnView.findViewById(R.id.imageViewExpandIcon) as ImageView?
            if (listItem.isExpanded)
                imageView?.setImageDrawable(context.resources.getDrawable(R.drawable.ic_expand_less))
            else
                imageView?.setImageDrawable(context.resources.getDrawable(R.drawable.ic_expand_more))
            val textView = returnView.findViewById(R.id.textViewSubject) as TextView?
            textView?.text = listItem.name
            returnView.tag = listItems.hashCode()
        } else {
            returnView = convertView
        }

        return returnView
    }

    internal fun setList(list: ArrayList<HierarchicEntity>) {
        val newList = list.map { it }
        listItems.clear()
        listItems.addAll(newList)
        listItemsHashCode = listItems.hashCode()
        notifyDataSetChanged()
    }

    private fun getHierarchyLevel(position: Int): Int {
        var listItem: HierarchicEntity? = listItems[position]
        var hierarchyLevel = 0
        while (listItem?.parentItemName != null) {
            hierarchyLevel++
            listItem = listItems.firstOrNull { it.name == listItem?.parentItemName!! }
        }
        return hierarchyLevel
    }
}