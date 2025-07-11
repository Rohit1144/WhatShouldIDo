package com.example.fit5046_g4_whatshouldido.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fit5046_g4_whatshouldido.R
import com.example.fit5046_g4_whatshouldido.data.local.entity.Quote
import com.example.fit5046_g4_whatshouldido.models.QuoteModel
import com.example.fit5046_g4_whatshouldido.viewmodel.QuoteViewModel


@Composable
fun SavedQuotesScreen(
    navController: NavController,
    viewModel: QuoteViewModel = viewModel()
) {
        val quotes by viewModel.quotes.collectAsState(emptyList())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Saved Quotes",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.dark_gray),
                fontFamily = FontFamily.Default,
                modifier = Modifier.padding(top = 40.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn  (modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
            ) {
                items(quotes) { quote: Quote ->
                    ElevatedCard(
                        modifier = Modifier.padding(16.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = quote.text,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "- ${quote.author}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(
                                onClick = { viewModel.deleteQuote(QuoteModel(quote.text, quote.author))}
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Quote",
                                    tint = colorResource(R.color.gray)
                                )
                            }
                        }

                    }
                }
            }
            Spacer(Modifier.height(32.dp))

            Button (
                onClick ={
                    navController.navigate("profile")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.light_red),
                    disabledContainerColor = colorResource(R.color.light_red)
                )
            ) {
                Text("OK")
            }
            Spacer(Modifier.height(16.dp))
        }
}
