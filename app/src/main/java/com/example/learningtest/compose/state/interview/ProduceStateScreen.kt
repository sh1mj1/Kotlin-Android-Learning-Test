package com.example.learningtest.compose.state.interview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.learningtest.ui.theme.LearningTestTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 네트워크 이미지 로딩 결과
 */
sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

/**
 * 가짜 이미지 데이터
 */
data class ImageData(
    val url: String,
    val width: Int,
    val height: Int,
    val loadedAt: String
)

/**
 * 이미지 저장소 시뮬레이션
 */
class ImageRepository {
    suspend fun load(url: String): ImageData? {
        // 네트워크 지연 시뮬레이션 (3초 - 로딩 상태 명확히 보기 위해)
        delay(3000)

        // "error"가 포함된 URL은 실패 처리
        return if (url.contains("error")) {
            null
        } else {
            ImageData(
                url = url,
                width = 800,
                height = 600,
                loadedAt = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            )
        }
    }
}

/**
 * 실시간 타이머 Flow
 */
fun tickerFlow(intervalMs: Long): Flow<Int> = flow {
    var count = 0
    while (true) {
        emit(count++)
        delay(intervalMs)
    }
}

/**
 * 예제 1: Flow → State 변환
 * produceState로 Flow를 Compose State로 변환
 */
@Composable
fun produceTickerState(intervalMs: Long): State<Int> {
    return produceState(initialValue = 0, intervalMs) {
        tickerFlow(intervalMs).collect {
            value = it
        }
    }
}

/**
 * 예제 2: 네트워크 이미지 로딩
 * produceState로 비동기 데이터를 State로 변환
 */
@Composable
fun loadNetworkImage(
    url: String,
    imageRepository: ImageRepository = ImageRepository()
): State<Result<ImageData>> {
    // key가 변경되면 실행 중인 프로듀서 취소 후 재실행
    return produceState<Result<ImageData>>(
        initialValue = Result.Loading,
        key1 = url,
        key2 = imageRepository
    ) {
        // ⭐ 즉시 Loading 상태로 설정 - 버튼 클릭 시 바로 로딩 UI 표시
        value = Result.Loading

        // 코루틴 내에서 suspend 함수 호출 가능
        val image = imageRepository.load(url)

        // State 업데이트 - 이 부분에서 리컴포지션 트리거
        value = if (image == null) {
            Result.Error("이미지 로딩 실패")
        } else {
            Result.Success(image)
        }
    }
}

/**
 * produceState 예제 화면
 */
@Composable
fun ProduceStateScreen() {
    // Lorem Picsum - 무료 랜덤 이미지 서비스 사용
    var imageUrl by remember { mutableStateOf("https://picsum.photos/800/600?random=1") }
    var tickerInterval by remember { mutableIntStateOf(1000) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 제목
        Text(
            text = "produceState",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "비-Compose 상태를 Compose 상태로 변환",
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
                    text = "외부 데이터 소스 → Compose State 변환기",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "• Flow, LiveData, 콜백 등을 Compose State로 변환",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "• 컴포지션 시작 시 실행, 종료 시 자동 취소",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "• 내부적으로 LaunchedEffect + mutableStateOf 조합",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        HorizontalDivider()

        // 예제 1: Flow → State
        FlowToStateExample(
            tickerInterval = tickerInterval,
            onIntervalChange = { tickerInterval = it }
        )

        HorizontalDivider()

        // 예제 2: 네트워크 이미지 로딩
        NetworkImageLoadingExample(
            imageUrl = imageUrl,
            onUrlChange = { imageUrl = it }
        )
    }
}

/**
 * 예제 1: Flow → State 변환
 */
@Composable
fun FlowToStateExample(
    tickerInterval: Int,
    onIntervalChange: (Int) -> Unit
) {
    // produceState로 Flow를 State로 변환
    val tickerValue by produceTickerState(intervalMs = tickerInterval.toLong())

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
                text = "📊 예제 1: Flow → State",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1976D2)
            )

            Text(
                text = "실시간 Flow 데이터를 Compose State로 변환",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // 타이머 값 표시
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "⏱️ 실시간 카운터",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "$tickerValue",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF1976D2)
                )

                Text(
                    text = "매 ${tickerInterval}ms 마다 업데이트",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // 인터벌 조정 버튼
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "업데이트 간격 조정:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onIntervalChange(500) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("빠름\n(500ms)", fontSize = 11.sp)
                    }
                    Button(
                        onClick = { onIntervalChange(1000) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("보통\n(1초)", fontSize = 11.sp)
                    }
                    Button(
                        onClick = { onIntervalChange(2000) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("느림\n(2초)", fontSize = 11.sp)
                    }
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
                    text = "💡 동작 방식:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )
                Text(
                    text = "1. tickerFlow()가 계속 새 값을 emit",
                    fontSize = 11.sp,
                    color = Color(0xFF1565C0)
                )
                Text(
                    text = "2. produceState가 collect하여 State 업데이트",
                    fontSize = 11.sp,
                    color = Color(0xFF1565C0)
                )
                Text(
                    text = "3. State 변경 시 자동 리컴포지션",
                    fontSize = 11.sp,
                    color = Color(0xFF1565C0)
                )
                Text(
                    text = "4. 간격 변경 시 기존 Flow 취소 후 새 Flow 시작",
                    fontSize = 11.sp,
                    color = Color(0xFF1565C0)
                )
            }
        }
    }
}

/**
 * 예제 2: 네트워크 이미지 로딩
 */
@Composable
fun NetworkImageLoadingExample(
    imageUrl: String,
    onUrlChange: (String) -> Unit
) {
    // produceState로 비동기 데이터 로딩
    val imageState by loadNetworkImage(url = imageUrl)

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
                text = "🖼️ 예제 2: 네트워크 데이터 로딩",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF388E3C)
            )

            Text(
                text = "비동기 데이터를 State로 변환 (Loading/Success/Error)",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // 현재 상태 표시
            when (val result = imageState) {
                is Result.Loading -> {
                    // 로딩 중 UI - 명확하고 크게
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(4f / 3f)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(64.dp),
                                color = Color(0xFF388E3C),
                                strokeWidth = 6.dp
                            )
                            Text(
                                text = "이미지 로딩 중...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray
                            )
                            Text(
                                text = "(3초 소요)",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                is Result.Success -> {
                    // 성공 - 실제 이미지 표시
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "✅ 로딩 성공!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF388E3C)
                        )

                        // 실제 이미지 표시
                        AsyncImage(
                            model = result.data.url,
                            contentDescription = "로드된 이미지",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(4f / 3f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray),
                            contentScale = ContentScale.Crop
                        )

                        // 이미지 정보
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFFF1F8E9),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "크기: ${result.data.width} x ${result.data.height}",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "로드 시각: ${result.data.loadedAt}",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                is Result.Error -> {
                    // 에러 UI
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(4f / 3f)
                            .background(
                                color = Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "❌",
                                fontSize = 48.sp
                            )
                            Text(
                                text = result.message,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD32F2F)
                            )
                        }
                    }
                }
            }

            // URL 선택 버튼
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "다른 이미지 로드:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onUrlChange("https://picsum.photos/800/600?random=1") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("랜덤\n이미지 1", fontSize = 11.sp)
                    }
                    Button(
                        onClick = { onUrlChange("https://picsum.photos/800/600?random=2") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("랜덤\n이미지 2", fontSize = 11.sp)
                    }
                    Button(
                        onClick = { onUrlChange("https://picsum.photos/800/600?random=3") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("랜덤\n이미지 3", fontSize = 11.sp)
                    }
                }

                Button(
                    onClick = { onUrlChange("https://error.invalid/image.jpg") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("에러 발생시키기 (잘못된 URL)", fontSize = 11.sp)
                }
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
                    text = "1. 초기값: Result.Loading 상태로 시작",
                    fontSize = 11.sp,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "2. suspend 함수로 이미지 로딩 (2초 소요)",
                    fontSize = 11.sp,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "3. 성공/실패에 따라 State 업데이트",
                    fontSize = 11.sp,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "4. URL 변경 시 이전 로딩 취소 후 새로 시작",
                    fontSize = 11.sp,
                    color = Color(0xFF2E7D32)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProduceStateScreenPreview() {
    LearningTestTheme {
        ProduceStateScreen()
    }
}
