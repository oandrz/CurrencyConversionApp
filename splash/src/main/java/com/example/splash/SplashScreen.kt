package com.example.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.dre.project.design.R as design

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = design.drawable.ic_launcher_background),
            contentDescription = "background",
        )
        Image(
            painter = painterResource(id = design.drawable.ic_launcher_foreground),
            contentDescription = "foreground",
        )
    }
}