package com.example.learningtest.compose.state.interview

import android.util.Log
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
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

private const val TAG = "SideEffectScreen"
/**
 * 외부 Analytics 시스템 시뮬레이션
 * SideEffect는 Compose 상태를 읽어서 외부 시스템에 동기화합니다.
 */
object AnalyticsSystem {
    // ✅ Compose State로 변경 - 로그 변경 시 자동으로 UI 업데이트
    private val eventLog: SnapshotStateList<String> = mutableListOf<String>().toMutableStateList()

    fun logInputChange(inputText: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        eventLog.add(0, "[$timestamp] 🔵 SideEffect 실행 - input: \"$inputText\"")
    }

    fun setUserProperty(key: String, value: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        eventLog.add(0, "[$timestamp] 🟢 SideEffect 실행 - $key=$value")
    }

    fun getEventLog(): SnapshotStateList<String> = eventLog

    fun clear() {
        eventLog.clear()
    }
}

/**
 * SideEffect 예제 화면
 */
@Composable
fun SideEffectScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 제목
        Text(
            text = "SideEffect",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Compose 상태를 외부 시스템에 동기화",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "💡 핵심 개념",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Composition 완료 후 외부 시스템에 부수효과 전달",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "• 매 Recomposition마다 실행 (key 없음)",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "• 코루틴이 아닌 동기 코드만 실행 가능",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "• Compose 상태 → 외부 시스템 동기화",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        HorizontalDivider()

        // 예제 1: 매 Recomposition마다 실행
        RecompositionTrackingExample()

        HorizontalDivider()

        // 예제 2: 외부 시스템 동기화
        ExternalSyncExample()

        HorizontalDivider()

        // Analytics 로그
        AnalyticsLogViewer()

        // 초기화 버튼
        Button(
            onClick = { AnalyticsSystem.clear() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("로그 초기화")
        }
    }
}

/**
 * 예제 1: 매 Recomposition마다 실행 확인
 */
@Composable
fun RecompositionTrackingExample() {
    var inputText by remember { mutableStateOf("") }

    // ⭐ 중요: 상태를 최상위에서 읽어야 전체 Composable이 recompose됨
    val currentInput = inputText  // 명시적으로 상태 읽기


    // ✅ SideEffect: Composition 성공 후 매번 실행
    SideEffect {
        AnalyticsSystem.logInputChange(currentInput)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "📊 예제 1: 매 Recomposition마다 실행",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1976D2)
            )

            Text(
                text = "TextField에 타이핑하면 SideEffect가 매번 실행됩니다",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // TextField
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("텍스트 입력") },
                placeholder = { Text("여기에 입력하세요...") },
                modifier = Modifier.fillMaxWidth()
            )

            // 현재 입력값 표시
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "현재 입력값",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = if (inputText.isEmpty()) "(비어있음)" else "\"$inputText\"",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF1976D2)
                )
                Text(
                    text = "글자 수: ${inputText.length}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // 설명
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFBBDEFB),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "💡 동작 원리:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )
                Text(
                    text = "1. TextField 타이핑 → inputText 상태 변경",
                    fontSize = 11.sp,
                    color = Color(0xFF1565C0)
                )
                Text(
                    text = "2. 상태 변경 → Recomposition 발생",
                    fontSize = 11.sp,
                    color = Color(0xFF1565C0)
                )
                Text(
                    text = "3. Composition 성공 → SideEffect 실행",
                    fontSize = 11.sp,
                    color = Color(0xFF1565C0)
                )
                Text(
                    text = "4. SideEffect에서 Analytics에 로그 기록",
                    fontSize = 11.sp,
                    color = Color(0xFF1565C0)
                )
                Text(
                    text = "⭐ 아래 로그 뷰어에서 실시간 확인 가능",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )
            }
        }
    }
}

/**
 * 예제 2: 외부 시스템 동기화
 */
@Composable
fun ExternalSyncExample() {
    var userName by remember { mutableStateOf("Guest") }
    var userAge by remember { mutableFloatStateOf(25f) }

    // ⭐ 중요: 상태를 최상위에서 읽어야 전체 Composable이 recompose됨
    val currentName = userName  // 명시적으로 상태 읽기
    val currentAge = userAge.toInt()  // 명시적으로 상태 읽기


    // ✅ SideEffect: Compose 상태를 외부 Analytics에 동기화
    SideEffect {
        AnalyticsSystem.setUserProperty("name", currentName)
        AnalyticsSystem.setUserProperty("age", currentAge.toString())
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "👤 예제 2: 외부 시스템 동기화",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF388E3C)
            )

            Text(
                text = "Compose 상태 변경 시 외부 Analytics에 자동 동기화",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // 사용자 이름 선택
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "사용자 이름: $userName",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { userName = "Alice" },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Alice", fontSize = 12.sp)
                    }
                    Button(
                        onClick = { userName = "Bob" },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Bob", fontSize = 12.sp)
                    }
                    Button(
                        onClick = { userName = "Charlie" },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Charlie", fontSize = 12.sp)
                    }
                }
            }

            // 나이 조절
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "나이: ${userAge.toInt()}세",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Slider(
                    value = userAge,
                    onValueChange = { userAge = it },
                    valueRange = 18f..60f,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 설명
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFC8E6C9),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "💡 동작 방식:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
                Text(
                    text = "1. userName 또는 userAge 변경 → Recomposition",
                    fontSize = 11.sp,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "2. Composition 성공 후 SideEffect 실행",
                    fontSize = 11.sp,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "3. 외부 Analytics에 최신 상태 동기화",
                    fontSize = 11.sp,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "4. 동기 코드만 가능 (suspend 함수 불가)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
            }
        }
    }
}

/**
 * Analytics 로그 뷰어
 */
@Composable
fun AnalyticsLogViewer() {
    // ✅ SnapshotStateList를 직접 읽음 - 변경 시 자동으로 recompose
    val logs = AnalyticsSystem.getEventLog()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF263238)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📊 Analytics 로그 (${logs.size}개)",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "🔴 실시간",
                    fontSize = 12.sp,
                    color = Color(0xFFFF5252),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF37474F),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (logs.isEmpty()) {
                    Text(
                        text = "아직 로그가 없습니다",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                } else {
                    // 최신 로그 10개 표시 (이미 최신이 앞에 있음)
                    logs.take(10).forEach { log ->
                        Text(
                            text = log,
                            color = if (log.contains("🔵")) Color(0xFF64B5F6) else Color(0xFF81C784),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    if (logs.size > 10) {
                        Text(
                            text = "... 그 외 ${logs.size - 10}개 로그",
                            color = Color.Gray,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Text(
                text = "💡 SideEffect가 매 Recomposition마다 실행되며, 외부 Analytics에 이벤트가 즉시 기록됩니다 (최신 로그가 위에 표시)",
                fontSize = 12.sp,
                color = Color(0xFFB0BEC5)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SideEffectScreenPreview() {
    LearningTestTheme {
        SideEffectScreen()
    }
}
