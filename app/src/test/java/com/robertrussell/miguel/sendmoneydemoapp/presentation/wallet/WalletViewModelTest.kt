package com.robertrussell.miguel.sendmoneydemoapp.presentation.wallet

import androidx.lifecycle.SavedStateHandle
import com.robertrussell.miguel.sendmoneydemoapp.domain.model.User
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.AuthRepository
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.AddBalanceUseCase
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.GetBalanceUseCase
import com.robertrussell.miguel.sendmoneydemoapp.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class WalletViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getBalanceUseCase: GetBalanceUseCase = mockk()
    private val addBalanceUseCase: AddBalanceUseCase = mockk()
    private val authRepository: AuthRepository = mockk()
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(mapOf("userEmail" to "test@example.com"))

    @Test
    fun `toggleBalanceVisibility toggles state`() {
        every { getBalanceUseCase("test@example.com") } returns flowOf(0.0)
        val viewModel = WalletViewModel(getBalanceUseCase, addBalanceUseCase, authRepository, savedStateHandle)
        
        val initial = viewModel.balanceVisible
        viewModel.toggleBalanceVisibility()
        assertNotEquals(initial, viewModel.balanceVisible)
    }

    @Test
    fun `init loads balance`() = runTest {
        val expectedBalance = 500.0
        every { getBalanceUseCase("test@example.com") } returns flowOf(expectedBalance)
        
        val viewModel = WalletViewModel(getBalanceUseCase, addBalanceUseCase, authRepository, savedStateHandle)

        assertEquals(expectedBalance, viewModel.balance, 0.0)
    }

    @Test
    fun `addFunds success calls onResult with success`() = runTest {
        every { getBalanceUseCase("test@example.com") } returns flowOf(0.0)
        coEvery { authRepository.login("test@example.com", "password") } returns Result.success(User("test@example.com", "Name"))
        coEvery { addBalanceUseCase("test@example.com", 100.0) } returns Result.success(Unit)

        val viewModel = WalletViewModel(getBalanceUseCase, addBalanceUseCase, authRepository, savedStateHandle)
        
        var result: Result<Unit>? = null
        viewModel.addFunds(100.0, "password") { result = it }

        assertNotNull(result)
        assertTrue(result?.isSuccess == true)
    }

    @Test
    fun `addFunds wrong password calls onResult with failure`() = runTest {
        every { getBalanceUseCase("test@example.com") } returns flowOf(0.0)
        coEvery { authRepository.login("test@example.com", "wrong") } returns Result.failure(Exception("Invalid"))

        val viewModel = WalletViewModel(getBalanceUseCase, addBalanceUseCase, authRepository, savedStateHandle)
        
        var result: Result<Unit>? = null
        viewModel.addFunds(100.0, "wrong") { result = it }

        assertNotNull(result)
        assertTrue(result?.isFailure == true)
        assertEquals("Invalid password", result?.exceptionOrNull()?.message)
    }
}
