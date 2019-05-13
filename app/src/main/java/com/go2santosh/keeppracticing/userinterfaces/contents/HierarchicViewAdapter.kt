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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindItems(processedHierarchicList[position])

    override fun getItemCount() = processedHierarchicList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView = itemView.findViewById(R.id.imageViewExpandIcon)
        private val textView: TextView = itemView.findViewById(R.id.textViewSubject)

        internal fun bindItems(hierarchicEntity: HierarchicEntity) = with(itemView) {
            if (hasChildren(hierarchicEntity = hierarchicEntity, listItems = hierarchicList)) {
                imageView.visibility = View.VISIBLE
                if (hierarchicEntity.isExpanded)
                    imageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_expand_less))
                else
                    imageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_expand_more))
            } else {
                imageView.visibility = View.INVISIBLE
            }
            imageView.setPadding(
                context.resources.getDimension(R.dimen.padding_large).toInt() *
                        getHierarchyLevel(hierarchicEntity = hierarchicEntity, listItems = processedHierarchicList),
                0,
                0,
                0
            )
            textView.text = hierarchicEntity.name
            setOnClickListener {
                expandOrCollapse(hierarchicEntity = hierarchicEntity, listItems = processedHierarchicList)
                onClickListener(hierarchicEntity)
            }
        }
    }

    private fun getHierarchyLevel(
        hierarchicEntity: HierarchicEntity,
        listItems: ArrayList<HierarchicEntity>
    ): Int {

        var entity: HierarchicEntity? = hierarchicEntity
        var hierarchyLevel = 0
        while (entity?.parentId != null) {
            hierarchyLevel++
            entity = listItems.firstOrNull { it.id == entity?.parentId }
        }
        return hierarchyLevel
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

    private fun hasChildren(
        hierarchicEntity: HierarchicEntity,
        listItems: ArrayList<HierarchicEntity>
    ) = !listItems.filter { it.parentId == hierarchicEntity.id }.isNullOrEmpty()
}