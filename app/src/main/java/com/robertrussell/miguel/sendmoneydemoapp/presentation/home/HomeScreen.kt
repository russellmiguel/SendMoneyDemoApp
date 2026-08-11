package com.robertrussell.miguel.sendmoneydemoapp.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var currentPage by remember { mutableStateOf("wallet") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hello! ${viewModel.userName}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                TextButton(onClick = onLogout) {
                    Text(text = "Logout", color = Color.Red, fontSize = 16.sp)
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentPage) {
                "wallet" -> WalletPage(
                    balanceVisible = viewModel.balanceVisible,
                    onToggleBalance = viewModel::toggleBalanceVisibility,
                    onViewTransactions = { currentPage = "transactions" }
                )
                "transactions" -> TransactionPage(
                    transactions = viewModel.transactions,
                    onBackPressed = { currentPage = "wallet" }
                )
            }
        }
    }
}
