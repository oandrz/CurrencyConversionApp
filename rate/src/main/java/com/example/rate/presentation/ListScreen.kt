package com.example.rate.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.design.MyTheme
import com.example.rate.R
import com.example.rate.model.RateCurrencyUIContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    vm: ListViewModel = viewModel()
) {
    val uiState by vm.uiState.collectAsState()
    Scaffold(
        topBar = { Toolbar() },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it), contentAlignment = Alignment.Center
            ) {
                when (uiState) {
                    is ListViewModel.ListUIState.Success -> SuccessContent(
                        state = uiState as ListViewModel.ListUIState.Success,
                    )
                    is ListViewModel.ListUIState.Failed -> FailedContent()
                    is ListViewModel.ListUIState.Loading -> LoadingContent()
                }
            }
        }
    )

}

@Composable
fun SuccessContent(
    modifier: Modifier = Modifier,
    state: ListViewModel.ListUIState.Success,
    onActionInvoked: ((ListViewModel.ListUIAction) -> Unit)? = null,
) {
    LazyColumn {
        items(state.flightRates) {
            Text(
                text = it.city,
                modifier = Modifier.wrapContentSize()
            )
        }
    }
}

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.testTag("loading")
    )
}

@Composable
fun FailedContent(modifier: Modifier = Modifier) {
    Text(text = stringResource(R.string.failed_fetching), modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyHeader(
    modifier: Modifier = Modifier,
    currency: String,
    currencyDetail: String,
    amount: String,
    onAmountChanged: ((String) -> Unit)? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.base_currency),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.surface
                )
                Text(
                    text = currency,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.surface
                )
                Text(
                    text = currencyDetail,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.surface
                )
            }

            var text by rememberSaveable { mutableStateOf(amount) }
            OutlinedTextField(
                value = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("amount_field"),
                onValueChange = {
                    text = "test"
                    onAmountChanged?.invoke(text)
                },
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.surface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                ),
                label = {
                    Text(
                        text = stringResource(R.string.amount),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.surface
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun RateList(
    modifier: Modifier = Modifier,
    rates: List<RateCurrencyUIContent>,
    onItemClicked: ((RateCurrencyUIContent) -> Unit)? = null
) = LazyColumn(modifier = modifier) {
    itemsIndexed(items = rates) { index, data ->
        Spacer(modifier = Modifier.size(16.dp))
        RateItem(
            position = index,
            currency = data.currencySymbol,
            currencyDetail = data.currencySymbolDetail,
            rate = "s",
            onItemClicked = {
                onItemClicked?.invoke(rates[it])
            }
        )
    }
}

@Composable
fun RateItem(
    modifier: Modifier = Modifier,
    position: Int,
    currency: String,
    currencyDetail: String,
    rate: String,
    onItemClicked: ((Int) -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                onItemClicked?.invoke(position)
            }
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.7f)) {
            Text(
                text = currency,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = currencyDetail)
        }

        Text(
            text = rate,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar() = TopAppBar(
    title = { Text(stringResource(R.string.toolbar_title)) },
    colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.surface
    )
)

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    MyTheme(dynamicColor = false) {
        ListScreen()
    }
}