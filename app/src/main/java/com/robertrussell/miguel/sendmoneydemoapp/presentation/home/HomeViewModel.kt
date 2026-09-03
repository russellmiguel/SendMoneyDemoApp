package com.robertrussell.miguel.sendmoneydemoapp.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val userName: String = savedStateHandle.get<String>("userName") ?: "User"

    var currentPage by mutableStateOf("wallet")
        private set
    var showLogoutDialog by mutableStateOf(false)
        private set

    fun onCurrentPageChange(current: String) {
        currentPage = current
    }

    fun onShowLogoutDialog(showDialog: Boolean) {
        showLogoutDialog = showDialog
    }
}
