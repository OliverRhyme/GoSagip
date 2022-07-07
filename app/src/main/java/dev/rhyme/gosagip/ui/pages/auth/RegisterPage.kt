package dev.rhyme.gosagip.ui.pages.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import dev.rhyme.gosagip.data.model.BloodType
import dev.rhyme.gosagip.ui.pages.CommonPage
import dev.rhyme.gosagip.ui.theme.GoSagipTheme

@Destination
@Composable
fun RegisterPage() {
    CommonPage {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Create Account",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))
        DetailsForm(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fullName = remember { mutableStateOf("") },
            address = remember { mutableStateOf("") },
            plateNumber = remember { mutableStateOf("") },
            bloodType = remember { mutableStateOf(null) },
            emergencyContact = remember { mutableStateOf("") },
            emergencyContactNumber = remember { mutableStateOf("null") },
            deviceId = remember { mutableStateOf("null") },
            submitText = "REGISTER",
            onSubmit = {
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailsForm(
    modifier: Modifier = Modifier,
    fullName: MutableState<String>,
    address: MutableState<String>,
    plateNumber: MutableState<String>,
    bloodType: MutableState<BloodType?>,
    emergencyContact: MutableState<String>,
    emergencyContactNumber: MutableState<String>,
    deviceId: MutableState<String>,
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
            maxLines = 1
        )
        TextField(
            modifier = Modifier
                .then(fieldPadding),
            value = deviceId.value,
            label = {
                Text(text = "Device ID")
            },
            onValueChange = { deviceId.value = it },
            colors = fieldColors,
            maxLines = 1
        )
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
            RegisterPage()
        }
    }
}