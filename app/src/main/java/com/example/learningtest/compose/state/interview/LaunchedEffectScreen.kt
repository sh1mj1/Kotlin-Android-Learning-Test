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
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * LaunchedEffect 예제 화면
 *
 * LaunchedEffect의 핵심 동작 확인:
 * 1. key가 바뀌면 이전 코루틴 취소 후 새로운 코루틴 실행
 * 2. 리컴포지션 시에는 다시 실행되지 않음
 * 3. 컴포저블 생명주기에 맞춰 자동 취소
 */
@Composable
fun LaunchedEffectScreen() {
    var selectedUserId by remember { mutableIntStateOf(1) }
    var showApiComponent by remember { mutableStateOf(false) }
    val eventLog = remember { mutableStateListOf<String>() }

    // 로그 추가 헬퍼
    fun addLog(message: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        eventLog.add(0, "[$timestamp] $message")
        if (eventLog.size > 25) {
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
            text = "LaunchedEffect",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "컴포지션 시 코루틴 실행",
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
                    text = "1️⃣ Key 변경 시 이전 코루틴 취소",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = "userId를 변경하면 진행 중이던 API 호출이 취소되고 새로운 호출이 시작됩니다. \n 아래 상태 UI의 LaunchedEffect 의 key 가 userId",
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

                // API 호출 시뮬레이션
                UserApiCallComponent(
                    userId = selectedUserId,
                    onLog = { addLog(it) }
                )
            }
        }

        HorizontalDivider()

        // 2. 리컴포지션 vs 재실행
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "2️⃣ 리컴포지션 시 재실행되지 않음",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = "버튼을 눌러 리컴포지션을 유발해도 LaunchedEffect(Unit)은 재실행되지 않습니다.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                RecompositionTestComponent(
                    onLog = { addLog(it) }
                )
            }
        }

        HorizontalDivider()

        // 3. 생명주기 관리
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "3️⃣ 컴포저블 생명주기에 맞춰 자동 취소",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = "컴포넌트를 숨기면 LaunchedEffect가 자동으로 취소됩니다.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Button(
                    onClick = {
                        showApiComponent = !showApiComponent
                        addLog(
                            if (showApiComponent)
                                "👁️ API 컴포넌트 표시"
                            else
                                "🙈 API 컴포넌트 숨김"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (showApiComponent) "컴포넌트 숨기기" else "컴포넌트 보이기",
                        modifier = Modifier.padding(8.dp)
                    )
                }

                if (showApiComponent) {
                    LongRunningApiComponent(
                        onLog = { addLog(it) }
                    )
                }
            }
        }

        // 이벤트 로그
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
                            .height(250.dp)
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
                                    log.contains("🚀 시작") -> Color(0xFF4CAF50)
                                    log.contains("❌ 취소") -> Color(0xFFF44336)
                                    log.contains("✅ 완료") -> Color(0xFF2196F3)
                                    log.contains("🔄") -> Color(0xFFFF9800)
                                    log.contains("📊") -> Color(0xFF9C27B0)
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
                    text = "💡 LaunchedEffect 핵심 동작",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "• key 변경 시: 이전 코루틴 자동 취소 후 새 코루틴 실행",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "• 리컴포지션 시: 재실행되지 않음 (동일 key)",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "• 컴포저블 제거 시: 코루틴 자동 취소",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "• 용도: API 호출, 애니메이션, 일회성 이벤트",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

/**
 * 예제 1: Key 변경 시 이전 코루틴 취소
 * userId가 변경되면 진행 중이던 API 호출이 취소되고 새로운 호출 시작
 */
@Composable
fun UserApiCallComponent(
    userId: Int,
    onLog: (String) -> Unit
) {
    var apiStatus by remember(userId) { mutableStateOf("대기 중") }
    var userData by remember(userId) { mutableStateOf<String?>(null) }
    var progress by remember(userId) { mutableIntStateOf(0) }

    LaunchedEffect(userId) {
        onLog("🚀 시작: 사용자 $userId API 호출")
        apiStatus = "로딩 중..."
        userData = null
        progress = 0

        try {
            // API 호출 시뮬레이션 (3초)
            for (i in 1..30) {
                delay(100)
                progress = (i * 100) / 30
            }

            // 성공
            userData = "사용자 $userId 데이터"
            apiStatus = "완료"
            onLog("✅ 완료: 사용자 $userId 데이터 로드 성공")
        } catch (e: Exception) {
            // 취소되면 여기로 옴
            apiStatus = "취소됨"
            onLog("❌ 취소: 사용자 $userId API 호출 취소됨")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = when (apiStatus) {
                    "로딩 중..." -> Color(0xFFFFF3E0)
                    "완료" -> Color(0xFFE8F5E9)
                    "취소됨" -> Color(0xFFFFEBEE)
                    else -> Color(0xFFF5F5F5)
                },
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "상태: $apiStatus",
            fontWeight = FontWeight.Medium
        )

        if (apiStatus == "로딩 중...") {
            Text(
                text = "진행률: $progress%",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        if (userData != null) {
            Text(
                text = "데이터: $userData",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )
        }
    }
}

/**
 * 예제 2: 리컴포지션 시 재실행되지 않음
 * 카운터를 증가시켜도 LaunchedEffect(Unit)은 한 번만 실행됨
 */
@Composable
fun RecompositionTestComponent(
    onLog: (String) -> Unit
) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    var effectExecutionCount by remember { mutableIntStateOf(0) }

    // LaunchedEffect(Unit) - 한 번만 실행
    LaunchedEffect(Unit) {
        effectExecutionCount++
        onLog("🎯 LaunchedEffect 실행 (${effectExecutionCount}번째)")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFE3F2FD),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "📊 리컴포지션 횟수: $recompositionCount",
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "🎯 Effect 실행 횟수: $effectExecutionCount",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2)
        )

        Button(
            onClick = {
                recompositionCount++
                onLog("📊 리컴포지션 유발 ($recompositionCount 번째)")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("리컴포지션 유발")
        }

        Text(
            text = "💡 LaunchedEffect(Unit)은 리컴포지션되어도 재실행되지 않습니다.",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

/**
 * 예제 3: 컴포저블 제거 시 자동 취소
 * 오래 실행되는 작업 중 컴포저블이 사라지면 자동 취소
 */
@Composable
fun LongRunningApiComponent(
    onLog: (String) -> Unit
) {
    var progress by remember { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        onLog("🚀 시작: 장시간 API 호출 (10초)")
        isRunning = true

        try {
            for (i in 1..100) {
                delay(100)
                progress = i
            }
            onLog("✅ 완료: 장시간 API 호출 완료")
            isRunning = false
        } catch (e: Exception) {
            onLog("❌ 취소: 장시간 API 호출 취소됨")
            isRunning = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFFF9C4),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "⏳ 장시간 작업 실행 중...",
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "진행률: $progress%",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF57C00)
        )
        Text(
            text = "💡 컴포넌트를 숨기면 작업이 자동으로 취소됩니다.",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LaunchedEffectScreenPreview() {
    LearningTestTheme {
        LaunchedEffectScreen()
    }
}
