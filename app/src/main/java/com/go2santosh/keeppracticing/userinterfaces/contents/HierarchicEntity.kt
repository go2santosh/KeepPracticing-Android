package com.go2santosh.keeppracticing.userinterfaces.contents

class HierarchicEntity(
    val name: String,
    val parentItemName: String? = null
) {
    var isExpanded = false
}