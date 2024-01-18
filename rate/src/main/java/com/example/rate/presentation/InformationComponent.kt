package com.example.rate.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/*
*
* */
@Composable
fun InformationBanner(
    modifier: Modifier = Modifier,
    description: String,
    buttonClickListener: (() -> Unit)? = null,
    buttonText: String? = null,
    imageResources: Int? = null
) {
    Box(
        modifier = modifier.background(color = Color.Black)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Content(
                description = description,
                buttonText = buttonText,
                buttonClickListener = buttonClickListener
            )
            imageResources?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = "banner_information",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    description: String,
    buttonText: String? = null,
    buttonClickListener: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = description,
            color = Color.White
        )
        buttonText?.let {
            Text(
                text = buttonText,
                modifier = Modifier.clickable { buttonClickListener?.invoke() },
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}