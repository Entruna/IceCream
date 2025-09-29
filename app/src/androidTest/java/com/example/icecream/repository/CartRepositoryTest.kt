package com.example.icecream.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.icecream.common.model.Status
import com.example.icecream.data.local.dao.CartDao
import com.example.icecream.data.local.dao.ExtraDao
import com.example.icecream.data.local.dao.IceCreamDao
import com.example.icecream.data.local.entity.CartItemEntity
import com.example.icecream.data.local.entity.ExtraCategoryEntity
import com.example.icecream.data.local.entity.ExtraEntity
import com.example.icecream.data.local.entity.IceCreamEntity
import com.example.icecream.data.remote.model.IceCreamOrderRequest
import com.example.icecream.domain.repository.CartRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CartRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: CartRepository

    @Inject
    lateinit var cartDao: CartDao

    @Inject
    lateinit var extraDao: ExtraDao

    @Inject
    lateinit var iceCreamDao: IceCreamDao

    @Before
    fun setUp() = runTest {
        hiltRule.inject()
        cartDao.deleteCartItems()
        extraDao.deleteAll()
        iceCreamDao.deleteIceCreams()
    }

    private suspend fun insertSampleIceCream(id: Long = 1L) {
        val vanilla = IceCreamEntity(id, "Vanilla", Status.AVAILABLE, "url")
        iceCreamDao.insertIceCreams(listOf(vanilla))
    }

    private suspend fun insertSampleExtras(): List<Long> {
        val toppingsCategory = ExtraCategoryEntity(1L, "Toppings")
        val oreo = ExtraEntity(10L, "Oreo", 5.0, toppingsCategory.id)
        val sprinkles = ExtraEntity(20L, "Sprinkles", 6.0, toppingsCategory.id)
        extraDao.insertCategories(listOf(toppingsCategory))
        extraDao.insertExtras(listOf(oreo, sprinkles))
        return listOf(oreo.id, sprinkles.id)
    }

    @Test
    fun testAddIceCreamWithExtras() = runTest {
        insertSampleIceCream()
        val extras = insertSampleExtras()
        val cartItem = CartItemEntity(id = 1L, iceCreamId = 1L)

        repository.addIceCreamWithExtras(cartItem, extras)

        val result = repository.getAllCartItemsWithExtrasFlow().first()
        assertEquals("Cart should contain one item", 1, result.size)
        assertEquals(extras.toSet(), result.first().extras.map { it.id }.toSet())
    }

    @Test
    fun testGetAllCartItemsWithExtras() = runTest {
        insertSampleIceCream()
        val cartItem = CartItemEntity(id = 1L, iceCreamId = 1L)
        repository.addIceCreamWithExtras(cartItem, emptyList())

        val result = repository.getAllCartItemsWithExtrasFlow().first()
        assertTrue("Expected non-empty cart item list", result.isNotEmpty())
    }

    @Test
    fun testUpdateCartItemExtras() = runTest {
        insertSampleIceCream()
        insertSampleExtras()
        val cartItem = CartItemEntity(id = 1L, iceCreamId = 1L)

        repository.addIceCreamWithExtras(cartItem, listOf(10L))
        repository.updateCartItemExtras(cartItem, listOf(20L))

        val items = repository.getAllCartItemsWithExtrasFlow().first()
        assertEquals(
            "Extras should be updated",
            listOf(20L),
            items.firstOrNull()?.extras?.map { it.id })
    }

    @Test
    fun testRemoveIceCream() = runTest {
        insertSampleIceCream()
        val cartItem = CartItemEntity(id = 1L, iceCreamId = 1L)
        repository.addIceCreamWithExtras(cartItem, emptyList())
        repository.removeIceCream(cartItem)

        val items = repository.getAllCartItemsWithExtrasFlow().first()
        assertTrue("Cart item should be removed", items.none { it.cartItem.id == cartItem.id })
    }

    @Test
    fun testClearCart() = runTest {
        insertSampleIceCream()
        val cartItem = CartItemEntity(id = 1L, iceCreamId = 1L)
        repository.addIceCreamWithExtras(cartItem, emptyList())
        repository.clearCart()

        val items = repository.getAllCartItemsWithExtrasFlow().first()
        assertEquals("Cart should be empty", 0, items.size)
    }

    @Test
    fun testSubmitOrder() = runTest {
        val order = listOf(IceCreamOrderRequest(id = 1L, extra = emptyList()))
        val response = repository.submitOrder(order)
        assertEquals("Expected 200 response code", 200, response.code())
    }

    @Test
    fun testSubmitOrderThrowException() = runTest {
        val invalidOrder = listOf(IceCreamOrderRequest(id = -1, extra = emptyList()))
        try {
            repository.submitOrder(invalidOrder)
            fail("Expected exception not thrown")
        } catch (e: RuntimeException) {
            assertTrue(e.message?.contains("Invalid ice cream order") == true)
        }
    }
}
