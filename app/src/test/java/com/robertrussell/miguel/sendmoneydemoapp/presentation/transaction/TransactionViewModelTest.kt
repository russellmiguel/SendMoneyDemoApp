package com.robertrussell.miguel.sendmoneydemoapp.presentation.transaction

import androidx.lifecycle.SavedStateHandle
import com.robertrussell.miguel.sendmoneydemoapp.domain.model.Transaction
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.GetTransactionsUseCase
import com.robertrussell.miguel.sendmoneydemoapp.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getTransactionsUseCase: GetTransactionsUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(mapOf("userEmail" to "test@example.com"))

    @Test
    fun `init should load transactions`() = runTest {
        val transactions = listOf(
            Transaction(1, 100.0, "Recipient", 1000L, "SEND")
        )
        every { getTransactionsUseCase("test@example.com") } returns flowOf(transactions)
        coEvery { getTransactionsUseCase.getRemoteTransactions() } returns Result.success(emptyList())

        val viewModel = TransactionViewModel(getTransactionsUseCase, savedStateHandle)

        assertEquals(transactions, viewModel.transactions)
    }

    @Test
    fun `loadRemoteTransactions should update transactions state`() = runTest {
        val localTransactions = listOf(
            Transaction(1, 100.0, "Recipient", 1000L, "SEND")
        )
        val remoteTransactions = listOf(
            Transaction(2, 200.0, "Remote", 2000L, "RECEIVE")
        )
        
        every { getTransactionsUseCase("test@example.com") } returns flowOf(localTransactions)
        coEvery { getTransactionsUseCase.getRemoteTransactions() } returns Result.success(remoteTransactions)

        val viewModel = TransactionViewModel(getTransactionsUseCase, savedStateHandle)
        
        // At this point init already called loadRemoteTransactions because local wasn't empty but remote was empty
        // Wait, the logic in init:
        // if (remoteTransactions.isEmpty() && !isProcessing) { loadRemoteTransactions() }
        
        assertTrue(viewModel.transactions.any { it.id == 2 })
        assertEquals(2, viewModel.transactions.size)
        assertEquals(2000L, viewModel.transactions[0].date) // Sorted by date descending
    }

    @Test
    fun `loadRemoteTransactions failure should not crash`() = runTest {
        val localTransactions = listOf(
            Transaction(1, 100.0, "Recipient", 1000L, "SEND")
        )
        every { getTransactionsUseCase("test@example.com") } returns flowOf(localTransactions)
        coEvery { getTransactionsUseCase.getRemoteTransactions() } returns Result.failure(Exception("Error"))

        val viewModel = TransactionViewModel(getTransactionsUseCase, savedStateHandle)

        assertEquals(localTransactions, viewModel.transactions)
    }
}
