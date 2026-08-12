package com.robertrussell.miguel.sendmoneydemoapp.presentation.home

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeViewModelTest {

    @Test
    fun `userName should be retrieved from savedStateHandle`() {
        val savedStateHandle = SavedStateHandle(mapOf("userName" to "John"))
        val viewModel = HomeViewModel(savedStateHandle)
        assertEquals("John", viewModel.userName)
    }

    @Test
    fun `userName should default to User if not in savedStateHandle`() {
        val savedStateHandle = SavedStateHandle()
        val viewModel = HomeViewModel(savedStateHandle)
        assertEquals("User", viewModel.userName)
    }
}
