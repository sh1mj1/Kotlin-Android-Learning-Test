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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import java.util.UUID

/**
 * User 데이터 클래스
 */
data class User(
    val name: String,
    val userType: String
)

/**
 * Analytics 시뮬레이션 클래스
 * 인스턴스 생성 시마다 고유 ID 할당하여 추적 가능
 */
class MockAnalytics(val instanceId: String = UUID.randomUUID().toString().take(8)) {
    var userProperty: String = ""
        private set

    private val propertyUpdateLog = mutableListOf<String>()

    fun setUserProperty(key: String, value: String) {
        userProperty = value
        val timestamp = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        propertyUpdateLog.add("[$timestamp] $key=$value")
    }

    fun getUpdateLog(): List<String> = propertyUpdateLog.toList()
}

/**
 * ❌ 잘못된 방법: remember(user) 사용
 * → user가 바뀔 때마다 Analytics 인스턴스 재생성!
 */
@Composable
fun rememberAnalyticsWrong(user: User): MockAnalytics {
    // ❌ user를 key로 사용 → user 바뀌면 Analytics 객체 재생성
    val analytics = remember(user) {
        MockAnalytics().apply {
            setUserProperty("userType", user.userType)
        }
    }
    return analytics
}

/**
 * ✅ 올바른 방법: remember + SideEffect 분리
 * → Analytics는 한 번만 생성, 속성만 동기화
 */
@Composable
fun rememberAnalyticsCorrect(user: User): MockAnalytics {
    // ✅ key 없이 remember → Analytics는 한 번만 생성
    val analytics: MockAnalytics = remember {
        MockAnalytics()
    }

    // ✅ SideEffect로 속성만 동기화
    // user가 바뀔 때마다 성공적인 Recomposition 후에 실행
    SideEffect {
        analytics.setUserProperty("userType", user.userType)
    }

    return analytics
}

/**
 * SideEffect 예제 화면
 * remember + SideEffect 패턴의 장점 시연
 */
@Composable
fun SideEffectScreen() {
    var wrongInstanceCount by remember { mutableIntStateOf(0) }
    var correctInstanceCount by remember { mutableIntStateOf(0) }

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
            text = "remember + SideEffect 패턴",
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
                    text = "객체는 안정적으로, 동기화는 반응적으로",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "• 잘못된 방법: remember(user) → user 바뀌면 인스턴스 재생성",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "• 올바른 방법: remember + SideEffect → 인스턴스는 유지, 속성만 동기화",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        HorizontalDivider()

        // ❌ 잘못된 방법
        WrongWayExample(
            onInstanceCreated = { wrongInstanceCount++ }
        )

        HorizontalDivider()

        // ✅ 올바른 방법
        CorrectWayExample(
            onInstanceCreated = { correctInstanceCount++ }
        )

        HorizontalDivider()

        // 비교 요약
        ComparisonSummary(
            wrongInstanceCount = wrongInstanceCount,
            correctInstanceCount = correctInstanceCount
        )

        // 초기화 버튼
        Button(
            onClick = {
                wrongInstanceCount = 0
                correctInstanceCount = 0
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("카운터 초기화")
        }
    }
}

/**
 * ❌ 잘못된 방법 예제
 */
@Composable
fun WrongWayExample(
    onInstanceCreated: () -> Unit
) {
    var selectedUser by remember { mutableStateOf(User("게스트", "Guest")) }
    val analytics = rememberAnalyticsWrong(selectedUser)

    // 인스턴스가 새로 생성될 때마다 카운트 증가
    SideEffect {
        onInstanceCreated()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "❌ 잘못된 방법",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFFC62828)
            )

            Text(
                text = "remember(user) 사용",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // User 선택 버튼들
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "👤 사용자 선택",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { selectedUser = User("게스트", "Guest") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("게스트", fontSize = 12.sp)
                    }
                    Button(
                        onClick = { selectedUser = User("무료회원", "Free") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("무료", fontSize = 12.sp)
                    }
                    Button(
                        onClick = { selectedUser = User("프리미엄", "Premium") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("프리미엄", fontSize = 12.sp)
                    }
                }

                Text(
                    text = "현재: ${selectedUser.name} (${selectedUser.userType})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Analytics 인스턴스 ID: ${analytics.instanceId}",
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC62828)
                )

                Text(
                    text = "현재 userType: ${analytics.userProperty}",
                    fontSize = 13.sp
                )

                HorizontalDivider()

                Text(
                    text = "속성 업데이트 로그:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                analytics.getUpdateLog().takeLast(3).forEach { log ->
                    Text(
                        text = log,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFFFF5252)
                    )
                }
            }

            Text(
                text = "⚠️ 문제점: User 바뀔 때마다 새 Analytics 인스턴스 생성!",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFC62828)
            )

            Text(
                text = "→ 기존 세션, 이벤트 기록 등이 모두 초기화됨",
                fontSize = 12.sp,
                color = Color(0xFFC62828)
            )
        }
    }
}

/**
 * ✅ 올바른 방법 예제
 */
@Composable
fun CorrectWayExample(
    onInstanceCreated: () -> Unit
) {
    var selectedUser by remember { mutableStateOf(User("게스트", "Guest")) }
    val analytics = rememberAnalyticsCorrect(selectedUser)

    // 첫 composition 때만 카운트 (remember의 초기화 블록은 한 번만 실행)
    remember {
        onInstanceCreated()
        null
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
                text = "✅ 올바른 방법",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF2E7D32)
            )

            Text(
                text = "remember + SideEffect 분리",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // User 선택 버튼들
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "👤 사용자 선택",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { selectedUser = User("게스트", "Guest") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("게스트", fontSize = 12.sp)
                    }
                    Button(
                        onClick = { selectedUser = User("무료회원", "Free") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("무료", fontSize = 12.sp)
                    }
                    Button(
                        onClick = { selectedUser = User("프리미엄", "Premium") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("프리미엄", fontSize = 12.sp)
                    }
                }

                Text(
                    text = "현재: ${selectedUser.name} (${selectedUser.userType})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Analytics 인스턴스 ID: ${analytics.instanceId}",
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )

                Text(
                    text = "현재 userType: ${analytics.userProperty}",
                    fontSize = 13.sp
                )

                HorizontalDivider()

                Text(
                    text = "속성 업데이트 로그:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                analytics.getUpdateLog().takeLast(3).forEach { log ->
                    Text(
                        text = log,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            Text(
                text = "✅ 장점: Analytics 인스턴스는 유지, 속성만 업데이트!",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2E7D32)
            )

            Text(
                text = "→ 세션 연속성 유지, 메모리 효율적, 안정적",
                fontSize = 12.sp,
                color = Color(0xFF2E7D32)
            )
        }
    }
}

/**
 * 비교 요약
 */
@Composable
fun ComparisonSummary(
    wrongInstanceCount: Int,
    correctInstanceCount: Int
) {
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
            Text(
                text = "📊 인스턴스 생성 횟수 비교",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "❌ 잘못된 방법",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFF5252)
                    )
                    Text(
                        text = "$wrongInstanceCount 회",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFFFF5252)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "✅ 올바른 방법",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4CAF50)
                    )
                    Text(
                        text = "$correctInstanceCount 회",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            HorizontalDivider(color = Color(0xFF37474F))

            Text(
                text = "💡 사용자를 여러 번 바꿔보세요!",
                fontSize = 12.sp,
                color = Color(0xFFB0BEC5)
            )

            Text(
                text = "잘못된 방법은 사용자 바꿀 때마다 인스턴스 재생성,\n올바른 방법은 단 1회만 생성됩니다.",
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
