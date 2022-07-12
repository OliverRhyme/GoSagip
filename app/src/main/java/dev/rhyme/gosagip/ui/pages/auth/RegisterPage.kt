package dev.rhyme.gosagip.ui.pages.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import dev.rhyme.gosagip.data.model.BloodType
import dev.rhyme.gosagip.ui.pages.CommonPage
import dev.rhyme.gosagip.ui.pages.NavGraphs
import dev.rhyme.gosagip.ui.pages.destinations.BackRidePageDestination
import dev.rhyme.gosagip.ui.theme.GoSagipTheme
import kotlinx.coroutines.launch

@Destination
@Composable
fun RegisterPage(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.errorState
            .collect {
                Toast.makeText(context, it.ifBlank { "Unknown Error" }, Toast.LENGTH_LONG).show()
            }
    }

    CommonPage(
        modifier = Modifier.verticalScroll(
            state = rememberScrollState()
        )
    ) {

        val username: MutableState<String> = remember { mutableStateOf("") }
        val password: MutableState<String> = remember { mutableStateOf("") }
        val fullName: MutableState<String> = remember { mutableStateOf("") }
        val address: MutableState<String> = remember { mutableStateOf("") }
        val plateNumber: MutableState<String> = remember { mutableStateOf("") }
        val bloodType: MutableState<BloodType?> = remember { mutableStateOf(null) }
        val emergencyContact: MutableState<String> = remember { mutableStateOf("") }
        val emergencyContactNumber: MutableState<String> = remember { mutableStateOf("") }
        val deviceId: MutableState<String> = remember { mutableStateOf("") }

        val scope = rememberCoroutineScope()
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Create Account",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))
        DetailsForm(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            username = username,
            password = password,
            fullName = fullName,
            address = address,
            plateNumber = plateNumber,
            bloodType = bloodType,
            emergencyContact = emergencyContact,
            emergencyContactNumber = emergencyContactNumber,
            deviceId = deviceId,
            submitText = "REGISTER",
            onSubmit = {
                scope.launch {
                    if (viewModel.registerRider(
                            username = username.value,
                            password = password.value,
                            fullName = fullName.value,
                            address = address.value,
                            plateNumber = plateNumber.value,
                            bloodType = bloodType.value!!,
                            emergencyContact = emergencyContact.value,
                            emergencyContactNumber = emergencyContactNumber.value,
                            deviceId = deviceId.value
                        )
                    ) {
                        Toast.makeText(
                            context,
                            "Successfully registered",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigator.navigate(BackRidePageDestination) {
                            popUpTo(NavGraphs.root.route)
                        }
                    }
                }

            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailsForm(
    modifier: Modifier = Modifier,
    username: MutableState<String>,
    password: MutableState<String>? = null,
    fullName: MutableState<String>,
    address: MutableState<String>,
    plateNumber: MutableState<String>,
    bloodType: MutableState<BloodType?>,
    emergencyContact: MutableState<String>,
    emergencyContactNumber: MutableState<String>,
    deviceId: MutableState<String>? = null,
    submitText: String,
    onSubmit: () -> Unit
) {
    Column(
        modifier = modifier
    ) {

        val fieldPadding = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 24.dp)

        val fieldColors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )

        TextField(
            modifier = Modifier
                .then(fieldPadding),
            value = username.value,
            label = {
                Text(text = "User Name")
            },
            onValueChange = { username.value = it },
            colors = fieldColors
        )
        password?.let { state ->
            TextField(
                modifier = Modifier
                    .then(fieldPadding),
                value = state.value,
                label = {
                    Text(text = "Password")
                },
                onValueChange = { state.value = it },
                colors = fieldColors
            )
        }
        TextField(
            modifier = Modifier
                .then(fieldPadding),
            value = fullName.value,
            label = {
                Text(text = "Full Name")
            },
            onValueChange = { fullName.value = it },
            colors = fieldColors
        )
        TextField(
            modifier = Modifier
                .then(fieldPadding),
            value = address.value,
            label = {
                Text(text = "Complete Address")
            },
            onValueChange = { address.value = it },
            colors = fieldColors,
            maxLines = 3
        )
        TextField(
            modifier = Modifier
                .then(fieldPadding),
            value = plateNumber.value,
            label = {
                Text(text = "Plate Number")
            },
            onValueChange = { plateNumber.value = it },
            colors = fieldColors,
            maxLines = 3
        )

        var bloodTypeExpanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            modifier = Modifier
                .then(fieldPadding),
            expanded = bloodTypeExpanded,
            onExpandedChange = {
                bloodTypeExpanded = !bloodTypeExpanded
            }
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                value = bloodType.value?.displayName ?: "",
                onValueChange = { },
                label = { Text("Blood Type") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = bloodTypeExpanded,
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            ExposedDropdownMenu(
                expanded = bloodTypeExpanded,
                onDismissRequest = {
                    bloodTypeExpanded = false
                }
            ) {
                BloodType.values().forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            bloodType.value = selectionOption
                            bloodTypeExpanded = false
                        }
                    ) {
                        Text(text = selectionOption.displayName)
                    }
                }
            }
        }
        TextField(
            modifier = Modifier
                .then(fieldPadding),
            value = emergencyContact.value,
            label = {
                Text(text = "Emergency Contact")
            },
            onValueChange = { emergencyContact.value = it },
            colors = fieldColors,
            maxLines = 1
        )
        TextField(
            modifier = Modifier
                .then(fieldPadding),
            value = emergencyContactNumber.value,
            label = {
                Text(text = "Emergency Contact Number")
            },
            onValueChange = { emergencyContactNumber.value = it },
            colors = fieldColors,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            )
        )
        deviceId?.let { state ->
            TextField(
                modifier = Modifier
                    .then(fieldPadding),
                value = state.value,
                label = {
                    Text(text = "Device ID")
                },
                onValueChange = { state.value = it },
                colors = fieldColors,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally),
            onClick = onSubmit
        ) {
            Text(text = submitText)
        }
    }
}

@Preview
@Composable
fun BackRidePagePreview() {
    GoSagipTheme {
        Surface(color = MaterialTheme.colors.background) {
            RegisterPage(
                navigator = EmptyDestinationsNavigator
            )
        }
    }
}