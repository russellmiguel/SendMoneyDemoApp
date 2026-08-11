package com.robertrussell.miguel.sendmoneydemoapp.presentation.sendmoney

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertrussell.miguel.sendmoneydemoapp.data.local.UserDao
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.AuthRepository
import com.robertrussell.miguel.sendmoneydemoapp.domain.security.PasswordHasher
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.GetBalanceUseCase
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.SendMoneyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendMoneyViewModel @Inject constructor(
    private val sendMoneyUseCase: SendMoneyUseCase,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val userEmail: String = savedStateHandle.get<String>("userEmail") ?: ""

    var amountText by mutableStateOf("")
        private set

    var isProcessing by mutableStateOf(false)
        private set

    fun onAmountChange(newAmount: String) {
        if (newAmount.isEmpty() || newAmount.toDoubleOrNull() != null || newAmount == ".") {
            amountText = newAmount
        }
    }

    fun clearAmount() {
        amountText = ""
    }

    fun onNumberClick(number: String) {
        if (amountText == "0" && number != ".") {
            amountText = number
        } else {
            amountText += number
        }
    }

    fun onBackspace() {
        if (amountText.isNotEmpty()) {
            amountText = amountText.dropLast(1)
        }
    }

    fun sendMoney(password: String, recipient: String, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            isProcessing = true
            try {
                // Verify Password
                val user = authRepository.login(userEmail, password)
                if (user.isFailure) {
                    onResult(Result.failure(Exception("Invalid password")))
                    return@launch
                }

                // Check Balance
                val currentBalance = getBalanceUseCase(userEmail).first()
                val amount = amountText.toDoubleOrNull() ?: 0.0
                if (amount <= 0) {
                    onResult(Result.failure(Exception("Invalid amount")))
                    return@launch
                }
                if (amount > currentBalance) {
                    onResult(Result.failure(Exception("Insufficient balance")))
                    return@launch
                }

                // Execute Send
                val result = sendMoneyUseCase(userEmail, amount, recipient)
                onResult(result)
            } finally {
                isProcessing = false
            }
        }
    }
}
