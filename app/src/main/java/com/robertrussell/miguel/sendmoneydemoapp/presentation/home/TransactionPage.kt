package com.robertrussell.miguel.sendmoneydemoapp.presentation.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robertrussell.miguel.sendmoneydemoapp.domain.model.Transaction
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionPage(
    transactions: List<Transaction>,
    onBackPressed: () -> Unit
) {
    BackHandler(onBack = onBackPressed)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Transactions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (transactions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No transactions found", color = Color.Gray)
            }
        } else {
            LazyColumn {
                items(transactions) { transaction ->
                    TransactionItem(transaction)
                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    val dateString = sdf.format(Date(transaction.date))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = transaction.recipient,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = dateString,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Text(
            text = "${if (transaction.type == "SEND") "-" else "+"} ₱ ${String.format("%.2f", transaction.amount)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (transaction.type == "SEND") Color.Red else Color(0xFF4CAF50)
        )
    }
}
