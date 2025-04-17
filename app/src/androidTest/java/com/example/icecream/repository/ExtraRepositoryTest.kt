package com.example.icecream.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.icecream.data.local.dao.ExtraDao
import com.example.icecream.data.local.entity.ExtraCategoryEntity
import com.example.icecream.data.local.entity.ExtraEntity
import com.example.icecream.domain.repository.ExtraRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ExtraRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: ExtraRepository

    @Inject
    lateinit var extraDao: ExtraDao

    @Before
    fun setUp() = runTest {
        hiltRule.inject()
        extraDao.deleteAll()
    }

    @Test
    fun testFetchAndStoreExtras() = runTest {
        repository.fetchAndStoreExtras()

        val categories = extraDao.getAllCategories()
        val extras = extraDao.getAllExtras()

        assertEquals("Should store 1 category", 1, categories.size)
        assertEquals("Should store 2 extras", 2, extras.size)
    }

    @Test
    fun testGetCategoriesWithExtrasByExtraIds() = runTest {
        val categoryEntity = ExtraCategoryEntity(id = 1L, type = "Toppings", required = true)
        val chocoChips = ExtraEntity(id = 1L, name = "Choco Chips", price = 1.0, categoryId = 1L)
        val sprinkles = ExtraEntity(id = 2L, name = "Sprinkles", price = 0.8, categoryId = 1L)

        extraDao.insertCategories(listOf(categoryEntity))
        extraDao.insertExtras(listOf(chocoChips, sprinkles))

        val result = repository.getCategoriesWithExtrasByExtraIds(listOf(1L, 2L))

        assertEquals("Should return 1 category", 1, result.size)
        assertEquals("Should include 2 extras in category", 2, result.first().extras.size)
    }

    @Test
    fun testGetCategoriesWithExtrasFromDb() = runTest {
        val toppingsCategory = ExtraCategoryEntity(id = 1L, type = "Toppings", required = true)
        val chocoChips = ExtraEntity(id = 1L, name = "Choco Chips", price = 1.0, categoryId = 1L)

        extraDao.insertCategories(listOf(toppingsCategory))
        extraDao.insertExtras(listOf(chocoChips))

        val result = repository.getCategoriesWithExtrasFromDb()

        assertEquals("Should return 1 category", 1, result.size)
        assertEquals("Category should contain 1 extra", 1, result.first().extras.size)
    }
}
