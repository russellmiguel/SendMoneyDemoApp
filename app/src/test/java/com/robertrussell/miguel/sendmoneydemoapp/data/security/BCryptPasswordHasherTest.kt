package com.robertrussell.miguel.sendmoneydemoapp.data.security

import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BCryptPasswordHasherTest {

    private val hasher = BCryptPasswordHasher()

    @Test
    fun `hash should return a different string`() {
        val password = "password123"
        val hash = hasher.hash(password)
        assertNotEquals(password, hash)
    }

    @Test
    fun `verify should return true for correct password`() {
        val password = "password123"
        val hash = hasher.hash(password)
        assertTrue(hasher.verify(password, hash))
    }
}
