package com.robertrussell.miguel.sendmoneydemoapp.presentation.wallet

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.robertrussell.miguel.sendmoneydemoapp.util.formatNumber
import com.robertrussell.miguel.sendmoneydemoapp.util.maskNumbers

@Composable
fun WalletPage(
    onViewTransactions: () -> Unit,
    onSendMoney: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val lightBlue = Color(0xFFE3F2FD)
    val borderGray = Color(0xFFD0D0D0)
    val context = LocalContext.current

    var showAddFundsDialog by remember { mutableStateOf(false) }

    if (showAddFundsDialog) {
        var amountText by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddFundsDialog = false },
            properties = DialogProperties(dismissOnClickOutside = false),
            title = { Text(text = "Add Funds") },
            text = {
                Column {
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = {
                            if (it.length <= 10 && (it.isEmpty() || it.toDoubleOrNull() != null || it == ".")) {
                                amountText = it
                            }
                        },
                        label = { Text("Amount") },
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            if (it.length <= 20) {
                                password = it
                            }
                        },
                        label = { Text("Enter Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        maxLines = 1
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = amountText.toDoubleOrNull() ?: 0.0
                        if (amount > 0 && password.isNotEmpty()) {
                            viewModel.addFunds(amount, password) { result ->
                                if (result.isSuccess) {
                                    showAddFundsDialog = false
                                    Toast.makeText(context, "Funds added successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, result.exceptionOrNull()?.message ?: "Error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Invalid amount and/or credentials!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    if (viewModel.isProcessing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(12.dp))
                    } else {
                        Text("Submit")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddFundsDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, borderGray)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Wallet Balance",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, borderGray, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (viewModel.balanceVisible) {
                            Text(
                                text = "₱ ${formatNumber(viewModel.balance)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                            )
                        } else {
                            Text(
                                text = "₱ ${formatNumber(viewModel.balance).maskNumbers()}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                            )
                        }

                        Row {
                            IconButton(onClick = { showAddFundsDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            IconButton(onClick = viewModel::toggleBalanceVisibility) {
                                Icon(
                                    imageVector = if (viewModel.balanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ActionRow(text = "Send Money", containerColor = lightBlue, onClick = onSendMoney)
                Spacer(modifier = Modifier.height(8.dp))
                ActionRow(text = "View Transactions", containerColor = lightBlue, onClick = onViewTransactions)
            }
        }
    }
}

@Composable
fun ActionRow(
    text: String,
    containerColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = Color.Black),
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Icon(Icons.Default.ChevronRight, contentDescription = null)
        }
    }
}
