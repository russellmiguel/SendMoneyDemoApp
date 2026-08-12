package com.robertrussell.miguel.sendmoneydemoapp.data.repository

import com.robertrussell.miguel.sendmoneydemoapp.data.local.UserDao
import com.robertrussell.miguel.sendmoneydemoapp.data.local.UserEntity
import com.robertrussell.miguel.sendmoneydemoapp.domain.security.PasswordHasher
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private lateinit var authRepository: AuthRepositoryImpl
    private val userDao: UserDao = mockk()
    private val passwordHasher: PasswordHasher = mockk()

    @Before
    fun setUp() {
        authRepository = AuthRepositoryImpl(userDao, passwordHasher)
    }

    @Test
    fun `signUp should hash password and insert user`() = runBlocking {
        val name = "John"
        val email = "john@example.com"
        val pass = "password"
        val hash = "hashed_password"

        every { passwordHasher.hash(pass) } returns hash
        coEvery { userDao.insertUser(any()) } returns Unit

        val result = authRepository.signUp(name, email, pass)

        assertTrue(result.isSuccess)
        coVerify { userDao.insertUser(match { it.email == email && it.passwordHash == hash }) }
    }

    @Test
    fun `login success with correct credentials`() = runBlocking {
        val email = "john@example.com"
        val pass = "password"
        val hash = "hashed_password"
        val userEntity = UserEntity(email, "John", hash, 100.0)

        coEvery { userDao.getUserByEmail(email) } returns userEntity
        every { passwordHasher.verify(pass, hash) } returns true

        val result = authRepository.login(email, pass)

        assertTrue(result.isSuccess)
        assertEquals("John", result.getOrThrow().name)
    }

    @Test
    fun `login failure with wrong password`() = runBlocking {
        val email = "john@example.com"
        val pass = "wrong"
        val hash = "hashed_password"
        val userEntity = UserEntity(email, "John", hash, 100.0)

        coEvery { userDao.getUserByEmail(email) } returns userEntity
        every { passwordHasher.verify(pass, hash) } returns false

        val result = authRepository.login(email, pass)

        assertTrue(result.isFailure)
        assertEquals("Invalid credentials", result.exceptionOrNull()?.message)
    }

    @Test
    fun `observeBalance should map userDao flow`() = runBlocking {
        val email = "john@example.com"
        every { userDao.observeUserBalance(email) } returns flowOf(150.0)

        val balance = authRepository.observeBalance(email).first()

        assertEquals(150.0, balance, 0.0)
    }
}
