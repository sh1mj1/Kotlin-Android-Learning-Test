package com.example.learningtest.compose.state.interview

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningtest.ui.theme.LearningTestTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * rememberCoroutineScope를 사용한 애니메이션 수동 관리 예제
 *
 * 핵심 포인트:
 * 1. rememberCoroutineScope로 코루틴 스코프 생성
 * 2. Job을 remember로 저장하여 수동 관리
 * 3. 사용자 이벤트로 애니메이션 시작/취소 제어
 */
@Composable
fun AnimationCancellationScreen() {
    // 코루틴 스코프를 기억
    val scope = rememberCoroutineScope()

    // 애니메이션 Job을 저장 (수동 취소를 위해)
    var animationJob by remember { mutableStateOf<Job?>(null) }

    // 애니메이션 진행 상태 (0f ~ 100f)
    val progress = remember { Animatable(0f) }

    // 애니메이션 상태 표시용
    var animationStatus by remember { mutableStateOf("대기 중") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 제목
        Text(
            text = "애니메이션 수동 관리",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "rememberCoroutineScope + Job 저장",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 진행 상태 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "진행 상태",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                // 진행률 숫자
                Text(
                    text = "${progress.value.toInt()}%",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // 진행률 바
                LinearProgressIndicator(
                    progress = { progress.value / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                )

                // 상태 표시
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = when (animationStatus) {
                                "실행 중" -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                                "취소됨" -> Color(0xFFF44336).copy(alpha = 0.1f)
                                "완료" -> Color(0xFF2196F3).copy(alpha = 0.1f)
                                else -> Color(0xFF9E9E9E).copy(alpha = 0.1f)
                            },
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "상태: $animationStatus",
                        fontWeight = FontWeight.Medium,
                        color = when (animationStatus) {
                            "실행 중" -> Color(0xFF4CAF50)
                            "취소됨" -> Color(0xFFF44336)
                            "완료" -> Color(0xFF2196F3)
                            else -> Color(0xFF9E9E9E)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 애니메이션 시작 버튼
        Button(
            onClick = {
                // 이전 애니메이션이 실행 중이면 취소
                animationJob?.cancel()

                // 새로운 애니메이션 시작
                animationJob = scope.launch {
                    animationStatus = "실행 중"
                    try {
                        progress.animateTo(
                            targetValue = 100f,
                            animationSpec = tween(durationMillis = 5000)
                        )
                        animationStatus = "완료"
                    } catch (e: Exception) {
                        // 취소되면 여기로 옴
                        animationStatus = "취소됨"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = animationStatus != "실행 중"
        ) {
            Text(
                text = "애니메이션 시작 (5초)",
                modifier = Modifier.padding(8.dp)
            )
        }

        // 애니메이션 취소 버튼
        Button(
            onClick = {
                // Job을 수동으로 취소
                animationJob?.cancel()
                animationStatus = "취소됨"
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = animationStatus == "실행 중"
        ) {
            Text(
                text = "애니메이션 취소",
                modifier = Modifier.padding(8.dp)
            )
        }

        // 리셋 버튼
        Button(
            onClick = {
                animationJob?.cancel()
                scope.launch {
                    progress.snapTo(0f)
                }
                animationStatus = "대기 중"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "리셋",
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "💡 핵심 포인트",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "• rememberCoroutineScope로 스코프 생성",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "• Job을 remember로 저장하여 수동 관리",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "• animationJob?.cancel()로 직접 취소",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "• LaunchedEffect와 달리 사용자 이벤트로 제어",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimationCancellationScreenPreview() {
    LearningTestTheme {
        AnimationCancellationScreen()
    }
}
