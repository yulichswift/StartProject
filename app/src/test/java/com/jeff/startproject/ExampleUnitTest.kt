package com.jeff.startproject

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun addition_double() {
        assertEquals(0.3, 0.2 + 0.1, 0.0)
    }

    @Test
    fun double_3_toString() {
        assertEquals(0.009.toBigDecimal().toString(), 0.009.toString())
    }

    @Test
    fun double_4_toString() {
        assertEquals(0.0009.toBigDecimal().toString(), 0.0009.toString())
    }
}
