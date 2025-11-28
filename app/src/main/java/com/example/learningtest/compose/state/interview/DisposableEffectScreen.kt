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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningtest.ui.theme.LearningTestTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate

/**
 * DisposableEffect 예제 화면
 *
 * DisposableEffect의 핵심 동작 확인:
 * 1. 리소스 등록 및 해제 (타이머)
 * 2. key 변경 시 onDispose → 재실행 순서
 * 3. 컴포저블 제거 시 cleanup 호출
 */
@Composable
fun DisposableEffectScreen() {
    var selectedUserId by remember { mutableStateOf(1) }
    var showTimerComponent by remember { mutableStateOf(false) }
    val eventLog = remember { mutableStateListOf<String>() }

    // 로그 추가 헬퍼
    fun addLog(message: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        eventLog.add(0, "[$timestamp] $message")
        if (eventLog.size > 20) {
            eventLog.removeAt(eventLog.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 제목
        Text(
            text = "DisposableEffect",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "리소스 등록/해제 관리",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 1. Key 변경 예제
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "1️⃣ Key 변경 시 onDispose → 재실행",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = "사용자 ID를 변경하면 이전 타이머를 정리하고 새로 시작합니다. \n타이머 컴포저블의 DisposableEffect 의 키가 변경됨: onDispose -> 재실행.",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                // 사용자 선택
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(1, 2, 3).forEach { userId ->
                        Button(
                            onClick = {
                                selectedUserId = userId
                                addLog("🔄 사용자 ID를 $userId 로 변경")
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedUserId == userId)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(
                                text = "사용자 $userId",
                                color = if (selectedUserId == userId)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // DisposableEffect with key
                UserTimerWithDisposableEffect(
                    userId = selectedUserId,
                    onLog = { addLog(it) }
                )
            }
        }

        HorizontalDivider()

        // 2. 컴포저블 제거 시 cleanup
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "2️⃣ 컴포저블 제거 시 cleanup 호출",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = "타이머를 표시/숨김하면서 리소스 정리를 확인합니다. \n타이머를 숨기면 타이머 컴포저블이 제거되어 onDispose 자동 호출됨",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                Button(
                    onClick = {
                        showTimerComponent = !showTimerComponent
                        addLog(
                            if (showTimerComponent)
                                "👁️ 타이머 컴포넌트 표시"
                            else
                                "🙈 타이머 컴포넌트 숨김"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (showTimerComponent) "타이머 숨기기" else "타이머 보이기",
                        modifier = Modifier.padding(8.dp)
                    )
                }

                if (showTimerComponent) {
                    GlobalTimerComponent(
                        onLog = { addLog(it) }
                    )
                }
            }
        }

        // 로그 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF263238)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📋 이벤트 로그",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Button(
                        onClick = {
                            eventLog.clear()
                            addLog("🗑️ 로그 초기화됨")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF37474F)
                        )
                    ) {
                        Text("초기화", fontSize = 12.sp)
                    }
                }

                if (eventLog.isEmpty()) {
                    Text(
                        text = "아직 이벤트가 없습니다.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(
                                color = Color(0xFF1E1E1E),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        eventLog.forEach { log ->
                            Text(
                                text = log,
                                color = when {
                                    log.contains("🟢 Setup") -> Color(0xFF4CAF50)
                                    log.contains("🔴 Dispose") -> Color(0xFFF44336)
                                    log.contains("⏱️") -> Color(0xFF2196F3)
                                    log.contains("🔄") -> Color(0xFFFF9800)
                                    else -> Color(0xFFB0BEC5)
                                },
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

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
                    text = "💡 DisposableEffect 동작 원리",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "• Setup: 리소스 등록 (타이머 시작)",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "• onDispose: 리소스 해제 (타이머 정리)",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "• key 변경 시: onDispose → Setup 순서",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "• 컴포저블 제거 시: onDispose 호출",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

/**
 * DisposableEffect 예제 1: Key 변경 시 동작
 * userId가 변경되면 이전 타이머를 정리하고 새로 시작
 */
@Composable
fun UserTimerWithDisposableEffect(
    userId: Int,
    onLog: (String) -> Unit
) {
    var tickCount by remember(userId) { mutableStateOf(0) }

    DisposableEffect(userId) {
        // Setup: 리소스 등록
        onLog("🟢 Setup: 사용자 $userId 타이머 시작")

        val timer = Timer()
        timer.scheduleAtFixedRate(0, 1000) {
            tickCount++
        }

        // onDispose: 리소스 해제
        onDispose {
            onLog("🔴 Dispose: 사용자 $userId 타이머 정리")
            timer.cancel()
        }
    }

    Text(
        text = "⏱️ 사용자 $userId 타이머: ${tickCount}초",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

/**
 * DisposableEffect 예제 2: 컴포저블 제거 시 cleanup
 * 컴포저블이 사라지면 타이머가 정리됨
 */
@Composable
fun GlobalTimerComponent(
    onLog: (String) -> Unit
) {
    var globalTicks by remember { mutableStateOf(0) }

    DisposableEffect(Unit) {
        onLog("🟢 Setup: 글로벌 타이머 시작")

        val timer = Timer()
        timer.scheduleAtFixedRate(0, 1000) {
            globalTicks++
        }

        onDispose {
            onLog("🔴 Dispose: 글로벌 타이머 정리")
            timer.cancel()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFE3F2FD),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "⏱️ 글로벌 타이머 실행 중",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2)
        )
        Text(
            text = "${globalTicks}초 경과",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DisposableEffectScreenPreview() {
    LearningTestTheme {
        DisposableEffectScreen()
    }
}
