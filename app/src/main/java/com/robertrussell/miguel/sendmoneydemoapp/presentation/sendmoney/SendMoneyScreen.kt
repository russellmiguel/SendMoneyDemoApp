package com.robertrussell.miguel.sendmoneydemoapp.presentation.sendmoney

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robertrussell.miguel.sendmoneydemoapp.util.formatNumber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMoneyScreen(
    onBackPressed: () -> Unit,
    viewModel: SendMoneyViewModel = hiltViewModel()
) {
    var showPasswordDialog by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var recipient by remember { mutableStateOf("") }
    var transactionResult by remember { mutableStateOf<Result<Unit>?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    BackHandler(onBack = {
        onBackPressed.invoke()
        viewModel.clearAmount()
    })

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Confirm Transaction") },
            text = {
                Column {
                    Text("Enter password to send ₱${formatNumber(viewModel.amountText.toDouble())}")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPasswordDialog = false
                        viewModel.sendMoney(password, recipient) { result ->
                            transactionResult = result
                            showSheet = true
                        }
                        password = ""
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (transactionResult?.isSuccess == true) "Success!" else "Failed",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (transactionResult?.isSuccess == true) Color(0xFF4CAF50) else Color.Red
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = transactionResult?.exceptionOrNull()?.message
                        ?: "Transaction processed successfully"
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        showSheet = false
                        if (transactionResult?.isSuccess == true) {
                            onBackPressed()
                            viewModel.clearAmount()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                onBackPressed.invoke()
                viewModel.clearAmount()
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Send Money",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Recipient name
        OutlinedTextField(
            value = recipient,
            onValueChange = {
                if (it.length <= 30) {
                    recipient = it
                }
            },
            placeholder = { Text("Name", color = Color.LightGray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            maxLines = 1,
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFD0D0D0),
                focusedBorderColor = Color.DarkGray,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            trailingIcon = {
                if (recipient.isNotEmpty()) {
                    IconButton(onClick = { recipient = "" }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = Color.LightGray
                        )
                    }
                }
            }
        )

        // Amount Display
        Card(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "₱", fontSize = 24.sp, color = Color.Gray)
                val amount = viewModel.amountText.take(10)
                Text(
                    text = if (viewModel.amountText.isEmpty()) "0" else formatNumber(amount.toDouble()),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = { if (viewModel.amountText.isNotEmpty()) showPasswordDialog = true },
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A5276))
        ) {
            if (viewModel.isProcessing) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Submit", fontSize = 18.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Keypad
        NumericKeypad(
            onNumberClick = viewModel::onNumberClick,
            onBackspace = viewModel::onBackspace
        )
    }
}

@Composable
fun NumericKeypad(
    onNumberClick: (String) -> Unit,
    onBackspace: () -> Unit
) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf(".", "0", "BACK")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        keys.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { key ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(72.dp)
                            .clickable {
                                if (key == "BACK") onBackspace() else onNumberClick(key)
                            }
                            .border(1.dp, Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        if (key == "BACK") {
                            Icon(
                                Icons.Default.Backspace,
                                contentDescription = "Backspace",
                                tint = Color.Gray
                            )
                        } else {
                            Text(text = key, fontSize = 24.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}
