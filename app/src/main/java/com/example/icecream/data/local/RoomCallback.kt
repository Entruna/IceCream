package com.example.icecream.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.icecream.common.model.Status
import com.example.icecream.data.local.entity.BasePriceEntity
import com.example.icecream.data.local.entity.ExtraCategoryEntity
import com.example.icecream.data.local.entity.ExtraEntity
import com.example.icecream.data.local.entity.IceCreamEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomCallback @Inject constructor(
    private val database: AppDatabase
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            val iceCreams = listOf(
                IceCreamEntity(
                    1,
                    "Vanília",
                    Status.AVAILABLE,
                    "https://raw.githubusercontent.com/udemx/hr-resources/master/images/vanilia.jpg"
                ),
                IceCreamEntity(
                    2,
                    "Karamell",
                    Status.AVAILABLE,
                    "https://raw.githubusercontent.com/udemx/hr-resources/master/images/karamell.jpg"
                ),
                IceCreamEntity(3, "Tutti-frutti", Status.AVAILABLE, null),
                IceCreamEntity(
                    4,
                    "Csokoládé",
                    Status.UNAVAILABLE,
                    "https://raw.githubusercontent.com/udemx/hr-resources/master/images/csokolade.jpg"
                ),
                IceCreamEntity(
                    5,
                    "Rumosdió",
                    Status.AVAILABLE,
                    "https://raw.githubusercontent.com/udemx/hr-resources/master/images/dio.jpg"
                ),
                IceCreamEntity(
                    6,
                    "Kávé",
                    Status.AVAILABLE,
                    "https://raw.githubusercontent.com/udemx/hr-resources/master/images/kave.jpg"
                ),

                IceCreamEntity(
                    7,
                    "Pisztácia",
                    Status.MELTED,
                    "https://raw.githubusercontent.com/udemx/hr-resources/master/images/pisztacia.jpg"
                )
            )

            val categories = listOf(
                ExtraCategoryEntity(1, "Tölcsérek", true),
                ExtraCategoryEntity(2, "Egyéb"),
                ExtraCategoryEntity(3, "Öntetek")
            )

            val extras = listOf(
                ExtraEntity(1, "Normál tölcsér", 0.0, 1),
                ExtraEntity(2, "Édes tölcsér", 1.0, 1),
                ExtraEntity(3, "Csokis tölcsér", 2.0, 1),
                ExtraEntity(4, "Kehely", 5.0, 1),
                ExtraEntity(5, "Cukorvarázs", 4.0, 2),
                ExtraEntity(6, "Roletti", 2.0, 2),
                ExtraEntity(7, "Ostya", 2.0, 2),
                ExtraEntity(8, "Eper öntet", 1.0, 3),
                ExtraEntity(9, "Vanília öntet", 1.0, 3)
            )

            val basePrice = BasePriceEntity(price = 1.0)

            database.iceCreamDao().insertIceCreams(iceCreams)
            database.extraDao().insertExtrasAndCategories(categories, extras)
            database.basePriceDao().insertBasePrice(basePrice)
        }
    }
}
