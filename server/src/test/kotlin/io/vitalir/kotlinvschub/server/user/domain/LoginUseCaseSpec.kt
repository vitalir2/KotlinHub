package io.vitalir.kotlinvschub.server.user.domain

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.ints.shouldNotBeExactly

class LoginUseCaseSpec : ShouldSpec({
    should("1 be equal to 1") {
        1 shouldBeExactly 1
    }
    should("1 not be equal to 2") {
        1 shouldNotBeExactly 2
    }
    should("fail") {
        1 shouldBeExactly 2
    }
})
