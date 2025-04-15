package com.example.icecream.common.res

import com.example.icecream.R

object ExtraStringResourceMapper {
    private val nameToResIdMap = mapOf(
        "Normál tölcsér" to R.string.normal_cone,
        "Édes tölcsér" to R.string.sweet_cone,
        "Csokis tölcsér" to R.string.chocolate_cone,
        "Kehely" to R.string.cup,
        "Cukorvarázs" to R.string.sugar_magic,
        "Roletti" to R.string.roletti,
        "Ostya" to R.string.waffle,
        "Eper öntet" to R.string.strawberry_syrup,
        "Vanília öntet" to R.string.vanilla_syrup
    )

    fun getResIdByName(name: String): Int? = nameToResIdMap[name]
}
