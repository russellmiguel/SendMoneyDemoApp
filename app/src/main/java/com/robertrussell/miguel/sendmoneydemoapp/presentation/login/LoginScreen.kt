package com.robertrussell.miguel.sendmoneydemoapp.presentation.login

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robertrussell.miguel.sendmoneydemoapp.domain.model.User
import com.robertrussell.miguel.sendmoneydemoapp.ui.theme.SendMoneyDemoAppTheme

@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: (User) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lightGray = Color(0xFFE8E8E8)
    val borderGray = Color(0xFFD0D0D0)
    val helpRed = Color(0xFFD32F2F)

    BackHandler {
        (context as? Activity)?.finish()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Sign in",
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // Email Field
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = viewModel::onEmailChange,
            placeholder = { Text("Email", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = borderGray,
                focusedBorderColor = Color.DarkGray,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = viewModel.password,
            onValueChange = viewModel::onPasswordChange,
            placeholder = { Text("Password", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = borderGray,
                focusedBorderColor = Color.DarkGray,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            trailingIcon = {
                val image =
                    if (viewModel.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = viewModel::togglePasswordVisibility) {
                    Icon(image, contentDescription = null, tint = Color.LightGray)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.login(
                    onSuccess = { user ->
                        Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                        onNavigateToHome(user)
                    },
                    onError = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = lightGray,
                contentColor = Color.DarkGray
            )
        ) {
            Text(text = "Log in", fontSize = 20.sp, fontWeight = FontWeight.Normal)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = { /* Handle Help */ },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Forgot password?",
                    color = helpRed,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            TextButton(
                onClick = onNavigateToSignUp,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Sign up",
                    color = Color.DarkGray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SendMoneyDemoAppTheme {
        LoginScreen(onNavigateToSignUp = {}, onNavigateToHome = {})
    }
}
