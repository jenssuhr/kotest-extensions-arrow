@file:Suppress("UNCHECKED_CAST")

package io.kotest.assertions.arrow

import io.kotest.assertions.assertionCounter
import io.kotest.assertions.collectOrThrow
import io.kotest.assertions.eq.eq
import io.kotest.assertions.errorCollector
import io.kotest.matchers.Matcher
import io.kotest.matchers.equalityMatcher
import io.kotest.matchers.invokeMatcher

internal infix fun <A> A.shouldBe(a: A): A =
  when (a) {
    is Matcher<*> -> invokeMatcher(this, a as Matcher<A>)
    else -> {
      val actual = this
      assertionCounter.inc()
      eq(actual, a)?.let(errorCollector::collectOrThrow)
      actual
    }
  }

internal infix fun <A> A.shouldNotBe(a: A?): A =
  when (a) {
    is Matcher<*> -> invokeMatcher(this, (a as Matcher<A>).invert())
    else -> {
      invokeMatcher(this, equalityMatcher(a).invert())
      this
    }
  }