package com.example.learningtest.fourComponents.service.foregroundboud

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.learningtest.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    currentTrack: Track,
    maxDuration: Float,
    currentDuration: Float,
    isPlaying: Boolean,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    onPrev: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
) {
    MaterialTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Music Player")
                    },
                    actions = {
                        IconButton(onClick = onStartService) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                            )
                        }
                        IconButton(onClick = onStopService) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                            )
                        }
                    },
                )
            },
            content = {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(it),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter =
                            painterResource(
                                id = currentTrack.image,
                            ),
                        contentDescription = null,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = currentTrack.name,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier =
                            Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = currentDuration.div(1000).toInt().toString(),
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Slider(
                            modifier = Modifier.weight(1f),
                            value = currentDuration,
                            onValueChange = {},
                            valueRange = 0f..maxDuration,
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = maxDuration.div(1000).toInt().toString(),
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = onPrev) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_prev),
                                contentDescription = null,
                            )
                        }

                        IconButton(onClick = onPlayPause) {
                            Icon(
                                painter =
                                    if (isPlaying) {
                                        painterResource(id = R.drawable.ic_pause)
                                    } else {
                                        painterResource(
                                            id = R.drawable.ic_play,
                                        )
                                    },
                                contentDescription = null,
                            )
                        }

                        IconButton(onClick = onNext) {
                            Icon(
                                painter =
                                    painterResource(
                                        id = R.drawable.ic_next,
                                    ),
                                contentDescription = null,
                            )
                        }
                    }
                }
            },
        )
    }
}
