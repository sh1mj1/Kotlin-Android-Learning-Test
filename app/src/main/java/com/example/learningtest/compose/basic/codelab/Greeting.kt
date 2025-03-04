package com.example.learningtest.compose.basic.codelab

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.example.learningtest.R

@Composable
fun Greeting(
    name: String,
    expandedInitially: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        modifier =
            modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .wrapContentHeight(),
    ) {
        CardContent(name = name, expandedInitially = expandedInitially)
    }
}

@Composable
private fun CardContent(
    name: String,
    expandedInitially: Boolean = false,
) {
    var expanded by rememberSaveable { mutableStateOf(expandedInitially) }

    Row(
        modifier =
            Modifier
                .padding(12.dp)
                .animateContentSize(
                    animationSpec =
                        spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow,
                        ),
                ),
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(12.dp),
        ) {
            Text(text = "Hello")
            Text(
                text = name,
                style =
                    MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                    ),
            )
            if (expanded) {
                Text(
                    text = LoremIpsum(20).values.joinToString(),
                )
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription =
                    if (expanded) {
                        stringResource(R.string.show_less)
                    } else {
                        stringResource(R.string.show_more)
                    },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardContentPreview() {
    CardContent(name = "Android")
}

@Preview(
    showBackground = true,
    name = "GreetingDarkMode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun GreetingPreviews(
    @PreviewParameter(GreetingPreviewParameters::class) expandedInitially: Boolean,
) {
    Box {
        Greeting(name = "Android", expandedInitially = expandedInitially)
    }
}

class GreetingPreviewParameters : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(false, true)
}

@Preview
@Composable
private fun GreetingPreviewsMaxHeight() {
    Box(modifier = Modifier.fillMaxHeight()) {
        Greeting(name = "Android")
    }
}
