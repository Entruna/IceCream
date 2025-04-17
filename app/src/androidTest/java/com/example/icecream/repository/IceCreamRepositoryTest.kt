package com.example.icecream.repository

import com.example.icecream.data.local.dao.BasePriceDao
import com.example.icecream.data.local.dao.IceCreamDao
import com.example.icecream.domain.repository.IceCreamRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class IceCreamRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var iceCreamDao: IceCreamDao

    @Inject
    lateinit var basePriceDao: BasePriceDao

    @Inject
    lateinit var repository: IceCreamRepository

    @Before
    fun setUp() = runTest {
        hiltRule.inject()
        iceCreamDao.deleteIceCreams()
        basePriceDao.deleteBasePrice()
    }

    @Test
    fun testFetchAndStoreIceCreams() = runTest {
        repository.fetchAndStoreIceCreams()

        val iceCreams = iceCreamDao.getAllIceCreams()
        val price = basePriceDao.getBasePrice()

        assertEquals("Should store 1 ice cream", 1, iceCreams.size)
        assertEquals("Base price should be 2.5", 2.5, price)
    }
}
