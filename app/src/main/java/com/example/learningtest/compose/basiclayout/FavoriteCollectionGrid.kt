package com.example.learningtest.compose.basiclayout

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningtest.R

@Composable
fun FavoriteCollectionGrid(modifier: Modifier = Modifier) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.height(168.dp),
    ) {
        items(favoriteCollectionsData()) { item ->
            FavoriteCollectionCard(
                drawable = item.drawableRes,
                text = item.text,
                modifier = modifier.height(80.dp),
            )
        }
    }
}

data class FavoriteCollectionCardData(
    @StringRes val text: Int,
    @DrawableRes val drawableRes: Int,
)

fun favoriteCollectionsData(): List<FavoriteCollectionCardData> =
    List(100) {
        FavoriteCollectionCardData(
            text = R.string.align_your_body_data_sample,
            drawableRes = R.drawable.ic_launcher_background,
        )
    }

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
private fun FavoriteCollectionCardPreview() {
    FavoriteCollectionGrid()
}
