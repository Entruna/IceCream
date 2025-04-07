import android.content.Context
import androidx.room.Room
import com.example.icecream.data.local.AppDatabase
import com.example.icecream.data.local.RoomCallback
import com.example.icecream.data.local.dao.BasePriceDao
import com.example.icecream.data.local.dao.CartDao
import com.example.icecream.data.local.dao.ExtraDao
import com.example.icecream.data.local.dao.IceCreamDao
import com.example.icecream.data.remote.IceCreamApi
import com.example.icecream.data.repository.CartRepositoryImpl
import com.example.icecream.data.repository.ExtraRepositoryImpl
import com.example.icecream.domain.repository.IceCreamRepository
import com.example.icecream.data.repository.IceCreamRepositoryImpl
import com.example.icecream.domain.repository.CartRepository
import com.example.icecream.domain.repository.ExtraRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://httpbin.org/"

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideIceCreamApi(retrofit: Retrofit): IceCreamApi {
        return retrofit.create(IceCreamApi::class.java)
    }

    @Provides
    fun provideRoomCallback(
        database: AppDatabase
    ): RoomCallback = RoomCallback(database)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context, callback: RoomCallback): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "icecream_db"
        ).addCallback(callback)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideIceCreamDao(db: AppDatabase): IceCreamDao = db.iceCreamDao()

    @Provides
    fun provideBasePriceDao(db: AppDatabase): BasePriceDao = db.basePriceDao()

    @Provides
    fun provideCartDao(db: AppDatabase): CartDao = db.cartDao()

    @Provides
    fun provideExtraDao(db: AppDatabase): ExtraDao = db.extraDao()

    @Provides
    @Singleton
    fun provideIceCreamRepository(
        iceCreamDao: IceCreamDao,

        iceCreamApi: IceCreamApi
    ): IceCreamRepository {
        return IceCreamRepositoryImpl(iceCreamDao, iceCreamApi)
    }

    @Provides
    @Singleton
    fun provideExtraRepository(
        extraDao: ExtraDao,
        iceCreamApi: IceCreamApi
    ): ExtraRepository {
        return ExtraRepositoryImpl(extraDao, iceCreamApi)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        cartDao: CartDao,
        iceCreamApi: IceCreamApi
    ): CartRepository {
        return CartRepositoryImpl(cartDao, iceCreamApi)
    }
}
