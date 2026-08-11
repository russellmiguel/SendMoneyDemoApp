package com.robertrussell.miguel.sendmoneydemoapp.presentation.home

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
class HomeViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val userName: String = savedStateHandle.get<String>("userName") ?: "User"
    val userEmail: String = savedStateHandle.get<String>("userEmail") ?: ""

    var transactions by mutableStateOf<List<Transaction>>(emptyList())
        private set

    init {
        loadData()
    }

    fun loadData() {
        if (userEmail.isNotEmpty()) {
            viewModelScope.launch {
                getTransactionsUseCase(userEmail).collectLatest {
                    transactions = it
                }
            }
        }
    }
}
