package com.example.icecream.mapper

import com.example.icecream.common.model.Status
import com.example.icecream.data.local.entity.CartItemEntity
import com.example.icecream.data.local.entity.CartItemWithExtras
import com.example.icecream.data.local.entity.ExtraCategoryEntity
import com.example.icecream.data.local.entity.ExtraCategoryWithExtras
import com.example.icecream.data.local.entity.ExtraEntity
import com.example.icecream.data.local.entity.IceCreamEntity
import com.example.icecream.data.mapper.CartMapper
import com.example.icecream.data.mapper.ExtraMapper
import com.example.icecream.data.mapper.IceCreamMapper
import com.example.icecream.presentation.model.CartItemUIModel
import com.example.icecream.presentation.model.ExtraCategoryUIModel
import com.example.icecream.presentation.model.IceCreamUIModel
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class CartMapperTest {

    private val iceCreamMapper: IceCreamMapper = mockk()
    private val extraMapper: ExtraMapper = mockk()
    private val cartMapper = CartMapper(iceCreamMapper, extraMapper)

    @Before
    fun setup() {
        every { extraMapper.mapToExtraCategoryUIModel(any()) } returns
                ExtraCategoryUIModel(1L, "Toppings", true, 123)

        every {
            iceCreamMapper.mapToUIModel(any(), any())
        } returns IceCreamUIModel(
            id = 1L,
            name = "Vanilla",
            imageUrl = "image_url",
            status = Status.AVAILABLE,
            price = 5.0,
            nameResId = 123
        )
    }

    @Test
    fun testMapToUIModel() {
        val cartItemWithExtras = CartItemWithExtras(
            cartItem = CartItemEntity(id = 1L, iceCreamId = 1L),
            iceCream = IceCreamEntity(
                id = 1L,
                name = "Vanilla",
                imageUrl = "image_url",
                status = Status.AVAILABLE,
                nameResId = 123
            ),
            extras = listOf(
                ExtraEntity(
                    id = 1L,
                    name = "Choco Chips",
                    price = 1.0,
                    categoryId = 1L,
                    nameResId = 456
                )
            )
        )
        val iceCreamPrice = 5.0
        val categoriesWithExtras = listOf(
            ExtraCategoryWithExtras(
                category = ExtraCategoryEntity(1L, "Toppings", true, 123),
                extras = listOf(
                    ExtraEntity(
                        id = 1L,
                        name = "Choco Chips",
                        price = 1.0,
                        categoryId = 1L,
                        nameResId = 456
                    )
                )
            )
        )

        val result =
            cartMapper.mapToUIModel(cartItemWithExtras, iceCreamPrice, categoriesWithExtras)

        val existingItem = result.cartItem as CartItemUIModel.Existing

        TestCase.assertEquals(cartItemWithExtras.cartItem.id, existingItem.id)
        TestCase.assertEquals(cartItemWithExtras.iceCream.name, result.cartItem.iceCream.name)
        TestCase.assertEquals(cartItemWithExtras.extras.size, result.extras.size)
    }

    @Test
    fun testMapToEntity() {
        val cartItemUIModel = CartItemUIModel.Existing(
            id = 1L,
            iceCream = IceCreamUIModel(
                id = 1L,
                name = "Vanilla",
                imageUrl = "image_url",
                status = Status.AVAILABLE,
                price = 5.0,
                nameResId = 123
            )
        )

        val result = cartMapper.mapToEntity(cartItemUIModel)

        TestCase.assertEquals(cartItemUIModel.id, result.id)
        TestCase.assertEquals(cartItemUIModel.iceCream.id, result.iceCreamId)
    }

    @Test
    fun testMapToUIModelList() {

        val item = CartItemWithExtras(
            cartItem = CartItemEntity(1L, 1L),
            iceCream = IceCreamEntity(1L, "Vanilla", Status.AVAILABLE, "url", 123),
            extras = listOf(ExtraEntity(1L, "Choco", 1.0, 1L, 456))
        )

        val categories = listOf(
            ExtraCategoryWithExtras(
                category = ExtraCategoryEntity(1L, "Toppings", true, 101),
                extras = item.extras
            )
        )

        val result = cartMapper.mapToUIModelList(listOf(item), 5.0, categories)

        TestCase.assertEquals(1, result.size)
        val existingItem = result[0].cartItem as CartItemUIModel.Existing
        TestCase.assertEquals(1L, existingItem.id)
        TestCase.assertEquals("Vanilla", existingItem.iceCream.name)
    }

}