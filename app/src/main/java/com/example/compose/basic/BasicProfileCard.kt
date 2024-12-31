package com.example.compose.basic

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BasicProfileCard(
    name: String,
    age: Int,
) {
    Column {
        Text(text = "Name: $name")
        Text(text = "Age: $age")
    }
}

@Preview(showBackground = true)
@Composable
private fun BasicProfileCardPreview() {
    BasicProfileCard(
        name = "sh1mj1",
        age = 27,
    )
}
