package com.example.learningtest.compose.state.interview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningtest.ui.theme.LearningTestTheme
import kotlinx.coroutines.delay

/**
 * rememberUpdatedState 예제 화면
 *
 * 문제 상황과 해결 방법을 비교하여 보여줌
 * - 문제: LaunchedEffect 내에서 오래된(stale) 값 참조
 * - 해결: rememberUpdatedState로 항상 최신 값 참조
 */
@Composable
fun RememberUpdatedStateScreen() {
    var selectedMessage by remember { mutableStateOf("메시지 A") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 제목
        Text(
            text = "rememberUpdatedState",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "항상 최신(Updated) 값을 기억하기",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 메시지 선택 버튼
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "메시지 선택",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("메시지 A", "메시지 B", "메시지 C").forEach { message ->
                        Button(
                            onClick = { selectedMessage = message },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedMessage == message)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(
                                text = message.takeLast(1),
                                color = if (selectedMessage == message)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Text(
                    text = "현재 선택: $selectedMessage",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        HorizontalDivider()

        // 문제 상황: LaunchedEffect만 사용
        TimerSection(
            title = "❌ 문제 상황",
            subtitle = "LaunchedEffect만 사용",
            message = selectedMessage,
            useRememberUpdatedState = false,
            backgroundColor = Color(0xFFFFEBEE)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 해결: rememberUpdatedState 사용
        TimerSection(
            title = "✅ 해결",
            subtitle = "rememberUpdatedState 사용",
            message = selectedMessage,
            useRememberUpdatedState = true,
            backgroundColor = Color(0xFFE8F5E9)
        )

        Spacer(modifier = Modifier.height(8.dp))

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
                    text = "💡 사용 방법",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "1. 메시지를 선택하고 타이머 시작",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "2. 타이머가 실행되는 동안 다른 메시지로 변경",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "3. 3초 후 결과 로그 확인",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "📌 차이점",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "• 문제: 처음 선택한 메시지가 출력됨",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "• 해결: 최신 선택한 메시지가 출력됨",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

/**
 * 타이머 섹션 - 문제 상황과 해결 방법을 각각 보여줌
 */
@Composable
fun TimerSection(
    title: String,
    subtitle: String,
    message: String,
    useRememberUpdatedState: Boolean,
    backgroundColor: Color
) {
    var isRunning by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(3) }
    var resultLog by remember { mutableStateOf<String?>(null) }
    var capturedMessage by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 헤더
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // 타이머 표시
            if (isRunning) {
                Text(
                    text = "⏱️ ${countdown}초 후 실행...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

                if (capturedMessage != null) {
                    Text(
                        text = "시작 시 메시지: \"$capturedMessage\"",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // 결과 로그
            if (resultLog != null) {
                Text(
                    text = "📝 결과: $resultLog",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1976D2)
                )
            }

            // 시작/리셋 버튼
            Button(
                onClick = {
                    if (isRunning) {
                        // 리셋
                        isRunning = false
                        countdown = 3
                        resultLog = null
                        capturedMessage = null
                    } else {
                        // 시작
                        isRunning = true
                        countdown = 3
                        resultLog = null
                        capturedMessage = message
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isRunning) "리셋" else "타이머 시작 (3초)",
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }

    // Effect 실행
    if (isRunning) {
        if (useRememberUpdatedState) {
            // ✅ 해결: rememberUpdatedState 사용
            TimerEffectWithRememberUpdatedState(
                message = message,
                onCountdown = { countdown = it },
                onTimeout = {
                    resultLog = "\"$it\" 출력됨"
                    isRunning = false
                }
            )
        } else {
            // ❌ 문제: LaunchedEffect만 사용
            TimerEffectWithoutRememberUpdatedState(
                message = message,
                onCountdown = { countdown = it },
                onTimeout = {
                    resultLog = "\"$it\" 출력됨"
                    isRunning = false
                }
            )
        }
    }
}

/**
 * ❌ 문제 상황: LaunchedEffect만 사용
 * 처음 캡처된 message 값만 사용됨
 */
@Composable
fun TimerEffectWithoutRememberUpdatedState(
    message: String,
    onCountdown: (Int) -> Unit,
    onTimeout: (String) -> Unit
) {
    LaunchedEffect(Unit) { // key가 Unit이므로 한 번만 실행
        for (i in 3 downTo 1) {
            onCountdown(i)
            delay(1000)
        }
        // ❌ 처음 캡처된 message만 호출됨!
        onTimeout(message)
    }
}

/**
 * ✅ 해결: rememberUpdatedState 사용
 * 항상 최신 message 값 사용
 */
@Composable
fun TimerEffectWithRememberUpdatedState(
    message: String,
    onCountdown: (Int) -> Unit,
    onTimeout: (String) -> Unit
) {
    // 항상 최신 message를 기억
    val currentMessage by rememberUpdatedState(message)

    LaunchedEffect(Unit) {
        for (i in 3 downTo 1) {
            onCountdown(i)
            delay(1000)
        }
        // ✅ 최신 currentMessage 호출!
        onTimeout(currentMessage)
    }
}

@Preview(showBackground = true)
@Composable
fun RememberUpdatedStateScreenPreview() {
    LearningTestTheme {
        RememberUpdatedStateScreen()
    }
}
