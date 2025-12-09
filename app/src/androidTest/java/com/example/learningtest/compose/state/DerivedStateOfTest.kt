package com.example.learningtest.compose.state

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

/**
 * derivedStateOf 학습 테스트
 *
 * 검증 목표:
 * 1. 원본 State 변경 시 파생 상태 재계산
 * 2. 무관한 리컴포지션 시 재계산 안 함 (캐싱)
 * 3. 여러 State 중 실제 읽은 것만 자동 추적
 * 4. 리스트 필터링 최적화
 * 5. remember vs derivedStateOf 비교
 *
 * ⚠️ 주의: 실제 기기에서 테스트 시 화면이 잠금되어 있으면 모든 테스트가 실패합니다.
 *          테스트 실행 전 화면을 켜고 잠금을 해제해주세요.
 */
class DerivedStateOfTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun 원본_State_변경_시_파생_상태가_재계산된다() {
        // Given: derivedStateOf로 계산된 파생 상태
        var calculationCount = 0

        composeTestRule.setContent {
            var sourceValue by remember { mutableStateOf(10) }
            val derived by remember {
                derivedStateOf {
                    calculationCount++
                    sourceValue * 2
                }
            }

            Text(
                text = "Result: $derived",
                modifier = Modifier.testTag("result")
            )
            Button(
                onClick = { sourceValue += 5 },
                modifier = Modifier.testTag("incrementButton")
            ) {
                Text("Increment")
            }
        }

        composeTestRule.waitForIdle()
        val initialCount = calculationCount

        // When: 원본 State를 변경
        composeTestRule.onNodeWithTag("incrementButton").performClick()
        composeTestRule.waitForIdle()

        // Then: 파생 상태가 재계산됨
        assert(calculationCount == initialCount + 1) {
            "계산 횟수가 증가해야 함: expected ${initialCount + 1}, actual $calculationCount"
        }
        composeTestRule.onNodeWithTag("result")
            .assertTextEquals("Result: 30") // (10+5) * 2
    }

    @Test
    fun 무관한_State_변경_시_파생_상태는_재계산되지_않는다() {
        // Given: derivedStateOf와 무관한 상태
        var derivedCalculationCount = 0

        composeTestRule.setContent {
            var relevantState by remember { mutableStateOf("Hello") }
            var irrelevantState by remember { mutableStateOf(0) }
            val derived by remember {
                derivedStateOf {
                    derivedCalculationCount++
                    relevantState.uppercase()
                }
            }

            Text(
                text = derived,
                modifier = Modifier.testTag("derivedText")
            )
            Text(
                text = "Count: $irrelevantState",
                modifier = Modifier.testTag("countText")
            )
            Button(
                onClick = { irrelevantState++ },
                modifier = Modifier.testTag("irrelevantButton")
            ) {
                Text("Increment Irrelevant")
            }
        }

        composeTestRule.waitForIdle()
        val beforeCount = derivedCalculationCount

        // When: 파생 상태가 읽지 않는 State를 변경
        composeTestRule.onNodeWithTag("irrelevantButton").performClick()
        composeTestRule.waitForIdle()

        // Then: 파생 상태는 재계산되지 않음 (캐싱)
        assert(derivedCalculationCount == beforeCount) {
            "derivedStateOf는 재계산되지 않아야 함: expected $beforeCount, actual $derivedCalculationCount"
        }
        composeTestRule.onNodeWithTag("derivedText")
            .assertTextEquals("HELLO")
        composeTestRule.onNodeWithTag("countText")
            .assertTextEquals("Count: 1")
    }

    @Test
    fun derivedStateOf가_읽는_State_변경_시_파생_상태가_재계산된다() {
        // Given: 여러 State 중 일부만 derivedStateOf에서 사용
        var derivedCalculationCount = 0

        composeTestRule.setContent {
            var firstName by remember { mutableStateOf("John") }
            var lastName by remember { mutableStateOf("Doe") }
            var age by remember { mutableStateOf(30) }

            val fullName by remember {
                derivedStateOf {
                    derivedCalculationCount++
                    "$firstName $lastName"
                }
            }

            Text(
                text = fullName,
                modifier = Modifier.testTag("fullName")
            )
            Text(
                text = "Age: $age",
                modifier = Modifier.testTag("age")
            )
            Button(
                onClick = { firstName = "Jane" },
                modifier = Modifier.testTag("changeFirstName")
            ) {
                Text("Change First Name")
            }
        }

        composeTestRule.waitForIdle()
        val beforeCount = derivedCalculationCount

        // When: derivedStateOf가 읽는 State를 변경
        composeTestRule.onNodeWithTag("changeFirstName").performClick()
        composeTestRule.waitForIdle()

        // Then: 파생 상태가 재계산됨
        assert(derivedCalculationCount == beforeCount + 1) {
            "firstName 변경 시 derivedStateOf 재계산: expected ${beforeCount + 1}, actual $derivedCalculationCount"
        }
        composeTestRule.onNodeWithTag("fullName")
            .assertTextEquals("Jane Doe")
    }

    @Test
    fun derivedStateOf가_읽지_않는_State_변경_시_재계산되지_않는다() {
        // Given: 여러 State 중 일부만 derivedStateOf에서 사용
        var derivedCalculationCount = 0

        composeTestRule.setContent {
            var firstName by remember { mutableStateOf("John") }
            var lastName by remember { mutableStateOf("Doe") }
            var age by remember { mutableStateOf(30) }

            val fullName by remember {
                derivedStateOf {
                    derivedCalculationCount++
                    "$firstName $lastName"
                }
            }

            Text(
                text = fullName,
                modifier = Modifier.testTag("fullName")
            )
            Text(
                text = "Age: $age",
                modifier = Modifier.testTag("age")
            )
            Button(
                onClick = { age++ },
                modifier = Modifier.testTag("changeAge")
            ) {
                Text("Change Age")
            }
        }

        composeTestRule.waitForIdle()
        val beforeCount = derivedCalculationCount

        // When: derivedStateOf가 읽지 않는 State를 변경
        composeTestRule.onNodeWithTag("changeAge").performClick()
        composeTestRule.waitForIdle()

        // Then: 파생 상태는 재계산되지 않음
        assert(derivedCalculationCount == beforeCount) {
            "age 변경 시 derivedStateOf 재계산 안됨: expected $beforeCount, actual $derivedCalculationCount"
        }
        composeTestRule.onNodeWithTag("fullName")
            .assertTextEquals("John Doe") // 이전 값 유지
        composeTestRule.onNodeWithTag("age")
            .assertTextEquals("Age: 31")
    }

    @Test
    fun 필터_조건에_맞는_항목_추가_시_파생_상태가_재계산되고_개수가_증가한다() {
        // Given: derivedStateOf로 HIGH priority 항목 필터링
        data class Task(val id: Int, val name: String, val priority: String)

        var filterCalculationCount = 0

        composeTestRule.setContent {
            val tasks = remember {
                mutableStateListOf(
                    Task(1, "Task A", "HIGH"),
                    Task(2, "Task B", "LOW"),
                    Task(3, "Task C", "HIGH"),
                    Task(4, "Task D", "MEDIUM")
                )
            }

            val highPriorityTasks by remember {
                derivedStateOf {
                    filterCalculationCount++
                    tasks.filter { it.priority == "HIGH" }
                }
            }

            Text(
                text = "High Priority: ${highPriorityTasks.size}",
                modifier = Modifier.testTag("highPriorityCount")
            )
            Button(
                onClick = { tasks.add(Task(5, "Task E", "HIGH")) },
                modifier = Modifier.testTag("addHighPriority")
            ) {
                Text("Add High Priority")
            }
        }

        composeTestRule.waitForIdle()
        val beforeCount = filterCalculationCount

        // When: HIGH priority 항목을 추가
        composeTestRule.onNodeWithTag("addHighPriority").performClick()
        composeTestRule.waitForIdle()

        // Then: 필터링이 재계산되어 개수가 증가
        assert(filterCalculationCount == beforeCount + 1) {
            "리스트 변경 시 필터링 재계산: expected ${beforeCount + 1}, actual $filterCalculationCount"
        }
        composeTestRule.onNodeWithTag("highPriorityCount")
            .assertTextEquals("High Priority: 3")
    }

    @Test
    fun 필터_조건에_안_맞는_항목_추가_시_재계산되지만_결과는_동일하다() {
        // Given: derivedStateOf로 HIGH priority 항목 필터링
        data class Task(val id: Int, val name: String, val priority: String)

        var filterCalculationCount = 0

        composeTestRule.setContent {
            val tasks = remember {
                mutableStateListOf(
                    Task(1, "Task A", "HIGH"),
                    Task(2, "Task B", "LOW"),
                    Task(3, "Task C", "HIGH"),
                    Task(4, "Task D", "MEDIUM")
                )
            }

            val highPriorityTasks by remember {
                derivedStateOf {
                    filterCalculationCount++
                    tasks.filter { it.priority == "HIGH" }
                }
            }

            Text(
                text = "High Priority: ${highPriorityTasks.size}",
                modifier = Modifier.testTag("highPriorityCount")
            )
            Button(
                onClick = { tasks.add(Task(6, "Task F", "LOW")) },
                modifier = Modifier.testTag("addLowPriority")
            ) {
                Text("Add Low Priority")
            }
        }

        composeTestRule.waitForIdle()
        val beforeCount = filterCalculationCount

        // When: LOW priority 항목을 추가
        composeTestRule.onNodeWithTag("addLowPriority").performClick()
        composeTestRule.waitForIdle()

        // Then: 필터링은 재계산되지만 HIGH priority 개수는 동일
        assert(filterCalculationCount == beforeCount + 1) {
            "리스트 변경 시 재계산됨: expected ${beforeCount + 1}, actual $filterCalculationCount"
        }
        composeTestRule.onNodeWithTag("highPriorityCount")
            .assertTextEquals("High Priority: 2")
    }

    @Test
    fun remember는_key_변경_시_재계산되지만_derivedStateOf는_읽은_값만_추적한다() {
        // Given: remember와 derivedStateOf로 같은 데이터 계산
        var rememberCalculationCount = 0
        var derivedCalculationCount = 0

        composeTestRule.setContent {
            var firstName by remember { mutableStateOf("John") }
            var lastName by remember { mutableStateOf("Doe") }
            var age by remember { mutableStateOf(30) }

            // remember: 모든 key를 명시적으로 지정
            val fullNameWithRemember = remember(firstName, lastName, age) {
                rememberCalculationCount++
                "$firstName $lastName (${age}세)"
            }

            // derivedStateOf: 실제로 읽은 것만 자동 추적
            val fullNameWithDerived by remember {
                derivedStateOf {
                    derivedCalculationCount++
                    "$firstName $lastName"
                }
            }

            Text(
                text = "Remember: $fullNameWithRemember",
                modifier = Modifier.testTag("remember")
            )
            Text(
                text = "Derived: $fullNameWithDerived",
                modifier = Modifier.testTag("derived")
            )
            Button(
                onClick = { age++ },
                modifier = Modifier.testTag("incrementAge")
            ) {
                Text("Increment Age")
            }
        }

        composeTestRule.waitForIdle()
        val rememberBefore = rememberCalculationCount
        val derivedBefore = derivedCalculationCount

        // When: age를 변경
        composeTestRule.onNodeWithTag("incrementAge").performClick()
        composeTestRule.waitForIdle()

        // Then: remember는 재계산되지만 derivedStateOf는 재계산되지 않음
        assert(rememberCalculationCount == rememberBefore + 1) {
            "remember는 age 변경 시 재계산: expected ${rememberBefore + 1}, actual $rememberCalculationCount"
        }
        assert(derivedCalculationCount == derivedBefore) {
            "derivedStateOf는 age를 읽지 않아 재계산 안됨: expected $derivedBefore, actual $derivedCalculationCount"
        }

        composeTestRule.onNodeWithTag("remember")
            .assertTextEquals("Remember: John Doe (31세)")
        composeTestRule.onNodeWithTag("derived")
            .assertTextEquals("Derived: John Doe")
    }
}
