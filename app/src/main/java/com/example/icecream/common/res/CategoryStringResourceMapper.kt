package com.example.icecream.common.res

import com.example.icecream.R

object CategoryStringResourceMapper {
    private val nameToResIdMap = mapOf(
        "Tölcsérek" to R.string.cone_category,
        "Egyéb" to R.string.other_category,
        "Öntetek" to R.string.topping_category
    )

    fun getResIdByName(name: String): Int? = nameToResIdMap[name]
}
