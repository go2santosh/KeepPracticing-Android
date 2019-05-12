package com.go2santosh.keeppracticing.userinterfaces.contents

import android.app.Activity
import android.os.Bundle
import com.go2santosh.keeppracticing.R
import com.go2santosh.keeppracticing.models.quizcontents.QuizContentsDataProvider
import kotlinx.android.synthetic.main.activity_contents.*
import android.widget.Toast


class ContentsActivity : Activity() {

    private var listItems: ArrayList<ContentsItem> = getContents()
    private lateinit var adapter: ContentsItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents)

        adapter = ContentsItemAdapter(this, R.layout.list_item_subject, listItems)
        listViewContents.adapter = adapter
        listViewContents.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "Position Clicked:"+" "+position,Toast.LENGTH_SHORT).show()
            expandOrCollapseListItems(position)
        }

        loadContentsItems()
    }

    private fun getContents(): ArrayList<ContentsItem> {

        val arrayList = ArrayList<ContentsItem>()
        QuizContentsDataProvider.topics
            .map { it.grade!! }
            .distinct()
            .map { grade ->
                arrayList.add(ContentsItem(grade))
                QuizContentsDataProvider.topics
                    .filter { it.grade == grade }
                    .map { it.subject!! }
                    .distinct()
                    .map { subject ->
                        arrayList.add(ContentsItem(subject, grade))
                        QuizContentsDataProvider.topics
                            .filter { it.grade == grade && it.subject == subject }
                            .map { it.topic!! }
                            .distinct()
                            .map { topic ->
                                arrayList.add(ContentsItem(topic, subject))
                            }
                    }
            }
        return arrayList
    }

    private fun loadContentsItems() {
        val grades = ArrayList(QuizContentsDataProvider.topics
            .map { it.grade!! }
            .distinct()
            .map { ContentsItem(it) })
        adapter.setList(grades)
    }

    private fun expandOrCollapseListItems(position: Int) {
        if (listItems[position].isExpanded) collapseListItems(position) else expandListItems(position)
        adapter.setList(listItems)
    }

    private fun expandListItems(position: Int) {
        listItems[position].isExpanded = true
        if (listItems[position].parentItemName == null) {
            val subjects = ArrayList(QuizContentsDataProvider.topics
                .filter { it.grade == listItems[position].name }
                .map { it.subject!! }
                .distinct()
                .map { ContentsItem(it, listItems[position].name ) })
            listItems.addAll(position + 1, subjects)
        } else {
            val topics = ArrayList(QuizContentsDataProvider.topics
                .filter { it.subject == listItems[position].name }
                .map { it.topic!! }
                .distinct()
                .map { ContentsItem(it, listItems[position].name ) })
            listItems.addAll(position + 1, topics)
        }
    }

    private fun collapseListItems(position: Int) {
        listItems[position].isExpanded = false
        val namesToEliminate: ArrayList<String> = ArrayList(listItems.filter { it.parentItemName == listItems[position].name }.map { it.name }.distinct())
        namesToEliminate.addAll(ArrayList(listItems.filter { namesToEliminate.contains(it.parentItemName) }.map { it.name }.distinct()))
        listItems = ArrayList(listItems.filter { !namesToEliminate.contains(it.name) })
    }
}
