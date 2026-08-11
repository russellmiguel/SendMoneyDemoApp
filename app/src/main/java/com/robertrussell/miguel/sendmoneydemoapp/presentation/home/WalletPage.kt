package com.robertrussell.miguel.sendmoneydemoapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WalletPage(
    balanceVisible: Boolean,
    onToggleBalance: () -> Unit,
    onViewTransactions: () -> Unit
) {
    val lightBlue = Color(0xFFE3F2FD)
    val borderGray = Color(0xFFD0D0D0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, borderGray)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Card",
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
                        Text(
                            text = "Wallet Balance",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Row {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = onToggleBalance) {
                                Icon(
                                    imageVector = if (balanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                    if (balanceVisible) {
                        Text(
                            text = "$ 1,234.56", // Mock balance
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ActionRow(text = "Send Money", containerColor = lightBlue) { /* TODO */ }
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
