package com.example.binchecker.ui.features.bininfo

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.binchecker.R
import com.example.binchecker.ui.composables.Loading
import com.example.binchecker.util.reversed
import com.example.domain.model.Bank
import com.example.domain.model.BankCardInfo
import com.example.domain.model.Country
import com.example.domain.model.Number
import java.util.Currency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankCardInfoScreen(
    viewModel: BankCardInfoViewModel,
    navigateBack: () -> Unit,
    navigateToMap: (Pair<Float, Float>) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember(::SnackbarHostState)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.messageFlow.collect { message ->
            snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = viewModel.bankCardInfo
                            ?.let { stringResource(R.string.bin_information, it.bin) }
                            ?: stringResource(R.string.bank_card_info_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "return to previous screen"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.background.reversed,
                    contentColor = MaterialTheme.colorScheme.onBackground.reversed,
                    dismissActionContentColor = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { contentPadding ->
        Crossfade(
            targetState = viewModel.isLoading,
            label = "loading cardInfo content",
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) { isLoading ->
            when {
                isLoading -> Loading()
                else -> viewModel.bankCardInfo?.let {
                    BankCardInfoContent(
                        bankCardInfo = it,
                        openLink = {
                            val intent = Intent(Intent.ACTION_VIEW).setData(it.url.toUri())
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun BankCardInfoContent(
    bankCardInfo: BankCardInfo,
    openLink: (Links) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
            bankCardInfo.country?.let {
                Country(country = it)
            }

            bankCardInfo.bank?.let {
                Bank(bank = it, openLink = openLink)
            }

            bankCardInfo.number?.let {
                Number(number = it, openLink = openLink)
            }

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    bankCardInfo.scheme?.let {
                        item {
                            InfoColumn(
                                title = stringResource(R.string.card_scheme),
                                content = it.replaceFirstChar { it.uppercaseChar() }
                            )
                        }
                    }

                    bankCardInfo.brand?.let {
                        item {
                            InfoColumn(
                                title = stringResource(R.string.card_brand),
                                content = it.replaceFirstChar { it.uppercaseChar() }
                            )
                        }
                    }

                    bankCardInfo.type?.let {
                        item {
                            InfoColumn(
                                title = stringResource(R.string.card_type),
                                content = it.replaceFirstChar { it.uppercaseChar() }
                            )
                        }
                    }

                    bankCardInfo.prepaid?.let {
                        item {
                            InfoColumn(
                                title = stringResource(R.string.card_prepaid),
                                content = if (it) stringResource(R.string.yes) else stringResource(R.string.no)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun Bank(
    bank: Bank,
    openLink: (Links) -> Unit,
    modifier: Modifier = Modifier
) {
    fun canExpand(): Boolean {
        return bank.url != null || bank.city != null || bank.phone != null
    }

    val expandedState = rememberSaveable { mutableStateOf(false) }
    val arrowRotationAnimation = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(expandedState.value) {
        val targetDegrees = when {
            expandedState.value -> 90f
            else -> 0f
        }

        if (canExpand()) {
            arrowRotationAnimation.animateTo(
                targetValue = targetDegrees,
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
            )
        }
    }

    bank.name?.let { bankName ->
        OutlinedCard(
            shape = MaterialTheme.shapes.medium,
            modifier = modifier,
            border = CardDefaults.outlinedCardBorder(enabled = expandedState.value),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            ListItem(
                overlineContent = {
                    Text(
                        text = stringResource(R.string.bank_title),
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                leadingContent = when {
                    canExpand() -> {
                        {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                                contentDescription = null,
                                modifier = Modifier
                                    .scale(.7f)
                                    .graphicsLayer {
                                        rotationZ = arrowRotationAnimation.value
                                    }
                            )
                        }
                    }
                    else -> null
                },
                headlineContent = {
                    Text(
                        text = bankName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier.clickable(enabled = canExpand()) {
                    expandedState.value = !expandedState.value
                },
            )

            AnimatedVisibility(
                visible = expandedState.value,
                enter = expandVertically(
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                ),
                exit = shrinkVertically(
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    bank.city?.let {
                        InfoRow(
                            title = stringResource(R.string.city),
                            content = it
                        )
                    }
                    bank.phone?.let {
                        InfoRow(
                            title = stringResource(R.string.phone),
                            content = it,
                            trailingIcon = {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable { openLink(Links.Phone(it)) }
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Call,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.scale(.85f)
                                    )
                                }
                            }
                        )
                    }
                    bank.url?.let {
                        InfoRow(
                            title = stringResource(R.string.website),
                            content = it,
                            trailingIcon = {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable { openLink(Links.Website(it)) }
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Language,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.scale(.85f)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun Number(
    number: Number,
    openLink: (Links) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            number.length?.let { length ->
                Text(
                    text = buildString {
                        append(stringResource(R.string.card_number_contains))
                        append(" ")
                        append(pluralStringResource(R.plurals.digits, length, length))
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            number.luhn?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = when {
                            it -> stringResource(R.string.luhn_used)
                            else -> stringResource(R.string.luhn_not_used)
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )

                    IconButton(
                        onClick = { openLink(Links.LuhnWiki) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.HelpOutline,
                            contentDescription = null,
                            modifier = Modifier.scale(.8f)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun Country(
    country: Country,
    modifier: Modifier = Modifier
) {
    fun canExpand(): Boolean {
        return country.currency != null || (country.latitude != null && country.longitude != null)
    }

    val expandedState = rememberSaveable { mutableStateOf(false) }
    val arrowRotationAnimation = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(expandedState.value) {
        val targetDegrees = when {
            expandedState.value -> 90f
            else -> 0f
        }

        if (canExpand()) {
            arrowRotationAnimation.animateTo(
                targetValue = targetDegrees,
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
            )
        }
    }

    country.name?.let { countryName ->
        OutlinedCard(
            shape = MaterialTheme.shapes.medium,
            modifier = modifier,
            border = CardDefaults.outlinedCardBorder(enabled = expandedState.value),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier
                            .scale(.7f)
                            .graphicsLayer {
                                rotationZ = arrowRotationAnimation.value
                            }
                    )
                },
                overlineContent = {
                    Text(
                        text = stringResource(R.string.country_title),
                        style = MaterialTheme.typography.titleSmall
                    )
                },

                trailingContent = when (country.emoji) {
                    null -> null
                    else -> {
                        { country.emoji?.let { Text(it) } }
                    }
                },
                headlineContent = {
                    Text(
                        text = countryName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier.clickable(enabled = canExpand()) {
                    expandedState.value = !expandedState.value
                },
            )

            AnimatedVisibility(
                visible = expandedState.value,
                enter = expandVertically(
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                ),
                exit = shrinkVertically(
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (country.latitude != null && country.longitude != null) {
                        InfoRow(
                            title = stringResource(R.string.country_location),
                            content = buildString {
                                append(stringResource(R.string.latitude), ": ")
                                country.latitude
                                    ?.let { if (it % 1 == 0f) it.toInt() else it }
                                    .let { append(it, ", ") }

                                append(stringResource(R.string.longitude).lowercase(), ": ")
                                country.longitude
                                    ?.let { if (it % 1 == 0f) it.toInt() else it }
                                    .let { append(it) }
                            }
                        )
                    }

                    country.currency?.let {
                        val currency = Currency.getInstance(it)
                        InfoRow(
                            title = stringResource(R.string.currency),
                            content = buildString {
                                append(currency.displayName.replaceFirstChar { it.uppercase() })
                                append(" (${currency.symbol})")
                            }
                        )
                    }
                }
            }
        }
    }
}



@Composable
private fun InfoRow(
    title: CharSequence,
    content: CharSequence,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (RowScope.() -> Unit)? = null,
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
            style = MaterialTheme.typography.labelSmall
        )

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            com.example.binchecker.util.Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium
            )

            trailingIcon?.invoke(this)
        }
    }
}


@Composable
private fun InfoColumn(
    title: CharSequence,
    content: CharSequence,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        com.example.binchecker.util.Text(
            text = title,
            style = MaterialTheme.typography.labelSmall
        )

        com.example.binchecker.util.Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}