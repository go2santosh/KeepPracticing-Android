package com.go2santosh.keeppracticing.userinterfaces.contents

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.go2santosh.keeppracticing.R
import com.go2santosh.keeppracticing.models.quizcontents.QuizContentsDataProvider
import kotlinx.android.synthetic.main.activity_contents.*
import android.widget.Toast
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.DefaultItemAnimator
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.go2santosh.keeppracticing.userinterfaces.QuizActivity

class ContentsActivity : Activity() {

    private val hierarchicList: ArrayList<HierarchicEntity> = getContents()

    private val viewHandler: (
        itemView: View,
        hierarchicEntity: HierarchicEntity
    ) -> Unit = { itemView, hierarchicEntity ->

        val imageView: ImageView = itemView.findViewById(R.id.imageViewExpandIcon)
        val textView: TextView = itemView.findViewById(R.id.textViewSubject)
        val buttonStart: Button = itemView.findViewById(R.id.buttonStart)
        if (hasChildren(hierarchicEntity = hierarchicEntity, listItems = hierarchicList)) {
            imageView.visibility = View.VISIBLE
            buttonStart.visibility = View.INVISIBLE
            if (hierarchicEntity.isExpanded)
                imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_expand_less))
            else
                imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_expand_more))
        } else {
            imageView.visibility = View.INVISIBLE
            buttonStart.visibility = View.VISIBLE
            buttonStart.apply { setOnClickListener { startQuiz(hierarchicEntity) } }
        }
        imageView.setPadding(
            resources.getDimension(R.dimen.padding_large).toInt() *
                    getHierarchyLevel(hierarchicEntity = hierarchicEntity, listItems = hierarchicList),
            0,
            0,
            0
        )
        textView.text = hierarchicEntity.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents)

        val adapter = HierarchicViewAdapter(
            resource = R.layout.list_item_hierarchic_view,
            viewHandler = viewHandler,
            hierarchicList = hierarchicList
        ) {
            Toast.makeText(this, "Tapped on ${it.name}", Toast.LENGTH_SHORT).show()
        }
        val layoutManager = LinearLayoutManager(applicationContext)
        listViewContents.layoutManager = layoutManager
        listViewContents.itemAnimator = DefaultItemAnimator()
        listViewContents.adapter = adapter
    }

    private fun hasChildren(
        hierarchicEntity: HierarchicEntity,
        listItems: ArrayList<HierarchicEntity>
    ) = !listItems.filter { it.parentId == hierarchicEntity.id }.isNullOrEmpty()

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

    private fun startQuiz(hierarchicEntity: HierarchicEntity) {
        val domainHierarchicEntity = hierarchicList.first { it.id == hierarchicEntity.parentId }
        val subjectHierarchicEntity = hierarchicList.first { it.id == domainHierarchicEntity.parentId }
        val gradeHierarchicEntity = hierarchicList.first { it.id == subjectHierarchicEntity.parentId }
        val quizTopic = QuizContentsDataProvider.topics.first {
            it.grade == gradeHierarchicEntity.name
                    && it.subject == subjectHierarchicEntity.name
                    && it.domain == domainHierarchicEntity.name
                    && it.topic == hierarchicEntity.name
        }
        val intent = Intent(applicationContext, QuizActivity::class.java)
        intent.putExtra("quizTopic", quizTopic.topic)
        intent.putExtra("quizFileName", quizTopic.quizFileName)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
