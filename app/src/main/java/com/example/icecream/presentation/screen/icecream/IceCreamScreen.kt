package com.example.icecream.presentation.screen.icecream

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.icecream.presentation.component.IceCreamShimmerItem
import com.example.icecream.presentation.component.TopBar
import com.example.icecream.presentation.model.IceCreamUIModel
import com.example.icecream.presentation.screen.ExtrasDialog
import com.example.icecream.presentation.viewmodel.CartViewModel
import com.example.icecream.presentation.viewmodel.ExtraViewModel
import com.example.icecream.presentation.viewmodel.IceCreamViewModel

@Composable
fun IceCreamScreen(
    navController: NavController,
    viewModel: IceCreamViewModel = hiltViewModel(),
    extraViewModel: ExtraViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()

) {
    val iceCreams by viewModel.sortedIceCreams.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val categoriesWithExtras by extraViewModel.categoriesWithExtras.collectAsState()
    val selectedExtras by extraViewModel.selectedExtras.collectAsState()
    val cartItemsWithExtras by cartViewModel.cartItems.collectAsState()
    val cartItemCount = cartItemsWithExtras.size


    val listState = rememberLazyListState()
    var sortTrigger by remember { mutableIntStateOf(0) }

    var showExtrasDialog by remember { mutableStateOf(false) }
    var selectedIceCream by remember { mutableStateOf<IceCreamUIModel?>(null) }


    LaunchedEffect(Unit) {
        cartViewModel.loadCartItems()
    }
    LaunchedEffect(sortTrigger) {
        listState.animateScrollToItem(0)
    }

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = null,
                isImageTitle = true,
                showBackButton = false,
                cartItemCount = cartItemCount,
                onSortClicked = { status ->
                    viewModel.sortIceCreamsByStatus(status)
                    sortTrigger++
                },
                onCartClicked = { navController.navigate("cartScreen") }
            )
        }
    ) { padding ->
        LazyColumn(    state = listState,
            contentPadding = padding) {
            if (isLoading) {
                items(6) { IceCreamShimmerItem() }
            } else {
                items(iceCreams.size) { index ->
                    IceCreamItem(iceCream = iceCreams[index]) {
                        selectedIceCream = iceCreams[index]
                        extraViewModel.clearSelectedExtras()
                        showExtrasDialog = true
                    }
                }
            }
        }
    }

    if (showExtrasDialog) {
        ExtrasDialog(
            categoriesWithExtras = categoriesWithExtras,
            selectedExtras = selectedExtras,
            onExtraSelectionChanged = { extraViewModel.toggleExtraSelection(it) },
            onConfirm = {
                selectedIceCream?.let {
                    cartViewModel.addToCart(it, selectedExtras)
                }
                showExtrasDialog = false
            },
            onDismiss = {
                showExtrasDialog = false
            }
        )
    }
}
