package com.slembers.alarmony.feature.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun CardBox(
    title : @Composable() () -> Unit = { CardTitle(title = "제목") },
    content: @Composable() () -> Unit = {},
    modifier: Modifier = DefaultModifier()
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        content = {
            Column(
                content = {
                    title()
                    content()
                }
            )
        }
    )
}

@Composable
fun DefaultModifier() : Modifier {
    return Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .border(
            BorderStroke(0.dp, MaterialTheme.colorScheme.background),
            MaterialTheme.shapes.medium
        )
        .padding(4.dp)
        .border(1.dp, Color.Black, MaterialTheme.shapes.medium)
        .shadow(
            elevation = 1.dp,
            MaterialTheme.shapes.medium,
            ambientColor = Color.Gray
        )
}