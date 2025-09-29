package com.example.icecream.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icecream.common.model.OrderStatus
import com.example.icecream.data.mapper.CartMapper
import com.example.icecream.data.remote.model.IceCreamOrderRequest
import com.example.icecream.domain.repository.CartRepository
import com.example.icecream.domain.repository.ExtraRepository
import com.example.icecream.domain.repository.IceCreamRepository
import com.example.icecream.presentation.model.CartItemUIModel
import com.example.icecream.presentation.model.CartItemWithExtrasUIModel
import com.example.icecream.presentation.model.ExtraUIModel
import com.example.icecream.presentation.model.IceCreamUIModel
import com.example.icecream.presentation.model.calculatePrice
import com.example.icecream.presentation.viewmodel.extension.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val cartMapper: CartMapper,
    private val iceCreamRepository: IceCreamRepository,
    private val extraRepository: ExtraRepository
) : ViewModel() {

    val cartItems: StateFlow<List<CartItemWithExtrasUIModel>> =
        combine(
            cartRepository.getAllCartItemsWithExtrasFlow(),
            iceCreamRepository.getBasePriceFlow()
        ) { cartItemsList, basePrice ->
            val extraIds = cartItemsList.flatMap { it.extras }.map { it.id }.distinct()
            val categories = extraRepository.getCategoriesWithExtrasByExtraIds(extraIds)

            cartMapper.mapToUIModelList(cartItemsList, basePrice, categories)
                .map { it.copy(calculatedPrice = it.calculatePrice()) }
        }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _orderStatus = MutableStateFlow<OrderStatus?>(null)
    val orderStatus: StateFlow<OrderStatus?> = _orderStatus.asStateFlow()

    val totalPrice: StateFlow<Double> = cartItems
        .map { cartItems ->
            cartItems.sumOf { it.calculatedPrice }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    fun addToCart(iceCream: IceCreamUIModel, selectedExtras: List<ExtraUIModel>) {
        launchIO {
            val cartItem = CartItemUIModel.New(
                iceCream = iceCream,
            )
            val extraIds = selectedExtras.map { it.id }
            val cartItemEntity = cartMapper.mapToEntity(cartItem)
            cartRepository.addIceCreamWithExtras(cartItemEntity, extraIds)
        }
    }

    fun removeIceCream(cartItem: CartItemUIModel) {
        launchIO {
            val cartItemEntity = cartMapper.mapToEntity(cartItem)
            cartRepository.removeIceCream(cartItemEntity)
        }
    }

    fun removeExtra(cartItemId: Long, extraId: Long) {
        launchIO {
            cartRepository.removeExtra(cartItemId, extraId)
        }
    }

    fun updateCartItemExtras(cartItemWithExtras: CartItemWithExtrasUIModel) {
        launchIO {
            val extraIds = cartItemWithExtras.extras.map { it.id }

            val cartItemEntity = cartMapper.mapToEntity(cartItemWithExtras.cartItem)
            cartRepository.updateCartItemExtras(cartItemEntity, extraIds)

        }
    }

    fun submitOrder() {
        launchIO {
            val uiModels = cartItems.value

            val orderRequest = uiModels.map {
                IceCreamOrderRequest(
                    id = it.cartItem.iceCream.id,
                    extra = it.extras.map { extra -> extra.id }
                )
            }

            val response = cartRepository.submitOrder(orderRequest)

            _orderStatus.value = if (response.isSuccessful) {
                cartRepository.clearCart()
                OrderStatus.SUCCESS
            } else {
                OrderStatus.ERROR
            }
        }
    }
}
