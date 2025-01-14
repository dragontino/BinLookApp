package com.example.binchecker.ui.features.enter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.binchecker.R
import com.example.binchecker.ui.composables.Loading
import com.example.binchecker.ui.navigation.NavArguments
import com.example.binchecker.util.reversed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterScreen(
    viewModel: EnterViewModel,
    navigateTo: (args: NavArguments) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember(::SnackbarHostState)

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is EnterViewModel.EnterEvent.NavigateToDestination -> navigateTo(event.arguments)

                is EnterViewModel.EnterEvent.ShowSnackbar -> snackbarHostState.showSnackbar(
                    message = event.message,
                    withDismissAction = true,
                    duration = SnackbarDuration.Long
                )
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.activity_name),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        IconButton(onClick = viewModel::showHistory) {
                            Icon(
                                imageVector = Icons.Rounded.History,
                                contentDescription = "show history",
                                modifier = Modifier.scale(1.2f)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(bottom = 64.dp)
                ) {
                    Snackbar(
                        snackbarData = it,
                        shape = MaterialTheme.shapes.medium,
                        containerColor = MaterialTheme.colorScheme.background.reversed,
                        contentColor = MaterialTheme.colorScheme.onBackground.reversed,
                        dismissActionContentColor = MaterialTheme.colorScheme.primary
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { contentPadding ->
            Content(
                viewModel = viewModel,
                modifier = Modifier.padding(contentPadding)
            )
        }


        AnimatedVisibility(
            visible = viewModel.isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Loading(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.background.copy(alpha = .9f)
                )
            )
        }
    }
}


@Composable
private fun Content(
    viewModel: EnterViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier.padding(16.dp).fillMaxSize()) {
        OutlinedTextField(
            value = viewModel.bin,
            onValueChange = {
                if (it.length <= 6 && it.all { it.isDigit() }) viewModel.bin = it
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.enter_bin),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingIcon = viewModel.bin.takeIf { it.isNotBlank() }?.let {
                {
                    IconButton(onClick = { viewModel.bin = "" }) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "Clear input"
                        )
                    }
                }
            },
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                focusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.align(Alignment.Center).fillMaxWidth()
        )


        TextButton(
            onClick = viewModel::getBankInformation,
            enabled = viewModel.bin.length == 6,
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .heightIn(min = 48.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.get_data),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}