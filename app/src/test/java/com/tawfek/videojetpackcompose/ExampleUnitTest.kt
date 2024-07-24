package com.tawfek.videojetpackcompose

import com.tawfek.videojetpackcompose.domain.use_cases.FormatDurationUseCase
import com.tawfek.videojetpackcompose.domain.use_cases.FormatViewsCountUseCase
import org.junit.Test

import org.junit.Assert.*

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
    fun convert_DurationFromSeconds_isCorrect() {
        val duration = 9

        val result = FormatDurationUseCase(duration)

        println("result = $result")
    }

    @Test
    fun format_Views_isCorrect(){
        val views = 121

        val result = FormatViewsCountUseCase(views)
        println("result = $result")
    }
}