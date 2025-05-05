import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class GroupingTest : FreeSpec({
    data class Person(val name: String, val city: String, val age: Int)

    val people =
        listOf(
            Person("Alice", "London", 30),
            Person("Bob", "Paris", 25),
            Person("Charlie", "London", 35),
            Person("David", "Paris", 30),
            Person("Eve", "London", 25),
        )

    "List Grouping" - {
        "groupBy - 키를 기준으로 요소들을 그룹화. 리턴 타입은 Map<T, List<V>>" {
            val groupedByCity: Map<String, List<Person>> = people.groupBy(Person::city)

            groupedByCity shouldBe
                mapOf<String, List<Person>>(
                    "London" to
                        listOf(
                            Person("Alice", "London", 30),
                            Person("Charlie", "London", 35),
                            Person("Eve", "London", 25),
                        ),
                    "Paris" to
                        listOf(
                            Person("Bob", "Paris", 25),
                            Person("David", "Paris", 30),
                        ),
                )
        }

        "groupBy - 키와 값 변환을 함께 적용하여 그룹화" {
            val groupedCityNames: Map<String, List<String>> =
                people.groupBy(
                    keySelector = (Person::city),
                    valueTransform = { it.name + 1 },
                )

            groupedCityNames shouldBe
                mapOf<String, List<String>>(
                    "London" to listOf("Alice1", "Charlie1", "Eve1"),
                    "Paris" to listOf("Bob1", "David1"),
                )
        }

        "groupingBy - eachCount() 로 각 그룹의 요소 개수를 계산" {
            // 도시별 인구 수 map
            val groupingByCity: Grouping<Person, String> = people.groupingBy(Person::city)
            val cityWithPeopleCount = groupingByCity.eachCount()

            cityWithPeopleCount shouldBe
                mapOf<String, Int>(
                    "London" to 3,
                    "Paris" to 2,
                )
        }

        "groupingBy - fold() 로 각 그룹 내에서 누적 연산 수행" {
            // 도시별 인구 나이의 합 map
            val groupingByCity: Grouping<Person, String> = people.groupingBy(Person::city)
            val cityWithPeopleAgeSum: Map<String, Int> =
                groupingByCity.fold(0) { acc, person ->
                    acc + person.age
                }

            cityWithPeopleAgeSum shouldBe
                mapOf<String, Int>(
                    "London" to 30 + 35 + 25,
                    "Paris" to 25 + 30,
                )
        }

        "groupingBy - reduce() 로 각 그룹 내에서 누적 연산 수행 (빈 그룹에는 사용 불가)" {
            val animals = listOf("raccoon", "reindeer", "cow", "camel", "giraffe", "goat")
            // 모음을 가장 많이 포함한 문자열만 수집
            val groupingByFirstChar: Grouping<String, Char> = animals.groupingBy(String::first)

            val comparator = compareBy { str: String -> str.count { it in "aeiou" } }

            val firstCharWithMaxVowelsAnimal: Map<Char, String> =
                groupingByFirstChar.reduce { key: Char, acc: String, animal: String ->
                    maxOf(acc, animal, comparator)
                }
            firstCharWithMaxVowelsAnimal shouldBe
                mapOf<Char, String>(
                    'r' to "reindeer",
                    'c' to "camel",
                    'g' to "giraffe",
                )
        }

        "groupingBy - aggregate() 로 더 복잡한 그룹별 집계 수행" {
            val numbers = listOf(3, 4, 5, 6, 7, 8, 9)

            val groupedElements: Map<Int, String> =
                numbers
                    .groupingBy { it % 3 }
                    .aggregate { key: Int, accumulator: StringBuilder?, element: Int, first ->
                        if (first) {
                            StringBuilder().append(key).append("-").append(element)
                        } else {
                            accumulator!!.append("-").append(element)
                        }
                    }.mapValues { (_, sb) -> sb.toString() }

            groupedElements shouldBe
                mapOf<Int, String>(
                    0 to "0-3-6-9",
                    1 to "1-4-7",
                    2 to "2-5-8",
                )
        }
    }
})
