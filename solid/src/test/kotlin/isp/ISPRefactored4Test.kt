package isp

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import solid.isp.ISPRefactored4.ChatbotLottoVendingMachine
import solid.isp.ISPRefactored4.DefaultChatResetCapable
import solid.isp.ISPRefactored4.DisCountLottoSeller
import solid.isp.ISPRefactored4.HelloChat
import solid.isp.ISPRefactored4.MorningChat
import solid.isp.ISPRefactored4.NoisyLottoVendingMachine
import solid.isp.ISPRefactored4.NoisyReset
import solid.isp.ISPRefactored4.NormalLottoSeller
import solid.isp.ISPRefactored4.NormalLottoVendingMachine
import solid.isp.ISPRefactored4.QuietReset

class ISPRefactored4Test : BehaviorSpec({

    Given("ChatCapable 인터페이스를 구현한 MorningChat") {
        val morningChat = MorningChat()

        When("onRest()를 호출하면") {
            val result = morningChat.onRest()

            Then("Chatting -- Good morning! 이 출력된다") {
                result shouldBe "Chatting -- Good morning!"
            }
        }
    }

    Given("ChatCapable 인터페이스를 구현한 HelloChat") {
        val helloChat = HelloChat()

        When("onRest()를 호출하면") {
            val result = helloChat.onRest()

            Then("Chatting -- Hello! 이 출력된다") {
                result shouldBe "Chatting -- Hello!"
            }
        }
    }

    Given("ResetCapable 인터페이스를 구현한 QuietReset") {
        val quietReset = QuietReset()

        When("onRest()를 호출하면") {
            val result = quietReset.onRest()

            Then("Resetting -- Quietly 가 출력된다") {
                result shouldBe "Resetting -- Quietly"
            }
        }
    }

    Given("ResetCapable 인터페이스를 구현한 NoisyReset") {
        val noisyReset = NoisyReset()

        When("onRest()를 호출하면") {
            val result = noisyReset.onRest()

            Then("Resetting -- With noise 가 출력된다") {
                result shouldBe "Resetting -- With noise"
            }
        }
    }

    Given("ChatResetCapable 인터페이스를 구현한 DefaultChatResetCapable") {
        val chatbot = DefaultChatResetCapable()

        When("onRest()를 호출하면") {
            val result = chatbot.onRest()

            Then("Chatting -- Hello, You can call me chatbot! \nResetting -- Quietly 가 출력된다") {
                result shouldBe "Chatting -- Hello, You can call me chatbot!\nResetting -- Quietly"
            }
        }
    }

    Given("NormalLottoSeller") {
        val normalLottoSeller = NormalLottoSeller()

        When("onRest()를 호출하면") {
            val result = normalLottoSeller.onRest()

            Then("Chatting -- Hello! 가 출력된다") {
                result shouldBe "Chatting -- Hello!"
            }
        }
    }

    Given("DisCountLottoSeller") {
        val discountLottoSeller = DisCountLottoSeller()

        When("onRest()를 호출하면") {
            val result = discountLottoSeller.onRest()

            Then("Chatting -- Good morning! 가 출력된다") {
                result shouldBe "Chatting -- Good morning!"
            }
        }
    }

    Given("NormalLottoVendingMachine") {
        val normalLottoVendingMachine = NormalLottoVendingMachine()

        When("onRest()를 호출하면") {
            val result = normalLottoVendingMachine.onRest()

            Then("Resetting -- Quietly 가 출력된다") {
                result shouldBe "Resetting -- Quietly"
            }
        }
    }

    Given("NoisyLottoVendingMachine") {
        val noisyLottoVendingMachine = NoisyLottoVendingMachine()

        When("onRest()를 호출하면") {
            val result = noisyLottoVendingMachine.onRest()

            Then("Resetting -- With noise 가 출력된다") {
                result shouldBe "Resetting -- With noise"
            }
        }
    }

    Given("ChatbotLottoVendingMachine") {
        val chatbotLottoVendingMachine = ChatbotLottoVendingMachine()

        When("onRest()를 호출하면") {
            val result = chatbotLottoVendingMachine.onRest()

            Then("Chatting -- Hello, You can call me chatbot! \nResetting -- Quietly 가 출력된다") {
                result shouldBe "Chatting -- Hello, You can call me chatbot!\nResetting -- Quietly"
            }
        }
    }
})
