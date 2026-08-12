package com.robertrussell.miguel.sendmoneydemoapp.data.repository

import com.robertrussell.miguel.sendmoneydemoapp.data.local.TransactionDao
import com.robertrussell.miguel.sendmoneydemoapp.data.local.UserDao
import com.robertrussell.miguel.sendmoneydemoapp.data.remote.JsonPlaceholderApi
import com.robertrussell.miguel.sendmoneydemoapp.data.remote.RemoteTransaction
import com.robertrussell.miguel.sendmoneydemoapp.data.remote.TransactionResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class TransactionRepositoryImplTest {

    private lateinit var transactionRepository: TransactionRepositoryImpl
    private val transactionDao: TransactionDao = mockk()
    private val userDao: UserDao = mockk()
    private val api: JsonPlaceholderApi = mockk()

    @Before
    fun setUp() {
        transactionRepository = TransactionRepositoryImpl(transactionDao, userDao, api)
    }

    @Test
    fun `getTransactions returns transactions from DAO`() = runBlocking {
        val email = "test@example.com"
        every { transactionDao.getTransactionsForUser(email) } returns flowOf(emptyList())

        val result = transactionRepository.getTransactions(email).first()

        assertTrue(result.isEmpty())
        coVerify { transactionDao.getTransactionsForUser(email) }
    }

    @Test
    fun `getRemoteTransactions success returns remote data`() = runBlocking {
        val remoteList = listOf(
            RemoteTransaction(1, 100.0, "Remote Recipient", 12345L, "SEND")
        )
        coEvery { api.getRemoteTransactions() } returns Response.success(remoteList)

        val result = transactionRepository.getRemoteTransactions()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
        assertEquals("Remote Recipient", result.getOrThrow()[0].recipient)
    }

    @Test
    fun `sendMoney success updates balance and adds transaction locally`() = runBlocking {
        val email = "test@example.com"
        val amount = 50.0
        val recipient = "Someone"
        
        coEvery { api.sendMoney(any()) } returns Response.success(TransactionResponse(1, "Title", "Body", 1))
        coEvery { userDao.updateBalance(email, -amount) } returns Unit
        coEvery { transactionDao.insertTransaction(any()) } returns Unit

        val result = transactionRepository.sendMoney(email, amount, recipient)

        assertTrue(result.isSuccess)
        coVerify { userDao.updateBalance(email, -amount) }
        coVerify { transactionDao.insertTransaction(match { it.userEmail == email && it.amount == amount && it.type == "SEND" }) }
    }

    @Test
    fun `addBalance success updates balance and adds transaction locally`() = runBlocking {
        val email = "test@example.com"
        val amount = 100.0
        
        coEvery { api.addBalance(any()) } returns Response.success(TransactionResponse(1, "Title", "Body", 1))
        coEvery { userDao.updateBalance(email, amount) } returns Unit
        coEvery { transactionDao.insertTransaction(any()) } returns Unit

        val result = transactionRepository.addBalance(email, amount)

        assertTrue(result.isSuccess)
        coVerify { userDao.updateBalance(email, amount) }
        coVerify { transactionDao.insertTransaction(match { it.userEmail == email && it.amount == amount && it.type == "DEPOSIT" }) }
    }
}
