package com.example.learningtest.compose.basic.introduction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BasicReusingComposable() {
    Column {
        BasicButton(text = "sh1mj1") { }
        Spacer(modifier = Modifier.height(16.dp))
        BasicButton(text = "sh1mj1") { }
    }
}

@Preview(showBackground = true)
@Composable
private fun BasicReusingComposablePreview() {
    BasicReusingComposable()
}
