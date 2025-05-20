package com.example.fit5046_g4_whatshouldido.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fit5046_g4_whatshouldido.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmationBottomSheet(
    onConfirmDelete: (String?) -> Unit,
    onDismiss: () -> Unit,
    deleteType: DeleteType,
    showPasswordField: Boolean = false
) {
    val (title, message) = when(deleteType) {
        DeleteType.TASK -> "Delete Task?" to "Are you sure you want to delete this task? This action cannot be undone."
        DeleteType.FACTORY_RESET -> "Factory Reset?" to "This will delete all your data. Are you sure you want to continue?"
        DeleteType.ACCOUNT -> "Delete Account?" to "Your account and all related data will be permanently removed. This cannot be undone."
    }

    var password by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(message)

            if(deleteType == DeleteType.ACCOUNT && showPasswordField) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    placeholder = { Text("Enter your password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { onConfirmDelete(if (showPasswordField) password else null) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.light_red),
                        disabledContainerColor = colorResource(R.color.light_red)
                    )
                ) {
                    Text(if(deleteType != DeleteType.FACTORY_RESET) "Delete" else "Reset")
                }

                Button(
                    onClick = onDismiss,
                    border = BorderStroke(1.dp, color = Color.DarkGray),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )
                ) {
                    Text("Cancel", color = Color.DarkGray)
                }
            }
        }
    }
}

enum class DeleteType {
    TASK, FACTORY_RESET, ACCOUNT
}
