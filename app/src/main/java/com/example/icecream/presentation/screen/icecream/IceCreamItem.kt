package com.example.icecream.presentation.screen.icecream

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.icecream.R
import com.example.icecream.common.model.Status
import com.example.icecream.presentation.component.shimmerEffect
import com.example.icecream.presentation.model.IceCreamUIModel
import kotlin.math.roundToInt

@Composable
fun IceCreamItem(
    iceCream: IceCreamUIModel,
    onAddToCartClicked: () -> Unit
) {
    val displayName = iceCream.nameResId?.let { stringResource(id = it) } ?: iceCream.name

    val painter = rememberAsyncImagePainter(
        model = iceCream.imageUrl ?: R.drawable.placeholder
    )
    val imageState = painter.state
    val isLoading = imageState is AsyncImagePainter.State.Loading

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .shimmerEffect()
                )
            }
            Image(
                painter = painter,
                contentDescription = iceCream.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = displayName,
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = when (iceCream.status) {
                        Status.AVAILABLE -> stringResource(
                            id = R.string.ice_cream_status_available,
                            iceCream.price.roundToInt()
                        )

                        Status.UNAVAILABLE -> stringResource(id = R.string.ice_cream_status_unavailable)
                        Status.MELTED -> stringResource(id = R.string.ice_cream_status_melted)
                    },
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Button(
                onClick = onAddToCartClicked,
                enabled = iceCream.status == Status.AVAILABLE,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(6.dp),
                        clip = false
                    ),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = Color.DarkGray,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    stringResource(id = R.string.button_add_to_cart).uppercase(),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),

                    )
            }
        }


    }
}
