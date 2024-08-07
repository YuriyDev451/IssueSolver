package com.issuesolver.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorText(errorMessage: String?) {
    errorMessage?.let {
        Text(
            text = it,
            fontSize = 13.sp,
            color = Color.Red,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(all = 8.dp)
        )
    }
}
