package com.robertrussell.miguel.sendmoneydemoapp.util

import org.junit.Assert.assertEquals
import org.junit.Test

class AppUtilsTest {

    @Test
    fun `formatNumber formats with commas`() {
        assertEquals("1,000", formatNumber(1000))
        assertEquals("1,234,567.89", formatNumber(1234567.89))
    }

    @Test
    fun `maskNumbers replaces digits with asterisks`() {
        assertEquals("***-***-****", "123-456-7890".maskNumbers())
        assertEquals("No digits", "No digits".maskNumbers())
    }
}
