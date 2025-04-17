package com.example.icecream.data.mapper

import com.example.icecream.data.local.entity.CartItemEntity
import com.example.icecream.data.local.entity.CartItemWithExtras
import com.example.icecream.data.local.entity.ExtraCategoryWithExtras
import com.example.icecream.presentation.model.CartItemUIModel
import com.example.icecream.presentation.model.CartItemWithExtrasUIModel
import com.example.icecream.presentation.model.ExtraUIModel
import javax.inject.Inject

class CartMapper @Inject constructor(
    private val iceCreamMapper: IceCreamMapper,
    private val extraMapper: ExtraMapper
) {

    fun mapToUIModel(
        cartItemWithExtras: CartItemWithExtras,
        iceCreamPrice: Double,
        categoriesWithExtras: List<ExtraCategoryWithExtras>
    ): CartItemWithExtrasUIModel {

        val iceCreamUIModel = iceCreamMapper.mapToUIModel(
            cartItemWithExtras.iceCream,
            iceCreamPrice
        )
        val extrasUIModels = cartItemWithExtras.extras.map { extra ->
            val categoryWithExtras = categoriesWithExtras.find {

                it.category.id == extra.categoryId
            } ?: throw IllegalArgumentException("Category not found for extra: ${extra.name} (id: ${extra.id})")

            ExtraUIModel(
                id = extra.id,
                name = extra.name,
                price = extra.price,
                category = extraMapper.mapToExtraCategoryUIModel(categoryWithExtras.category),
                nameResId = extra.nameResId
            )
        }
        return CartItemWithExtrasUIModel(
            cartItem = CartItemUIModel.Existing(
                id = cartItemWithExtras.cartItem.id,
                iceCream = iceCreamUIModel

            ),
            extras = extrasUIModels
        )

    }


    fun mapToUIModelList(
        cartItemsWithExtras: List<CartItemWithExtras>,
        iceCreamPrice: Double, categoriesWithExtras: List<ExtraCategoryWithExtras>
    ): List<CartItemWithExtrasUIModel> {
        return cartItemsWithExtras.map { mapToUIModel(it, iceCreamPrice, categoriesWithExtras) }
    }

    fun mapToEntity(cartItem: CartItemUIModel): CartItemEntity {
        return when (cartItem) {
            is CartItemUIModel.Existing -> CartItemEntity(
                id = cartItem.id,
                iceCreamId = cartItem.iceCream.id
            )
            is CartItemUIModel.New -> CartItemEntity(
                id = 0L,
                iceCreamId = cartItem.iceCream.id
            )
        }
    }

}