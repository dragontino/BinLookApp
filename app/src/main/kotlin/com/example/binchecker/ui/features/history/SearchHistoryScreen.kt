package com.example.binchecker.ui.features.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.binchecker.R
import com.example.binchecker.ui.navigation.CardInfoArguments
import com.example.binchecker.util.reversed
import com.example.domain.model.BankCardInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchHistoryScreen(
    viewModel: BinHistoryViewModel,
    navigateToCardInfoDetails: (CardInfoArguments) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember(::SnackbarHostState)

    LaunchedEffect(Unit) {
        viewModel.messageFlow.collect { message ->
            snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
    }

    val searchHistoryListState = viewModel.searchHistoryFlow.collectAsState(listOf())
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "return to previous screen"
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.history_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    shape = MaterialTheme.shapes.medium,
                    containerColor = MaterialTheme.colorScheme.background.reversed,
                    contentColor = MaterialTheme.colorScheme.onBackground.reversed,
                    dismissActionContentColor = MaterialTheme.colorScheme.primary
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { contentPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
        ) {
            items(searchHistoryListState.value) {
                BankCardInfo(
                    bankCardInfo = it,
                    onClick = {
                        navigateToCardInfoDetails(CardInfoArguments(cardInfoId = it.id))
                    }
                )
            }
        }
    }
}


@Composable
private fun BankCardInfo(
    bankCardInfo: BankCardInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            width = 1.5.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.onBackground
                )
            )
        ),
        onClick = onClick,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            InfoRow(
                title = stringResource(R.string.bin_number),
                content = bankCardInfo.bin
            )

            bankCardInfo.country?.let { country ->
                if (country.name != null || country.emoji != null) {
                    InfoRow(
                        title = stringResource(R.string.country),
                        content = buildString {
                            country.name?.let { append(it, " ") }
                            country.emoji?.let { append(it) }
                        }
                    )
                }
            }

            bankCardInfo.bank?.name?.let {
                InfoRow(
                    title = stringResource(R.string.bank),
                    content = it
                )
            }
        }
    }
}


@Composable
private fun InfoRow(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        com.example.binchecker.util.Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )

        com.example.binchecker.util.Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}