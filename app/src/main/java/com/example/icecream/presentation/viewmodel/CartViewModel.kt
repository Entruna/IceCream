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

    private val _cartItems = MutableStateFlow<List<CartItemWithExtrasUIModel>>(emptyList())
    val cartItems: StateFlow<List<CartItemWithExtrasUIModel>> = _cartItems.asStateFlow()

    private val _orderStatus = MutableStateFlow<OrderStatus?>(null)
    val orderStatus: StateFlow<OrderStatus?> = _orderStatus.asStateFlow()

    val totalPrice: StateFlow<Double> = _cartItems
        .map { cartItems ->
            cartItems.sumOf { it.calculatedPrice }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    init {
        loadCartItems()
    }

    private suspend fun getMappedCartItems(): List<CartItemWithExtrasUIModel> {
        val cartItems = cartRepository.getAllCartItemsWithExtras()
        val extraIds = cartItems.flatMap { it.extras }.map { it.id }.distinct()
        val categories = extraRepository.getCategoriesWithExtrasByExtraIds(extraIds)
        val basePrice = iceCreamRepository.getBasePrice()

        val mappedItems =  cartMapper.mapToUIModelList(cartItems, basePrice, categories)

        return mappedItems.map { item ->
            item.copy(calculatedPrice = item.calculatePrice())
        }
    }

    fun loadCartItems() {
        launchIO {
            val items = getMappedCartItems()
            _cartItems.value = items
        }
    }

    fun addToCart(iceCream: IceCreamUIModel, selectedExtras: List<ExtraUIModel>) {
        launchIO {
            val cartItem = CartItemUIModel.New(
                iceCream = iceCream,
            )
            val extraIds = selectedExtras.map { it.id }
            val cartItemEntity = cartMapper.mapToEntity(cartItem)
            cartRepository.addIceCreamWithExtras(cartItemEntity, extraIds)

            loadCartItems()
        }
    }

    fun removeIceCream(cartItem: CartItemUIModel) {
        launchIO {
            val cartItemEntity = cartMapper.mapToEntity(cartItem)
            cartRepository.removeIceCream(cartItemEntity)
            loadCartItems()
        }
    }

    fun removeExtra(cartItemId: Long, extraId: Long) {
        launchIO {
            cartRepository.removeExtra(cartItemId, extraId)
            loadCartItems()
        }
    }

    fun updateCartItemExtras(cartItemWithExtras: CartItemWithExtrasUIModel) {
        launchIO {
            val extraIds = cartItemWithExtras.extras.map { it.id }

            val cartItemEntity = cartMapper.mapToEntity(cartItemWithExtras.cartItem)
            cartRepository.updateCartItemExtras(cartItemEntity, extraIds)

            loadCartItems()
        }
    }

    fun submitOrder() {
        launchIO {
            val uiModels = getMappedCartItems()

            val orderRequest = uiModels.map {
                IceCreamOrderRequest(
                    id = it.cartItem.iceCream.id,
                    extra = it.extras.map { extra -> extra.id }
                )
            }

            val response = cartRepository.submitOrder(orderRequest)

            _orderStatus.value = if (response.isSuccessful) {
                cartRepository.clearCart()
                loadCartItems()
                OrderStatus.SUCCESS
            } else {
                OrderStatus.ERROR
            }
        }
    }
}
