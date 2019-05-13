package com.go2santosh.keeppracticing.userinterfaces.contents

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.go2santosh.keeppracticing.R

class HierarchicViewAdapter(
    private val hierarchicList: ArrayList<HierarchicEntity>,
    val onClickListener: (HierarchicEntity) -> Unit
) : RecyclerView.Adapter<HierarchicViewAdapter.ViewHolder>() {

    private var processedHierarchicList: ArrayList<HierarchicEntity> = ArrayList(hierarchicList.map { it })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_hierarchic_view, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(processedHierarchicList[position])
    }

    override fun getItemCount(): Int {
        return processedHierarchicList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView = itemView.findViewById(R.id.imageViewExpandIcon)
        private val textView: TextView = itemView.findViewById(R.id.textViewSubject)

        internal fun bindItems(hierarchicEntity: HierarchicEntity) = with(itemView) {
            if (hasChildren(hierarchicEntity, hierarchicList)) {
                imageView.visibility = View.VISIBLE
                if (hierarchicEntity.isExpanded)
                    imageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_expand_less))
                else
                    imageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_expand_more))
            } else {
                imageView.visibility = View.INVISIBLE
            }
            imageView.setPadding(
                context.resources.getDimension(R.dimen.padding_large).toInt() * getHierarchyLevel(
                    hierarchicEntity,
                    processedHierarchicList
                ),
                0,
                0,
                0
            )
            textView.text = hierarchicEntity.name
            setOnClickListener {
                expandOrCollapse(hierarchicEntity, processedHierarchicList)
                onClickListener(hierarchicEntity)
            }
        }
    }

    private fun getHierarchyLevel(hierarchicEntity: HierarchicEntity, listItems: ArrayList<HierarchicEntity>): Int {
        var entity: HierarchicEntity? = hierarchicEntity
        var hierarchyLevel = 0
        while (entity?.parentItemName != null) {
            hierarchyLevel++
            entity = listItems.firstOrNull { it.name == entity?.parentItemName }
        }
        return hierarchyLevel
    }

    private fun expandOrCollapse(
        hierarchicEntity: HierarchicEntity,
        listItems: ArrayList<HierarchicEntity>
    ) {
        val updatedListItems = if (hierarchicEntity.isExpanded) {
            collapseHierarchicEntities(hierarchicEntity, listItems)
        } else {
            expandHierarchicEntities(hierarchicEntity, listItems)
        }
        processedHierarchicList = updatedListItems
        notifyDataSetChanged()
    }

    private fun expandHierarchicEntities(
        hierarchicEntity: HierarchicEntity,
        listItems: ArrayList<HierarchicEntity>
    ): ArrayList<HierarchicEntity> {

        hierarchicEntity.isExpanded = true
        listItems.addAll(
            listItems.indexOf(hierarchicEntity) + 1,
            ArrayList(hierarchicList.filter { it.parentItemName == hierarchicEntity.name })
        )
        return listItems
    }

    private fun collapseHierarchicEntities(
        hierarchicEntity: HierarchicEntity,
        listItems: ArrayList<HierarchicEntity>
    ): ArrayList<HierarchicEntity> {

        hierarchicEntity.isExpanded = false
        val namesToEliminate: ArrayList<String> = ArrayList(
            listItems.filter { it.parentItemName == hierarchicEntity.name }
                .map { it.name }
                .distinct()
        )
        namesToEliminate.addAll(ArrayList(
            listItems.filter { namesToEliminate.contains(it.parentItemName) }
                .map { it.name }
                .distinct()))
        return ArrayList(listItems.filter { !namesToEliminate.contains(it.name) })
    }

    private fun hasChildren(
        hierarchicEntity: HierarchicEntity,
        listItems: ArrayList<HierarchicEntity>
    ): Boolean {

        return !listItems.filter { it.parentItemName == hierarchicEntity.name }.isNullOrEmpty()
    }
}