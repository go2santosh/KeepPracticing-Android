package com.go2santosh.keeppracticing.userinterfaces.contents

import android.app.Activity
import android.os.Bundle
import com.go2santosh.keeppracticing.R
import com.go2santosh.keeppracticing.models.quizcontents.QuizContentsDataProvider
import kotlinx.android.synthetic.main.activity_contents.*
import android.widget.Toast

class ContentsActivity : Activity() {

    private var hierarchicList: ArrayList<HierarchicEntity> = getContents()
    private lateinit var adapter: HierarchicViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents)

        adapter = HierarchicViewAdapter(this, R.layout.list_item_subject, hierarchicList)
        listViewContents.adapter = adapter
        listViewContents.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "Position Clicked:"+" "+position,Toast.LENGTH_SHORT).show()
            expandOrCollapseHierarchicEntities(position)
        }

        loadContentsItems()
    }

    private fun getContents(): ArrayList<HierarchicEntity> {

        val arrayList = ArrayList<HierarchicEntity>()
        QuizContentsDataProvider.topics
            .map { it.grade!! }
            .distinct()
            .map { grade ->
                arrayList.add(HierarchicEntity(grade))
                QuizContentsDataProvider.topics
                    .filter { it.grade == grade }
                    .map { it.subject!! }
                    .distinct()
                    .map { subject ->
                        arrayList.add(HierarchicEntity(subject, grade))
                        QuizContentsDataProvider.topics
                            .filter { it.grade == grade && it.subject == subject }
                            .map { it.topic!! }
                            .distinct()
                            .map { topic ->
                                arrayList.add(HierarchicEntity(topic, subject))
                            }
                    }
            }
        return arrayList
    }

    private fun loadContentsItems() {
        val grades = ArrayList(QuizContentsDataProvider.topics
            .map { it.grade!! }
            .distinct()
            .map { HierarchicEntity(it) })
        adapter.setList(grades)
    }

    private fun expandOrCollapseHierarchicEntities(position: Int) {
        if (hierarchicList[position].isExpanded) {
            collapseHierarchicEntities(position)
        } else {
            expandHierarchicEntities(position)
        }
        adapter.setList(hierarchicList)
    }

    private fun expandHierarchicEntities(position: Int) {
        hierarchicList[position].isExpanded = true
        if (hierarchicList[position].parentItemName == null) {
            val subjects = ArrayList(QuizContentsDataProvider.topics
                .filter { it.grade == hierarchicList[position].name }
                .map { it.subject!! }
                .distinct()
                .map { HierarchicEntity(it, hierarchicList[position].name) })
            hierarchicList.addAll(position + 1, subjects)
        } else {
            val topics = ArrayList(QuizContentsDataProvider.topics
                .filter { it.subject == hierarchicList[position].name }
                .map { it.topic!! }
                .distinct()
                .map { HierarchicEntity(it, hierarchicList[position].name) })
            hierarchicList.addAll(position + 1, topics)
        }
    }

    private fun collapseHierarchicEntities(position: Int) {
        hierarchicList[position].isExpanded = false
        val namesToEliminate: ArrayList<String> = ArrayList(
            hierarchicList.filter { it.parentItemName == hierarchicList[position].name }
                .map { it.name }
                .distinct()
        )
        namesToEliminate.addAll(ArrayList(hierarchicList.filter { namesToEliminate.contains(it.parentItemName) }.map { it.name }.distinct()))
        hierarchicList = ArrayList(hierarchicList.filter { !namesToEliminate.contains(it.name) })
    }
}
