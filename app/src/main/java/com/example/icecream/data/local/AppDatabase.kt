package com.example.icecream.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.icecream.data.local.dao.BasePriceDao
import com.example.icecream.data.local.dao.CartDao
import com.example.icecream.data.local.dao.ExtraDao
import com.example.icecream.data.local.dao.IceCreamDao
import com.example.icecream.data.local.entity.BasePriceEntity
import com.example.icecream.data.local.entity.CartExtraCrossRef
import com.example.icecream.data.local.entity.CartItemEntity
import com.example.icecream.data.local.entity.ExtraCategoryEntity
import com.example.icecream.data.local.entity.ExtraEntity
import com.example.icecream.data.local.entity.IceCreamEntity

@Database(entities = [IceCreamEntity::class, BasePriceEntity::class, CartItemEntity::class, ExtraEntity::class, CartExtraCrossRef::class, ExtraCategoryEntity::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun iceCreamDao(): IceCreamDao
    abstract fun basePriceDao(): BasePriceDao
    abstract fun cartDao(): CartDao
    abstract fun extraDao(): ExtraDao
}