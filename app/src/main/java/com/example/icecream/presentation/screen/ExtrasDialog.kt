package com.example.icecream.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.icecream.R
import com.example.icecream.presentation.model.ExtraCategoryWithExtrasUIModel
import com.example.icecream.presentation.model.ExtraUIModel
import kotlin.math.roundToInt


@Composable
fun ExtrasDialog(
    categoriesWithExtras: List<ExtraCategoryWithExtrasUIModel>,
    selectedExtras: List<ExtraUIModel>,
    onExtraSelectionChanged: (ExtraUIModel) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isUpdate: Boolean = false
) {
    val hasConeSelected = selectedExtras.any {
        it.category.required == true
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(6.dp),
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                },
                enabled = hasConeSelected,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = Color.DarkGray,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = if (isUpdate)
                        stringResource(id = R.string.button_update).uppercase()
                    else
                        stringResource(id = R.string.button_add_to_cart)
                            .uppercase()
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Text(stringResource(id = R.string.cancel).uppercase())
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.add_extras),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {
                categoriesWithExtras.forEach { categoryWithExtras ->
                    val categoryDisplayName =
                        categoryWithExtras.category.nameResId?.let { stringResource(id = it) }
                            ?: categoryWithExtras.category.type

                    val isRequiredCategory =
                        categoryWithExtras.category.nameResId == R.string.cone_category


                    Text(
                        text = if (isRequiredCategory) "$categoryDisplayName (${stringResource(R.string.required)})" else categoryDisplayName,
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    categoryWithExtras.extras.forEach { extra ->
                        val isSelected = selectedExtras.contains(extra)
                        val extraDisplayName =
                            extra.nameResId?.let { stringResource(id = it) } ?: extra.name

                        Row(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                                .toggleable(
                                    value = isSelected,
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onValueChange = { onExtraSelectionChanged(extra) }
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween,

                            ) {

                            Row(modifier = Modifier.weight(1f)) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = null,
                                    enabled = true,
                                    colors = CheckboxColors(
                                        uncheckedBoxColor = MaterialTheme.colorScheme.secondary,
                                        checkedBoxColor = MaterialTheme.colorScheme.secondary,
                                        checkedCheckmarkColor = Color.Black,
                                        uncheckedCheckmarkColor = Color.Black,
                                        disabledCheckedBoxColor = Color.LightGray,
                                        disabledUncheckedBoxColor = Color.LightGray,
                                        disabledIndeterminateBoxColor = Color.LightGray,
                                        checkedBorderColor = Color.DarkGray,
                                        uncheckedBorderColor = Color.DarkGray,
                                        disabledBorderColor = Color.LightGray,
                                        disabledUncheckedBorderColor = Color.LightGray,
                                        disabledIndeterminateBorderColor = Color.LightGray
                                    )
                                )
                                Text(
                                    text = extraDisplayName,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                            Text(
                                text = " ${extra.price.roundToInt()} €",
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.primary
    )
}

