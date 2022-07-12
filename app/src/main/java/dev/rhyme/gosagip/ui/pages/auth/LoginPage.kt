package dev.rhyme.gosagip.ui.pages.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.rhyme.gosagip.data.model.UserType
import dev.rhyme.gosagip.ui.components.AppTitle
import dev.rhyme.gosagip.ui.pages.NavGraphs
import dev.rhyme.gosagip.ui.pages.destinations.BackRidePageDestination
import dev.rhyme.gosagip.ui.pages.destinations.MonitoringPageDestination
import dev.rhyme.gosagip.ui.pages.destinations.RegisterPageDestination
import kotlinx.coroutines.launch

@Destination
@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = hiltViewModel(),
    type: UserType
) {
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.errorState
            .collect {
                Toast.makeText(context, it.ifBlank { "Unknown Error" }, Toast.LENGTH_LONG).show()
            }
    }

    var username: String by remember { mutableStateOf("") }
    var password: String by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(
                state = rememberScrollState()
            )
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
            value = username,
            onValueChange = { username = it },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            label = {
                Text(text = "Username")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = password,
            onValueChange = { password = it },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            label = {
                Text(text = "Password")
            }
        )
        Spacer(modifier = Modifier.height(32.dp))

        val scope = rememberCoroutineScope()
        Button(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = {
                scope.launch {
                    val destination = when (type) {
                        UserType.RIDER -> {
                            if (viewModel.loginRider(username, password))

                                BackRidePageDestination else null
                        }
                        UserType.AMBULANCE -> {
                            if (viewModel.loginAmbulance(username, password))
                                MonitoringPageDestination
                            else null
                        }
                    }
                    if (destination != null) {
                        navigator.navigate(destination) {
                            popUpTo(NavGraphs.root.route)
                        }
                    }
                }
            }
        ) {
            Text(text = "LOGIN")
        }

        if (type == UserType.RIDER) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Don't have an account yet?")
            Text(
                modifier = Modifier.clickable {
                    navigator.navigate(RegisterPageDestination)
                },
                text = "Create now!", color = Color(0xFF2EC4B6)
            )
        }

    }
}