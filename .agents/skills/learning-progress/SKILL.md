---
name: learning-progress
description: "Tracks and manages learning progress for Kotlin, Coroutines, Flow, Compose, and Android topics. Creates and updates LEARNING_PROGRESS.md with checkboxes, completion dates, and personalized recommendations for next learning topics based on completed items."
---

# Learning Progress Tracker

## Overview
This skill helps you track your learning journey across Kotlin, Coroutines, Flow, Compose, and Android development. It maintains a `LEARNING_PROGRESS.md` file with structured checklists, completion dates, and provides intelligent recommendations for what to learn next.

## Scope
This skill handles **learning progress management**:
- **Initialize Progress**: Create `LEARNING_PROGRESS.md` with comprehensive checklists
- **Update Progress**: Mark topics as completed with dates
- **View Progress**: Show current learning status and statistics
- **Recommendations**: Suggest next topics based on completed items and dependencies

## Progress Tracking Strategy

### 1. Initialize Progress File
When first requested, create `LEARNING_PROGRESS.md` with:
- Categorized checklists (Kotlin, Coroutines, Flow, Compose, Android)
- Difficulty levels (기초, 중급, 고급)
- Dependencies between topics
- Learning path recommendations

### 2. Update Completed Topics
When user reports completion:
- Find the topic in the checklist
- Mark with ✅ and add completion date
- Update statistics
- Suggest next related topics

### 3. Progress Visualization
Show learning statistics:
- Completion percentage per category
- Total items completed
- Recent completions
- Suggested next steps

### 4. Smart Recommendations
Based on completed topics, suggest:
- Next logical topic in learning path
- Related topics to deepen understanding
- Advanced topics when fundamentals are complete

## Output Format

### LEARNING_PROGRESS.md Structure

```markdown
# Kotlin & Android Learning Progress

## 📊 Overview
- **Total Completed**: 15 / 50 (30%)
- **Last Updated**: 2025-01-24
- **Current Focus**: Coroutines 기초

## 📚 Learning Categories

### 🔷 Kotlin 기초 (5 / 12)
- [x] Collections 기본 연산 ✅ 2025-01-15
- [x] Lambda & 고차함수 ✅ 2025-01-16
- [ ] Extension Functions
- [ ] Data Classes & Sealed Classes
- [ ] Delegation Pattern

### ⚡ Coroutines (3 / 10)
- [x] launch vs async ✅ 2025-01-20
- [x] Dispatchers ✅ 2025-01-21
- [ ] Structured Concurrency
- [ ] Exception Handling
- [ ] Cancellation

### 🌊 Flow (0 / 8)
- [ ] Flow 기초
- [ ] Flow Operators
- [ ] StateFlow vs SharedFlow

### 🎨 Compose (0 / 12)
- [ ] Composable Functions
- [ ] State & Recomposition
- [ ] Side Effects

### 📱 Android (2 / 8)
- [x] Activity Lifecycle ✅ 2025-01-18
- [x] ViewModel ✅ 2025-01-19
- [ ] LiveData
- [ ] Navigation

## 🎯 Recommended Next Steps
Based on your progress:
1. **Coroutines - Structured Concurrency** (prerequisite: launch/async ✅)
2. **Flow 기초** (prerequisite: Coroutines 기초 ✅)
3. **Extension Functions** (standalone topic)
```

## Usage Instructions

### When This Skill Activates
The skill automatically activates when you say:
- "학습 진도 보여줘"
- "코루틴 기초 완료했어"
- "학습 진도 초기화해줘"
- "다음 뭐 공부하면 좋을까?"
- Any learning progress tracking request

### Initialize Progress
**User**: "학습 진도 파일 만들어줘"

**Skill Output**:
1. Creates `LEARNING_PROGRESS.md` in project root
2. Populates with comprehensive checklists
3. Shows initial statistics (0% completed)

### Mark Topic Complete
**User**: "코루틴 launch vs async 학습 완료했어"

**Skill Output**:
1. Finds "launch vs async" in checklist
2. Marks with ✅ and today's date
3. Updates statistics
4. Suggests next topic: "Structured Concurrency를 학습하시면 좋겠습니다"

### View Progress
**User**: "학습 진도 어떻게 돼?"

**Skill Output**:
```
📊 Current Learning Progress

Kotlin 기초: ████░░░░░░ 40% (5/12)
Coroutines:  ███░░░░░░░ 30% (3/10)
Flow:        ░░░░░░░░░░ 0% (0/8)
Compose:     ░░░░░░░░░░ 0% (0/12)
Android:     ██░░░░░░░░ 25% (2/8)

Overall: ███░░░░░░░ 30% (15/50)

최근 완료: Coroutines - Dispatchers (2025-01-21)

추천 학습:
1. Coroutines - Structured Concurrency
2. Flow 기초
```

### Get Recommendations
**User**: "다음 뭐 공부하면 좋을까?"

**Skill Output**:
```
현재 진도를 기반으로 추천합니다:

우선순위 1: Coroutines - Structured Concurrency
- 이유: launch/async를 이미 학습했으므로 다음 단계입니다
- 난이도: 중급
- 예상 시간: 2-3일

우선순위 2: Flow 기초
- 이유: Coroutines 기초가 완료되어 Flow를 시작할 수 있습니다
- 난이도: 중급
- 예상 시간: 3-4일

우선순위 3: Extension Functions
- 이유: 독립 주제로 언제든 학습 가능합니다
- 난이도: 기초
- 예상 시간: 1일
```

## Best Practices

### 1. Manual Completion Only
Progress is updated only when you explicitly state completion:
```
✅ "코루틴 기초 완료했어"
❌ Automatic tracking (not implemented)
```

### 2. Accurate Topic Names
Use topic names matching the checklist:
```
✅ "launch vs async 완료"
❌ "코루틴 launch 완료" (too vague)
```

### 3. Partial Completion
For large topics, break into subtopics:
```markdown
### Coroutines (5 / 10)
- [x] launch ✅ 2025-01-20
- [x] async ✅ 2025-01-20
- [ ] Job
- [ ] Dispatcher
```

### 4. Regular Reviews
Periodically review your progress:
```
User: "이번 주 학습 진도 보여줘"
```

### 5. Follow Recommendations
Trust the dependency-based recommendations for optimal learning path.

## Progress File Management

### File Location
`LEARNING_PROGRESS.md` in project root:
```
Kotlin-Android-Learning-Test/
├── LEARNING_PROGRESS.md  ← Here
├── .Codex/
└── app/
```

### File Format
Markdown with checkboxes:
```markdown
- [x] Completed Topic ✅ 2025-01-20
- [ ] Pending Topic
```

### Statistics Calculation
Automatically counted:
- Per-category completion (e.g., Kotlin: 5/12)
- Overall completion (e.g., 15/50)
- Completion percentage

### Date Format
ISO format: `YYYY-MM-DD` (e.g., 2025-01-24)

## Learning Path Dependencies

### Kotlin Foundation Required
Before Coroutines:
- Collections ✅
- Lambda & Higher-Order Functions ✅

### Coroutines Required
Before Flow:
- Coroutines Basics (launch, async) ✅
- Dispatchers ✅

### Flow + Coroutines Required
Before Compose State Management:
- Flow 기초 ✅
- StateFlow ✅

### Kotlin + Android Required
Before Architecture:
- Kotlin 기초 ✅
- Activity/Fragment Lifecycle ✅

## Troubleshooting

### If LEARNING_PROGRESS.md Doesn't Exist
Say: "학습 진도 파일 만들어줘"

### If Topic Not Found
The skill will:
1. Search for closest match
2. Ask for clarification if ambiguous
3. List available topics if no match

### If Statistics Wrong
The skill recalculates on every update - should auto-correct.

### If Recommendations Seem Off
Recommendations are based on:
- Completed prerequisites
- Difficulty progression
- Topic dependencies

You can always override and learn in any order you prefer.

## Related Skills
- **kotlin-learning**: Create Kotlin learning tests
- **compose-learning**: Create Compose learning tests
- **android-learning**: Create Android learning tests

After completing a topic, use those skills to create practice tests!

## Example Workflow

**Day 1**:
```
User: "학습 진도 초기화해줘"
→ LEARNING_PROGRESS.md created with full checklist
```

**Day 5**:
```
User: "코루틴 launch vs async 완료했어"
→ ✅ marked, date added, statistics updated
→ Recommendation: "다음은 Structured Concurrency를 학습하세요"
```

**Day 10**:
```
User: "학습 진도 보여줘"
→ Shows visual progress bars and statistics
→ Highlights recent completions
```

**Day 15**:
```
User: "다음 뭐 공부할까?"
→ Smart recommendations based on prerequisites
→ Suggests optimal learning path
```

This creates a structured, motivating learning experience with clear goals and progress tracking!