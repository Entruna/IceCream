package com.example.icecream.presentation.screen.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.icecream.R
import com.example.icecream.presentation.model.CartItemUIModel
import com.example.icecream.presentation.model.CartItemWithExtrasUIModel
import kotlin.math.roundToInt

@Composable
fun CartItem(
    cartItemWithExtras: CartItemWithExtrasUIModel,
    onRemoveIceCream: (CartItemUIModel) -> Unit,
    onRemoveExtra: (Long) -> Unit,
    onEditExtras: (CartItemWithExtrasUIModel) -> Unit,
    calculateItemPrice: (CartItemWithExtrasUIModel) -> Double
) {
    val displayName =
        cartItemWithExtras.cartItem.iceCream.nameResId?.let { stringResource(id = it) }
            ?: cartItemWithExtras.cartItem.iceCream.name

    val totalPrice = calculateItemPrice(cartItemWithExtras)

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                ),
            )
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(

                    onClick = { onEditExtras(cartItemWithExtras) }
                ) {
                    Icon(
                        Icons.Default.Edit, contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

                IconButton(onClick = { onRemoveIceCream(cartItemWithExtras.cartItem) }) {
                    Icon(
                        Icons.Default.Close, contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

        }

        cartItemWithExtras.extras.forEach { extra ->

            val extraDisplayName =
                extra.nameResId?.let { stringResource(id = it) } ?: extra.name
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = extraDisplayName, style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                if (extra.category.nameResId != R.string.cone_category) {
                    IconButton(onClick = { onRemoveExtra(extra.id) }) {
                        Icon(
                            Icons.Default.Close, contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${totalPrice.roundToInt()} €",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

        }

    }
}