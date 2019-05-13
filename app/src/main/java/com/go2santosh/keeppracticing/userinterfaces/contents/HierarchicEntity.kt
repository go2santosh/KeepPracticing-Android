package com.go2santosh.keeppracticing.userinterfaces.contents

import java.util.*

data class HierarchicEntity(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val parentId: String? = null
) {
    internal var isExpanded = true
}