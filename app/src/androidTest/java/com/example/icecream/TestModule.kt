package com.example.icecream

import android.content.Context
import androidx.room.Room
import com.example.icecream.data.local.AppDatabase
import com.example.icecream.data.local.dao.BasePriceDao
import com.example.icecream.data.local.dao.CartDao
import com.example.icecream.data.local.dao.ExtraDao
import com.example.icecream.data.local.dao.IceCreamDao
import com.example.icecream.data.mapper.CartMapper
import com.example.icecream.data.mapper.ExtraMapper
import com.example.icecream.data.mapper.IceCreamMapper
import com.example.icecream.data.remote.IceCreamApi
import com.example.icecream.data.repository.CartRepositoryImpl
import com.example.icecream.data.repository.ExtraRepositoryImpl
import com.example.icecream.data.repository.IceCreamRepositoryImpl
import com.example.icecream.di.AppModule
import com.example.icecream.domain.repository.CartRepository
import com.example.icecream.domain.repository.ExtraRepository
import com.example.icecream.domain.repository.IceCreamRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestModule {

    @Provides
    @Singleton
    fun provideInMemoryDb(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideIceCreamDao(db: AppDatabase) = db.iceCreamDao()

    @Provides
    fun provideBasePriceDao(db: AppDatabase) = db.basePriceDao()

    @Provides
    fun provideExtraDao(db: AppDatabase) = db.extraDao()

    @Provides
    fun provideCartDao(db: AppDatabase) = db.cartDao()

    @Provides
    fun provideCartMapper(
        iceCreamMapper: IceCreamMapper,
        extraMapper: ExtraMapper
    ): CartMapper = CartMapper(iceCreamMapper, extraMapper)


    @Provides
    @Singleton
    fun provideCartRepository(
        cartDao: CartDao,
        api: IceCreamApi,
    ): CartRepository = CartRepositoryImpl(cartDao, api)

    @Provides
    @Singleton
    fun provideIceCreamRepository(
        iceCreamDao: IceCreamDao,
        basePriceDao: BasePriceDao,
        api: IceCreamApi,
        mapper: IceCreamMapper
    ): IceCreamRepository = IceCreamRepositoryImpl(
        iceCreamDao,
        basePriceDao,
        api,
        mapper
    )

    @Provides
    @Singleton
    fun provideExtraRepository(
        extraDao: ExtraDao,
        api: IceCreamApi,
        extraMapper: ExtraMapper
    ): ExtraRepository = ExtraRepositoryImpl(extraDao, api, extraMapper)

    @Provides
    @Singleton
    fun provideTestApi(): IceCreamApi = TestIceCreamApi()
}
