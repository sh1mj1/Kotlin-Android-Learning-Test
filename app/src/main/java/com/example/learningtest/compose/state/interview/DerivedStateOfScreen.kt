package com.example.learningtest.compose.state.interview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * derivedStateOf 예제 화면
 *
 * derivedStateOf의 핵심 동작 확인:
 * 1. 하나 이상의 State로부터 파생(계산)되는 새로운 State
 * 2. 원본 State가 변경될 때만 재계산
 * 3. 불필요한 recomposition 방지 (성능 최적화)
 * 4. Snapshot System의 자동 의존성 추적
 */
@Composable
fun DerivedStateOfScreen() {
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
            text = "derivedStateOf",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "파생된 상태로 최적화하기",
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
                    text = "하나 이상의 State로부터 파생(계산)되는 새로운 State",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "• 원본 State 변경 시에만 재계산",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "• 불필요한 recomposition 방지",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "• Snapshot System이 의존성 자동 추적",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        HorizontalDivider()

        // 예제 1: 기본 사용법 - 리스트 필터링
        BasicFilteringExample(onLog = { addLog(it) })

        HorizontalDivider()

        // 예제 2: derivedStateOf vs remember 비교
        ComparisonExample(onLog = { addLog(it) })

        HorizontalDivider()

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
                                    log.contains("🔄 재계산") -> Color(0xFFFF9800)
                                    log.contains("✅") -> Color(0xFF4CAF50)
                                    log.contains("⚡ 캐시") -> Color(0xFF2196F3)
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
    }
}

/**
 * 예제 1: 기본 사용법 - 리스트 필터링
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BasicFilteringExample(onLog: (String) -> Unit) {
    // 전체 태스크 리스트
    val tasks = remember {
        mutableStateListOf(
            Task(1, "코드 리뷰", TaskPriority.HIGH),
            Task(2, "문서 작성", TaskPriority.LOW),
            Task(3, "버그 수정", TaskPriority.HIGH),
            Task(4, "회의 참석", TaskPriority.MEDIUM),
            Task(5, "리팩토링", TaskPriority.LOW),
        )
    }

    // ⭐ derivedStateOf를 사용한 파생 상태
    // tasks에서 HIGH priority만 필터링한 결과를 계산
    val highTaskPriorityTasks by remember {
        derivedStateOf {
            tasks.filter { it.taskPriority == TaskPriority.HIGH }
        }
    }

    // 상태 추적
    var lastAction by remember { mutableStateOf("대기 중") }
    var calculationCount by remember { mutableIntStateOf(0) }

    // 부수 효과는 LaunchedEffect로 분리
    LaunchedEffect(highTaskPriorityTasks.size, tasks.size) {
        calculationCount++
        lastAction = "재계산됨 (${highTaskPriorityTasks.size}개)"
        onLog("🔄 재계산: highPriorityTasks 필터링 (${highTaskPriorityTasks.size}개)")
    }

    var recompositionTrigger by remember { mutableIntStateOf(0) }

    // Dialog state: null이면 안보임, "high_add" or "recomposition"이면 해당 설명 표시
    var showExplanationDialog by remember { mutableStateOf<String?>(null) }

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
                text = "1️⃣ 기본 사용법: 리스트 필터링",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1976D2)
            )

            // 상태 인디케이터
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = when {
                            lastAction.contains("재계산") -> Color(0xFFE8F5E9)
                            lastAction.contains("캐시") -> Color(0xFFE3F2FD)
                            else -> Color(0xFFF5F5F5)
                        },
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📊 $lastAction",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "계산: ${calculationCount}회",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )
            }

            // 리컴포지션 카운터 표시
            Text(
                text = "📊 리컴포지션 횟수: ${recompositionTrigger}회",
                fontSize = 12.sp,
                color = Color(0xFF1976D2),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 4.dp)
            )

            // 안내 텍스트
            Text(
                text = "💡 버튼을 길게 누르면 자세한 설명을 볼 수 있습니다",
                fontSize = 11.sp,
                color = Color(0xFF757575),
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // HIGH 추가 버튼
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .combinedClickable(
                            onClick = {
                                val newTask = Task(
                                    id = tasks.size + 1,
                                    name = "새 태스크 ${tasks.size + 1}",
                                    taskPriority = TaskPriority.HIGH
                                )
                                tasks.add(0, newTask)
                                recompositionTrigger++
                                onLog("✅ 태스크 추가: ${newTask.name} → 재계산 발생")
                            },
                            onLongClick = {
                                showExplanationDialog = "high_add"
                            }
                        ),
                    shape = ButtonDefaults.shape,
                    color = MaterialTheme.colorScheme.primary,
                    shadowElevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier
                            .defaultMinSize(
                                minWidth = ButtonDefaults.MinWidth,
                                minHeight = ButtonDefaults.MinHeight
                            )
                            .padding(ButtonDefaults.ContentPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "HIGH 추가",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                // 리컴포지션 버튼
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .combinedClickable(
                            onClick = {
                                recompositionTrigger++
                                lastAction = "캐시 사용 (재계산 안함)"
                                onLog("📊 리컴포지션 유발 → 재계산 없음")
                            },
                            onLongClick = {
                                showExplanationDialog = "recomposition"
                            }
                        ),
                    shape = ButtonDefaults.shape,
                    color = MaterialTheme.colorScheme.primary,
                    shadowElevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier
                            .defaultMinSize(
                                minWidth = ButtonDefaults.MinWidth,
                                minHeight = ButtonDefaults.MinHeight
                            )
                            .padding(ButtonDefaults.ContentPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "리컴포지션",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            Text(
                text = "tasks가 변경될 때만 highPriorityTasks가 재계산됩니다",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // 전체 태스크 표시
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = "전체 태스크 (${tasks.size}개)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                tasks.forEach { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = task.name, fontSize = 13.sp)
                        Text(
                            text = task.taskPriority.name,
                            fontSize = 13.sp,
                            color = when (task.taskPriority) {
                                TaskPriority.HIGH -> Color(0xFFE53935)
                                TaskPriority.MEDIUM -> Color(0xFFFB8C00)
                                TaskPriority.LOW -> Color(0xFF43A047)
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // HIGH priority 태스크만 표시
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = "⭐ HIGH Priority 태스크 (${highTaskPriorityTasks.size}개)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE53935),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                highTaskPriorityTasks.forEach { task ->
                    Text(
                        text = "• ${task.name}",
                        fontSize = 13.sp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
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
                    text = "💡 핵심:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )
                Text(
                    text = "• 'HIGH 추가' 클릭 → tasks 변경 → derivedStateOf 재계산",
                    fontSize = 11.sp,
                    color = Color(0xFF1565C0)
                )
                Text(
                    text = "• '리컴포지션' 클릭 → tasks 미변경 → 재계산 없음 (캐시 사용)",
                    fontSize = 11.sp,
                    color = Color(0xFF1565C0)
                )
            }
        }
    }

    // 설명 Dialog
    if (showExplanationDialog != null) {
        AlertDialog(
            onDismissRequest = { showExplanationDialog = null },
            title = {
                Text(
                    text = when (showExplanationDialog) {
                        "high_add" -> "🔍 HIGH 추가 버튼"
                        "recomposition" -> "🔍 리컴포지션 버튼"
                        else -> ""
                    },
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when (showExplanationDialog) {
                        "high_add" -> {
                            Text(
                                text = "📌 이 버튼의 의도",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1976D2)
                            )
                            Text(
                                text = "derivedStateOf의 재계산 트리거를 직접 확인하기 위한 버튼입니다. tasks 리스트를 실제로 변경하여 파생 상태가 어떻게 반응하는지 관찰합니다.",
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "💻 코드 동작",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1976D2)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color(0xFFF5F5F5),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "1. tasks.add(0, newTask)",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF37474F)
                                )
                                Text(
                                    text = "   → 리스트 상단에 HIGH 태스크 추가",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                                Text(
                                    text = "2. recompositionTrigger++",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF37474F)
                                )
                                Text(
                                    text = "   → 리컴포지션 횟수 증가",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                                Text(
                                    text = "3. derivedStateOf 자동 재계산",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFE53935),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "   → tasks 변경 감지 → 필터링 재실행",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                            }
                        }
                        "recomposition" -> {
                            Text(
                                text = "📌 이 버튼의 의도",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1976D2)
                            )
                            Text(
                                text = "derivedStateOf의 캐싱 동작을 확인하기 위한 버튼입니다. 리컴포지션은 발생하지만 tasks는 변경되지 않아서 파생 상태가 재계산되지 않는 것을 관찰합니다.",
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "💻 코드 동작",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1976D2)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color(0xFFF5F5F5),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "1. recompositionTrigger++",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF37474F)
                                )
                                Text(
                                    text = "   → 리컴포지션 유발",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                                Text(
                                    text = "2. lastAction 변경",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF37474F)
                                )
                                Text(
                                    text = "   → UI 상태 업데이트",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                                Text(
                                    text = "3. tasks는 변경 안함",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "   → derivedStateOf 캐시 사용 (재계산 X)",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showExplanationDialog = null }
                ) {
                    Text("확인")
                }
            }
        )
    }
}

/**
 * 예제 2: derivedStateOf vs remember 비교 - 다중 의존성 추적
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComparisonExample(onLog: (String) -> Unit) {
    var firstName by remember { mutableStateOf("John") }
    var lastName by remember { mutableStateOf("Doe") }
    var age by remember { mutableIntStateOf(30) }

    // Dialog state: "first_name", "last_name", "age"
    var showComparisonDialog by remember { mutableStateOf<String?>(null) }

    // remember 사용: 모든 의존성을 명시적으로 지정해야 함
    var rememberCalculationCount by remember { mutableIntStateOf(0) }
    val fullNameRemember = remember(firstName, lastName, age) {
        "$firstName $lastName (${age}세)"  // ✅ 모든 변수 사용 → 모두 의존성
    }

    // derivedStateOf 사용: 자동으로 의존성 추적
    var derivedCalculationCount by remember { mutableIntStateOf(0) }
    val fullNameDerived by remember {
        derivedStateOf {
            "$firstName $lastName"  // ✅ age는 읽지 않음 → age 변경 시 재계산 안함!
        }
    }

    // 부수 효과는 LaunchedEffect로 분리
    LaunchedEffect(firstName, lastName, age) {
        rememberCalculationCount++
        onLog("🔄 remember: 계산 #${rememberCalculationCount} (모든 변경 감지)")
    }

    LaunchedEffect(fullNameDerived) {
        derivedCalculationCount++
        onLog("🔄 derivedStateOf: 계산 #${derivedCalculationCount} (firstName, lastName만)")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "2️⃣ derivedStateOf vs remember",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFFE65100)
            )

            Text(
                text = "다중 의존성 자동 추적의 차이를 확인하세요",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // 입력 필드들
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // First Name
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "First Name:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.width(100.dp)
                    )
                    Text(
                        text = firstName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        modifier = Modifier.combinedClickable(
                            onClick = {
                                firstName += "n"
                                onLog("✏️ firstName 변경: $firstName")
                            },
                            onLongClick = {
                                showComparisonDialog = "first_name"
                            }
                        ),
                        shape = ButtonDefaults.shape,
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 2.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .defaultMinSize(
                                    minWidth = 58.dp,
                                    minHeight = ButtonDefaults.MinHeight
                                )
                                .padding(ButtonDefaults.ContentPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                // Last Name
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Last Name:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.width(100.dp)
                    )
                    Text(
                        text = lastName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        modifier = Modifier.combinedClickable(
                            onClick = {
                                lastName += "e"
                                onLog("✏️ lastName 변경: $lastName")
                            },
                            onLongClick = {
                                showComparisonDialog = "last_name"
                            }
                        ),
                        shape = ButtonDefaults.shape,
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 2.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .defaultMinSize(
                                    minWidth = 58.dp,
                                    minHeight = ButtonDefaults.MinHeight
                                )
                                .padding(ButtonDefaults.ContentPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                // Age (무관한 상태)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Age:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.width(100.dp)
                    )
                    Text(
                        text = "${age}세",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF9800),
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        modifier = Modifier.combinedClickable(
                            onClick = {
                                age++
                                onLog("🎂 age 변경: $age (derivedStateOf는 재계산 안함!)")
                            },
                            onLongClick = {
                                showComparisonDialog = "age"
                            }
                        ),
                        shape = ButtonDefaults.shape,
                        color = Color(0xFFFF9800),
                        shadowElevation = 2.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .defaultMinSize(
                                    minWidth = 58.dp,
                                    minHeight = ButtonDefaults.MinHeight
                                )
                                .padding(ButtonDefaults.ContentPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // 비교 결과
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color(0xFFFFCDD2),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "remember()",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC62828)
                    )
                    Text(
                        text = "계산: ${rememberCalculationCount}회",
                        fontSize = 13.sp,
                        color = Color(0xFFD32F2F),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = fullNameRemember,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "age도 추적함",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color(0xFFC8E6C9),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "derivedStateOf",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = "계산: ${derivedCalculationCount}회 ✅",
                        fontSize = 13.sp,
                        color = Color(0xFF388E3C),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = fullNameDerived,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "age 추적 안함",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }

            // 설명
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFFFE0B2),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "💡 핵심 차이:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100)
                )
                Text(
                    text = "• remember: 모든 의존성을 명시적으로 key에 지정 필요",
                    fontSize = 11.sp,
                    color = Color(0xFFEF6C00)
                )
                Text(
                    text = "• derivedStateOf: 실제로 읽은 상태만 자동 추적",
                    fontSize = 11.sp,
                    color = Color(0xFFEF6C00)
                )
                Text(
                    text = "⚡ age 버튼을 눌러보세요! derivedStateOf는 재계산하지 않습니다",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFBF360C)
                )
            }
        }
    }

    // 설명 Dialog
    if (showComparisonDialog != null) {
        AlertDialog(
            onDismissRequest = { showComparisonDialog = null },
            title = {
                Text(
                    text = when (showComparisonDialog) {
                        "first_name" -> "🔍 First Name + 버튼"
                        "last_name" -> "🔍 Last Name + 버튼"
                        "age" -> "🔍 Age + 버튼"
                        else -> ""
                    },
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when (showComparisonDialog) {
                        "first_name" -> {
                            Text(
                                text = "📌 이 버튼의 의도",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1976D2)
                            )
                            Text(
                                text = "firstName 변경이 remember와 derivedStateOf에 미치는 영향을 확인합니다. 둘 다 firstName을 사용하므로 모두 재계산됩니다.",
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "💻 코드 동작",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1976D2)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color(0xFFF5F5F5),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "1. firstName += \"n\"",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF37474F)
                                )
                                Text(
                                    text = "   → firstName 변경",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                                Text(
                                    text = "2. remember 재계산 ✅",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFE53935),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "   → firstName이 key이므로 재계산",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                                Text(
                                    text = "3. derivedStateOf 재계산 ✅",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFE53935),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "   → firstName을 읽으므로 재계산",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                            }
                        }
                        "last_name" -> {
                            Text(
                                text = "📌 이 버튼의 의도",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1976D2)
                            )
                            Text(
                                text = "lastName 변경이 remember와 derivedStateOf에 미치는 영향을 확인합니다. 둘 다 lastName을 사용하므로 모두 재계산됩니다.",
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "💻 코드 동작",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1976D2)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color(0xFFF5F5F5),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "1. lastName += \"e\"",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF37474F)
                                )
                                Text(
                                    text = "   → lastName 변경",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                                Text(
                                    text = "2. remember 재계산 ✅",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFE53935),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "   → lastName이 key이므로 재계산",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                                Text(
                                    text = "3. derivedStateOf 재계산 ✅",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFE53935),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "   → lastName을 읽으므로 재계산",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                            }
                        }
                        "age" -> {
                            Text(
                                text = "📌 이 버튼의 의도 (핵심!)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFFFF9800)
                            )
                            Text(
                                text = "derivedStateOf의 자동 의존성 추적 vs remember의 명시적 key 차이를 보여줍니다. age는 remember의 key이지만 derivedStateOf는 읽지 않으므로 차이가 명확히 드러납니다!",
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "💻 코드 동작",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFFFF9800)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color(0xFFF5F5F5),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "1. age++",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF37474F)
                                )
                                Text(
                                    text = "   → age 변경",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                                Text(
                                    text = "2. remember 재계산 ✅",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFE53935),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "   → age가 key이므로 재계산",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                                Text(
                                    text = "3. derivedStateOf 재계산 안함 ❌",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "   → age를 읽지 않으므로 캐시 사용!",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "⚡ 핵심 차이점",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFFFF9800)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color(0xFFFFF3E0),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "• remember: 모든 의존성을 key에 명시",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "• derivedStateOf: 실제로 읽은 것만 추적",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showComparisonDialog = null }
                ) {
                    Text("확인")
                }
            }
        )
    }
}

enum class TaskPriority {
    LOW, MEDIUM, HIGH
}

data class Task(
    val id: Int,
    val name: String,
    val taskPriority: TaskPriority,
)

@Preview(showBackground = true)
@Composable
fun DerivedStateOfScreenPreview() {
    LearningTestTheme {
        DerivedStateOfScreen()
    }
}

