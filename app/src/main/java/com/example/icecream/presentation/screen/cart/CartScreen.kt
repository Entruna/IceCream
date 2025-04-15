package com.example.icecream.presentation.screen.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.icecream.R
import com.example.icecream.common.model.OrderStatus
import com.example.icecream.presentation.component.TopBar
import com.example.icecream.presentation.model.CartItemUIModel
import com.example.icecream.presentation.model.CartItemWithExtrasUIModel
import com.example.icecream.presentation.screen.ExtrasDialog
import com.example.icecream.presentation.viewmodel.CartViewModel
import com.example.icecream.presentation.viewmodel.ExtraViewModel

@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel = hiltViewModel(),
    extraViewModel: ExtraViewModel = hiltViewModel()
) {
    val cartItemsWithExtras by cartViewModel.cartItems.collectAsState()

    val categoriesWithExtras by extraViewModel.categoriesWithExtras.collectAsState()

    val orderStatus by cartViewModel.orderStatus.collectAsState()

    val totalCartPrice by cartViewModel.totalPrice.collectAsState()

    val selectedExtras = extraViewModel.selectedExtras.collectAsState()
    var selectedCartItem by remember { mutableStateOf<CartItemWithExtrasUIModel?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    LaunchedEffect(orderStatus) {
        when (orderStatus) {
            OrderStatus.SUCCESS -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.order_success)
                )
            }

            OrderStatus.ERROR -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.order_error)
                )
            }

            else -> {}
        }
    }

    LaunchedEffect(selectedCartItem) {
        selectedCartItem?.let {
            extraViewModel.setSelectedExtras(it.extras)
        }
    }

    selectedCartItem?.let { cartItem ->
        ExtrasDialog(
            isUpdate = true,
            categoriesWithExtras = categoriesWithExtras,
            selectedExtras = selectedExtras.value,
            onExtraSelectionChanged = { extra -> extraViewModel.toggleExtraSelection(extra) },
            onConfirm = {
                cartViewModel.updateCartItemExtras(
                    cartItem.copy(extras = selectedExtras.value)
                )
                extraViewModel.clearSelectedExtras()
                selectedCartItem = null
            },
            onDismiss = {
                extraViewModel.clearSelectedExtras()
                selectedCartItem = null
            }
        )
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },

        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(id = R.string.cart_title).uppercase(),
                isImageTitle = false,
            )
        },

        bottomBar = {
            Button(
                onClick = { cartViewModel.submitOrder() },
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 16.dp)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(6.dp),
                        clip = false
                    ),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = Color.DarkGray,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)

            ) {
                Text(
                    text = stringResource(id = R.string.button_order).uppercase(),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                )
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(cartItemsWithExtras.size) { index ->
                val cartItemWithExtras = cartItemsWithExtras[index]

                CartItem(
                    cartItemWithExtras = cartItemWithExtras,
                    onRemoveIceCream = { cartViewModel.removeIceCream(cartItemWithExtras.cartItem) },
                    onRemoveExtra = { extraId ->
                        val cartItem = cartItemWithExtras.cartItem
                        check(cartItem is CartItemUIModel.Existing) { "Invalid operation: extras removal requires existing item with ID." }
                        cartViewModel.removeExtra(cartItem.id, extraId)
                    },
                    onEditExtras = { selectedCartItem = cartItemWithExtras },
                    calculateItemPrice = { cartViewModel.calculateItemPrice(it) }
                )
                if (index != cartItemsWithExtras.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth(),
                        thickness = 10.dp,
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }

            if (cartItemsWithExtras.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.total_price),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "$totalCartPrice €",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

        }
    }
}
