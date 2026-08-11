package com.robertrussell.miguel.sendmoneydemoapp.presentation.wallet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.AuthRepository
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.AddBalanceUseCase
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.GetBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getBalanceUseCase: GetBalanceUseCase,
    private val addBalanceUseCase: AddBalanceUseCase,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val userEmail: String = savedStateHandle.get<String>("userEmail") ?: ""

    var balanceVisible by mutableStateOf(false)
        private set

    var balance by mutableStateOf(0.0)
        private set

    var isProcessing by mutableStateOf(false)
        private set

    fun toggleBalanceVisibility() {
        balanceVisible = !balanceVisible
    }

    init {
        loadData()
    }

    fun loadData() {
        if (userEmail.isNotEmpty()) {
            viewModelScope.launch {
                getBalanceUseCase(userEmail).collectLatest { newBalance ->
                    balance = newBalance
                }
            }
        }
    }

    fun addFunds(amount: Double, password: String, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            isProcessing = true
            try {
                // Verify Password
                val user = authRepository.login(userEmail, password)
                if (user.isFailure) {
                    onResult(Result.failure(Exception("Invalid password")))
                    return@launch
                }

                // Execute Add balance
                val result = addBalanceUseCase(userEmail, amount)
                onResult(result)
            } finally {
                isProcessing = false
            }
        }
    }
}