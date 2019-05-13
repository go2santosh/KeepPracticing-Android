package com.go2santosh.keeppracticing.userinterfaces.contents

import android.app.Activity
import android.os.Bundle
import com.go2santosh.keeppracticing.R
import com.go2santosh.keeppracticing.models.quizcontents.QuizContentsDataProvider
import kotlinx.android.synthetic.main.activity_contents.*
import android.widget.Toast
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.DefaultItemAnimator

class ContentsActivity : Activity() {

    private val hierarchicList: ArrayList<HierarchicEntity> = getContents()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents)

        val adapter = HierarchicViewAdapter(hierarchicList) {
            Toast.makeText(this, "Tapped on ${it.name}", Toast.LENGTH_SHORT).show()
        }
        val layoutManager = LinearLayoutManager(applicationContext)
        listViewContents.layoutManager = layoutManager
        listViewContents.itemAnimator = DefaultItemAnimator()
        listViewContents.adapter = adapter
    }

    private fun getContents(): ArrayList<HierarchicEntity> {

        val arrayList = ArrayList<HierarchicEntity>()
        QuizContentsDataProvider.topics
            .map { it.grade!! }
            .distinct()
            .map { grade ->
                val gradeEntity = HierarchicEntity(name = grade)
                arrayList.add(gradeEntity)
                QuizContentsDataProvider.topics
                    .filter { it.grade == grade }
                    .map { it.subject!! }
                    .distinct()
                    .map { subject ->
                        val subjectEntity = HierarchicEntity(name = subject, parentId = gradeEntity.id)
                        arrayList.add(subjectEntity)
                        QuizContentsDataProvider.topics
                            .filter { it.grade == grade && it.subject == subject }
                            .map { it.domain!! }
                            .distinct()
                            .map { domain ->
                                val domainEntity = HierarchicEntity(name = domain, parentId = subjectEntity.id)
                                arrayList.add(domainEntity)
                                QuizContentsDataProvider.topics
                                    .filter { it.grade == grade && it.subject == subject && it.domain == domain }
                                    .map { it.topic!! }
                                    .distinct()
                                    .map { topic ->
                                        val topicEntity = HierarchicEntity(name = topic, parentId = domainEntity.id)
                                        arrayList.add(topicEntity)
                                    }
                            }
                    }
            }
        return arrayList
    }
}
