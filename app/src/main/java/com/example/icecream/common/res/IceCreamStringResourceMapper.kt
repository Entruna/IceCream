package com.example.icecream.common.res

import com.example.icecream.R

object IceCreamStringResourceMapper {
    private val nameToResIdMap = mapOf(
        "Vanília" to R.string.vanilla,
        "Karamell" to R.string.caramel,
        "Tutti-frutti" to R.string.tutti_frutti,
        "Csokoládé" to R.string.chocolate,
        "Rumosdió" to R.string.rum_nut,
        "Kávé" to R.string.coffee,
        "Pisztácia" to R.string.pistachio,
    )

    fun getResIdByName(name: String): Int? {
        return nameToResIdMap[name]
    }
}
