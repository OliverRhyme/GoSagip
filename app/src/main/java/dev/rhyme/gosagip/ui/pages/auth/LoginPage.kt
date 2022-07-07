package dev.rhyme.gosagip.ui.pages.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.rhyme.gosagip.ui.components.AppTitle
import dev.rhyme.gosagip.ui.pages.destinations.RegisterPageDestination

@RootNavGraph(start = true)
@Destination
@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppTitle(fontSize = 42.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "WELCOME!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = "",
            onValueChange = {},
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = "",
            onValueChange = {},
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = {
                navigator.navigate(RegisterPageDestination)
            }
        ) {
            Text(text = "LOGIN")
        }

    }
}