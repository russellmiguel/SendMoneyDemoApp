package com.robertrussell.miguel.sendmoneydemoapp.presentation.transaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertrussell.miguel.sendmoneydemoapp.domain.model.Transaction
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val userEmail: String = savedStateHandle.get<String>("userEmail") ?: ""

    var transactions by mutableStateOf<List<Transaction>>(emptyList())
        private set

    var isProcessing by mutableStateOf(false)
        private set

    private var localTransactions = emptyList<Transaction>()
    private var remoteTransactions = emptyList<Transaction>()

    init {
        loadTransactions()
    }

    fun loadTransactions() {
        if (userEmail.isNotEmpty()) {
            viewModelScope.launch {
                getTransactionsUseCase(userEmail).collectLatest { local ->
                    localTransactions = local
                    if (local.isEmpty()) {
                        // If local is empty, pull remote data
                        loadRemoteTransactions()
                    } else {
                        // If not empty, combine local and remote
                        if (remoteTransactions.isEmpty() && !isProcessing) {
                            loadRemoteTransactions()
                        } else {
                            updateTransactionsState()
                        }
                    }
                }
            }
        }
    }

    private fun updateTransactionsState() {
        // Combine local and remote, then sort by date descending
        transactions = (localTransactions + remoteTransactions)
            .distinctBy { "${it.id}_${it.type}_${it.date}" } // Handle potential ID overlaps from different sources
            .sortedByDescending { it.date }
    }

    /**
     * Simulation of fetching passed transactions from server.
     * No defined security such as user credential validation, used only fake API.
     */
    fun loadRemoteTransactions() {
        viewModelScope.launch {
            isProcessing = true
            val result = getTransactionsUseCase.getRemoteTransactions()
            if (result.isSuccess) {
                remoteTransactions = result.getOrNull() ?: emptyList()
            }
            updateTransactionsState()
            isProcessing = false
        }
    }
}
