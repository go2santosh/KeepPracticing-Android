package com.go2santosh.keeppracticing.userinterfaces.contents

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class HierarchicViewAdapter(
    val resource: Int,
    private val hierarchicList: ArrayList<HierarchicEntity>,
    val viewHandler: (itemView: View, hierarchicEntity: HierarchicEntity) -> Unit,
    val clickHandler: (HierarchicEntity) -> Unit
) : RecyclerView.Adapter<HierarchicViewAdapter.ViewHolder>() {

    private var processedHierarchicList: ArrayList<HierarchicEntity> = ArrayList(hierarchicList.map { it })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(resource, parent, false)
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) = holder.bindItems(processedHierarchicList[position])

    override fun getItemCount() = processedHierarchicList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal fun bindItems(hierarchicEntity: HierarchicEntity) {
            viewHandler(itemView, hierarchicEntity)
            itemView.setOnClickListener {
                expandOrCollapse(hierarchicEntity = hierarchicEntity, listItems = processedHierarchicList)
                clickHandler(hierarchicEntity)
            }
        }
    }

    private fun expandOrCollapse(
        hierarchicEntity: HierarchicEntity,
        listItems: ArrayList<HierarchicEntity>
    ) {

        val updatedListItems = if (hierarchicEntity.isExpanded) {
            collapseHierarchicEntities(hierarchicEntity = hierarchicEntity, listItems = listItems)
        } else {
            expandHierarchicEntities(hierarchicEntity = hierarchicEntity, listItems = listItems)
        }
        processedHierarchicList = updatedListItems
        notifyDataSetChanged()
    }

    private fun expandHierarchicEntities(
        hierarchicEntity: HierarchicEntity,
        listItems: ArrayList<HierarchicEntity>
    ): ArrayList<HierarchicEntity> {

        hierarchicEntity.isExpanded = true
        val expandedItems = ArrayList(hierarchicList.filter { it.parentId == hierarchicEntity.id })
        expandedItems.forEach { it.isExpanded = false }
        listItems.addAll(
            listItems.indexOf(hierarchicEntity) + 1,
            expandedItems
        )
        return listItems
    }

    private fun collapseHierarchicEntities(
        hierarchicEntity: HierarchicEntity,
        listItems: ArrayList<HierarchicEntity>
    ): ArrayList<HierarchicEntity> {

        hierarchicEntity.isExpanded = false

        val idsToEliminate = ArrayList<String>()

        idsToEliminate.addAll(ArrayList(
            listItems.filter { it.parentId == hierarchicEntity.id }
                .map { it.id }
                .distinct()
        ))

        var beforeCount = -1
        while (beforeCount != idsToEliminate.size) {
            beforeCount = idsToEliminate.size
            idsToEliminate.addAll(ArrayList(
                listItems.filter { idsToEliminate.contains(it.parentId) && !idsToEliminate.contains(it.id) }
                    .map { it.id }
                    .distinct()))
        }

        return ArrayList(listItems.filter { !idsToEliminate.contains(it.id) })
    }
}