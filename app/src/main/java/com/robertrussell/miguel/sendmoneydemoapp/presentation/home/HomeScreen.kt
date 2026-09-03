package com.robertrussell.miguel.sendmoneydemoapp.presentation.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robertrussell.miguel.sendmoneydemoapp.presentation.sendmoney.SendMoneyScreen
import com.robertrussell.miguel.sendmoneydemoapp.presentation.transaction.TransactionPage
import com.robertrussell.miguel.sendmoneydemoapp.presentation.wallet.WalletPage

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    if (viewModel.currentPage == "wallet") {
        BackHandler {
            viewModel.onShowLogoutDialog(true)
        }
    }

    if (viewModel.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onShowLogoutDialog(false) },
            title = { Text(text = "Logout") },
            text = { Text(text = "Are you sure you want to logout?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onShowLogoutDialog(false)
                    onLogout()
                }) {
                    Text(text = "Yes", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onShowLogoutDialog(false) }) {
                    Text(text = "No")
                }
            }
        )
    }

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

                    TextButton(onClick = { viewModel.onShowLogoutDialog(true) }) {
                        Text(text = "Logout", color = Color.Red, fontSize = 16.sp)
                    }
                }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (viewModel.currentPage) {
                "wallet" -> WalletPage(
                    onViewTransactions = { viewModel.onCurrentPageChange("transactions") },
                    onSendMoney = { viewModel.onCurrentPageChange("send_money") }
                )
                "transactions" -> TransactionPage(
                    onBackPressed = { viewModel.onCurrentPageChange("wallet") }
                )
                "send_money" -> SendMoneyScreen(
                    onBackPressed = { viewModel.onCurrentPageChange("wallet") }
                )
            }
        }
    }
}
