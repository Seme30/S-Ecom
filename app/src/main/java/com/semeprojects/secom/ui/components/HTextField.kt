package com.semeprojects.secom.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HTextField(
    modifier: Modifier = Modifier,
    inputState: MutableState<String> = mutableStateOf(""),
    leadingIcon: (@Composable() () -> Unit)? = null,
    trailingIcon: (@Composable() () -> Unit)? = null,
    label: String = "",
    placeholder: (@Composable() () -> Unit)? = null,
    onError: Boolean = false,
    supportingText: (@Composable() () -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions =KeyboardOptions(keyboardType = KeyboardType.Text),
    readOnly: Boolean = false,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {


        TextField(
            value = inputState.value,
            onValueChange = {
                inputState.value = it
            },
            modifier = modifier.fillMaxWidth().padding(10.dp).clip(
                MaterialTheme.shapes.medium
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary
            ),
            keyboardActions = keyboardActions,
            readOnly = readOnly,
            interactionSource = interactionSource,
            isError = onError,
            supportingText = supportingText,
            textStyle = MaterialTheme.typography.bodyMedium,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            placeholder = placeholder
        )
}