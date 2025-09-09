package com.example.icecream.presentation.screen.icecream

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.icecream.R
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
    val isError by viewModel.isError.collectAsState()

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

        when {
            isLoading -> {
                LazyColumn(contentPadding = padding) {
                    items(6) { IceCreamShimmerItem() }
                }
            }

            isError -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        text = stringResource(id = R.string.error_fetch_data)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    IconButton(
                        onClick = { viewModel.initIceCreams() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(id = R.string.retry),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                }
            }

            else -> {
                LazyColumn(
                    state = listState,
                    contentPadding = padding
                ) {
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
