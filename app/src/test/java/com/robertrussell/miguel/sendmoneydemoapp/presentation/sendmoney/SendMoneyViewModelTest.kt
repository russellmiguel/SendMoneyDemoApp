package com.robertrussell.miguel.sendmoneydemoapp.presentation.sendmoney

import androidx.lifecycle.SavedStateHandle
import com.robertrussell.miguel.sendmoneydemoapp.domain.model.User
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.AuthRepository
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.GetBalanceUseCase
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.SendMoneyUseCase
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

@OptIn(ExperimentalCoroutinesApi::class)
class SendMoneyViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val sendMoneyUseCase: SendMoneyUseCase = mockk()
    private val getBalanceUseCase: GetBalanceUseCase = mockk()
    private val authRepository: AuthRepository = mockk()
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(mapOf("userEmail" to "test@example.com"))

    private lateinit var viewModel: SendMoneyViewModel

    private fun setupViewModel() {
        viewModel = SendMoneyViewModel(sendMoneyUseCase, getBalanceUseCase, authRepository, savedStateHandle)
    }

    @Test
    fun `onAmountChange updates amountText`() {
        setupViewModel()
        viewModel.onAmountChange("100.5")
        assertEquals("100.5", viewModel.amountText)
    }

    @Test
    fun `onNumberClick appends numbers`() {
        setupViewModel()
        viewModel.onNumberClick("1")
        viewModel.onNumberClick("0")
        assertEquals("10", viewModel.amountText)
    }

    @Test
    fun `onBackspace removes last character`() {
        setupViewModel()
        viewModel.onAmountChange("100")
        viewModel.onBackspace()
        assertEquals("10", viewModel.amountText)
    }

    @Test
    fun `sendMoney success calls onResult with success`() = runTest {
        setupViewModel()
        viewModel.onAmountChange("50")
        coEvery { authRepository.login("test@example.com", "password") } returns Result.success(User("test@example.com", "Name"))
        every { getBalanceUseCase("test@example.com") } returns flowOf(100.0)
        coEvery { sendMoneyUseCase("test@example.com", 50.0, "Recipient") } returns Result.success(Unit)

        var result: Result<Unit>? = null
        viewModel.sendMoney("password", "Recipient") { result = it }

        assertNotNull(result)
        assertTrue(result?.isSuccess == true)
    }

    @Test
    fun `sendMoney insufficient balance returns error`() = runTest {
        setupViewModel()
        viewModel.onAmountChange("150")
        coEvery { authRepository.login("test@example.com", "password") } returns Result.success(User("test@example.com", "Name"))
        every { getBalanceUseCase("test@example.com") } returns flowOf(100.0)

        var result: Result<Unit>? = null
        viewModel.sendMoney("password", "Recipient") { result = it }

        assertNotNull(result)
        assertTrue(result?.isFailure == true)
        assertEquals("Insufficient balance", result?.exceptionOrNull()?.message)
    }
}
